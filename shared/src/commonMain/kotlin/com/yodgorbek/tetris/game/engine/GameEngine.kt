package com.yodgorbek.tetris.game.engine

import com.yodgorbek.tetris.game.logic.GameLogic
import com.yodgorbek.tetris.game.logic.Randomizer
import com.yodgorbek.tetris.game.model.Offset
import com.yodgorbek.tetris.game.state.GameAction
import com.yodgorbek.tetris.game.state.GameState
import com.yodgorbek.tetris.game.state.GameStatus
import com.yodgorbek.tetris.util.AudioManager
import com.yodgorbek.tetris.util.SoundType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlin.math.pow

class GameEngine(
    private val logic: GameLogic,
    private val randomizer: Randomizer,
    private val audioManager: AudioManager,
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private var gameLoopJob: Job? = null

    fun dispatch(action: GameAction) {
        when (action) {
            GameAction.Start -> startGame()
            GameAction.Pause -> pauseGame()
            GameAction.Resume -> resumeGame()
            GameAction.Restart -> restartGame()
            GameAction.MoveLeft -> {
                _state.update { currentState ->
                    val nextState = logic.move(currentState, Offset(-1, 0))
                    if (nextState != currentState) {
                        audioManager.playSound(SoundType.MOVE)
                    }
                    nextState
                }
            }
            GameAction.MoveRight -> {
                _state.update { currentState ->
                    val nextState = logic.move(currentState, Offset(1, 0))
                    if (nextState != currentState) {
                        audioManager.playSound(SoundType.MOVE)
                    }
                    nextState
                }
            }
            GameAction.MoveDown -> tick()
            GameAction.RotateClockwise -> {
                _state.update { currentState ->
                    val nextState = logic.rotate(currentState, true)
                    if (nextState != currentState) {
                        audioManager.playSound(SoundType.ROTATE)
                    }
                    nextState
                }
            }
            GameAction.RotateCounterClockwise -> {
                _state.update { currentState ->
                    val nextState = logic.rotate(currentState, false)
                    if (nextState != currentState) {
                        audioManager.playSound(SoundType.ROTATE)
                    }
                    nextState
                }
            }
            GameAction.HardDrop -> _state.update { hardDrop(it) }
            GameAction.Hold -> _state.update { holdPiece(it) }
        }
    }

    private fun startGame() {
        if (_state.value.status != GameStatus.IDLE && _state.value.status != GameStatus.GAME_OVER) return

        audioManager.playSound(SoundType.START)
        audioManager.playMusic()

        _state.value = GameState(
            status = GameStatus.RUNNING,
            currentPiece = logic.spawnPiece(randomizer.next()),
            nextPiece = randomizer.next()
        )
        runGameLoop()
    }

    private fun pauseGame() {
        _state.update { it.copy(status = GameStatus.PAUSED) }
        audioManager.stopMusic()
    }

    private fun resumeGame() {
        _state.update { it.copy(status = GameStatus.RUNNING) }
        audioManager.playMusic()
    }

    private fun restartGame() {
        gameLoopJob?.cancel()
        _state.value = GameState()
        startGame()
    }

    private fun runGameLoop() {
        gameLoopJob?.cancel()
        gameLoopJob = scope.launch {
            while (isActive) {
                if (_state.value.status == GameStatus.RUNNING) {
                    if (_state.value.clearingLines.isEmpty()) {
                        delay(getTickDelay())
                        tick()
                    } else {
                        // Wait for line clear animation
                        delay(500)
                        finalizeLineClear()
                    }
                } else {
                    delay(100)
                }
            }
        }
    }

    private fun tick() {
        _state.update { currentState ->
            if (currentState.status != GameStatus.RUNNING || currentState.clearingLines.isNotEmpty()) return@update currentState

            val nextState = logic.move(currentState, Offset(0, 1))
            if (nextState == currentState) {
                processLock(currentState)
            } else {
                nextState
            }
        }
    }

    private fun processLock(currentState: GameState): GameState {
        val lockedState = logic.lockPiece(currentState)
        val timestamp = com.yodgorbek.tetris.currentTimeMillis()

        if (lockedState.clearingLines.isNotEmpty()) {
            audioManager.playSound(SoundType.CLEAR)
            return lockedState.copy(lastLockTimestamp = timestamp)
        }

        audioManager.playSound(SoundType.LOCK)
        return spawnNextPiece(lockedState.copy(lastLockTimestamp = timestamp))
    }

    private fun finalizeLineClear() {
        _state.update { currentState ->
            val clearedState = logic.finalizeLineClear(currentState)
            spawnNextPiece(clearedState)
        }
    }

    private fun spawnNextPiece(state: GameState): GameState {
        val spawnedPiece = logic.spawnPiece(state.nextPiece)

        return if (logic.checkCollision(state.board, spawnedPiece.type, spawnedPiece.rotation, spawnedPiece.offset)) {
            audioManager.stopMusic()
            audioManager.playSound(SoundType.GAME_OVER)
            state.copy(status = GameStatus.GAME_OVER, currentPiece = spawnedPiece)
        } else {
            state.copy(
                currentPiece = spawnedPiece,
                nextPiece = randomizer.next()
            )
        }
    }

    private fun hardDrop(currentState: GameState): GameState {
        if (currentState.status != GameStatus.RUNNING || currentState.currentPiece == null || currentState.clearingLines.isNotEmpty()) return currentState

        var droppedState = currentState
        while (true) {
            val next = logic.move(droppedState, Offset(0, 1))
            if (next == droppedState) break
            droppedState = next
        }
        return processLock(droppedState)
    }

    private fun holdPiece(currentState: GameState): GameState {
        if (!currentState.canHold || currentState.status != GameStatus.RUNNING || currentState.currentPiece == null || currentState.clearingLines.isNotEmpty()) return currentState

        val currentType = currentState.currentPiece.type
        val heldType = currentState.heldPiece

        audioManager.playSound(SoundType.MOVE)

        val targetType = heldType ?: currentState.nextPiece
        val newNextPiece = if (heldType == null) randomizer.next() else currentState.nextPiece
        val spawnedPiece = logic.spawnPiece(targetType)

        return if (logic.checkCollision(currentState.board, spawnedPiece.type, spawnedPiece.rotation, spawnedPiece.offset)) {
            audioManager.stopMusic()
            audioManager.playSound(SoundType.GAME_OVER)
            currentState.copy(
                heldPiece = currentType,
                currentPiece = spawnedPiece,
                nextPiece = newNextPiece,
                canHold = false,
                status = GameStatus.GAME_OVER
            )
        } else {
            currentState.copy(
                heldPiece = currentType,
                currentPiece = spawnedPiece,
                nextPiece = newNextPiece,
                canHold = false
            )
        }
    }

    private fun getTickDelay(): Long {
        val level = _state.value.level
        val speedFactor = 0.8.pow((level - 1).toDouble())
        return (1000 * speedFactor).toLong().coerceAtLeast(100)
    }
}
