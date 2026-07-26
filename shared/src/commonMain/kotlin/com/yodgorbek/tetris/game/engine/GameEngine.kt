package com.yodgorbek.tetris.game.engine

import com.yodgorbek.tetris.game.logic.GameLogic
import com.yodgorbek.tetris.game.logic.Randomizer
import com.yodgorbek.tetris.game.model.Offset
import com.yodgorbek.tetris.game.state.GameAction
import com.yodgorbek.tetris.game.state.GameState
import com.yodgorbek.tetris.game.state.GameStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.pow

class GameEngine(
    private val logic: GameLogic = GameLogic(),
    private val randomizer: Randomizer = Randomizer(),
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
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
            GameAction.MoveLeft -> _state.update { logic.move(it, Offset(-1, 0)) }
            GameAction.MoveRight -> _state.update { logic.move(it, Offset(1, 0)) }
            GameAction.MoveDown -> tick()
            GameAction.RotateClockwise -> _state.update { logic.rotate(it, true) }
            GameAction.RotateCounterClockwise -> _state.update { logic.rotate(it, false) }
            GameAction.HardDrop -> _state.update { hardDrop(it) }
            GameAction.Hold -> _state.update { holdPiece(it) }
        }
    }

    private fun startGame() {
        if (_state.value.status != GameStatus.IDLE && _state.value.status != GameStatus.GAME_OVER) return

        _state.value = GameState(
            status = GameStatus.RUNNING,
            currentPiece = logic.spawnPiece(randomizer.next()),
            nextPiece = randomizer.next()
        )
        runGameLoop()
    }

    private fun pauseGame() {
        _state.update { it.copy(status = GameStatus.PAUSED) }
    }

    private fun resumeGame() {
        _state.update { it.copy(status = GameStatus.RUNNING) }
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
                    delay(getTickDelay())
                    tick()
                } else {
                    delay(100)
                }
            }
        }
    }

    private fun tick() {
        _state.update { currentState ->
            if (currentState.status != GameStatus.RUNNING) return@update currentState

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
        val spawnedPiece = logic.spawnPiece(lockedState.nextPiece)

        return if (logic.checkCollision(lockedState.board, spawnedPiece.type, spawnedPiece.rotation, spawnedPiece.offset)) {
            lockedState.copy(status = GameStatus.GAME_OVER, currentPiece = spawnedPiece)
        } else {
            lockedState.copy(
                currentPiece = spawnedPiece,
                nextPiece = randomizer.next()
            )
        }
    }

    private fun hardDrop(currentState: GameState): GameState {
        if (currentState.status != GameStatus.RUNNING || currentState.currentPiece == null) return currentState

        var droppedState = currentState
        while (true) {
            val next = logic.move(droppedState, Offset(0, 1))
            if (next == droppedState) break
            droppedState = next
        }
        return processLock(droppedState)
    }

    private fun holdPiece(currentState: GameState): GameState {
        if (!currentState.canHold || currentState.status != GameStatus.RUNNING || currentState.currentPiece == null) return currentState

        val currentType = currentState.currentPiece.type
        val heldType = currentState.heldPiece

        return if (heldType == null) {
            currentState.copy(
                heldPiece = currentType,
                currentPiece = logic.spawnPiece(currentState.nextPiece),
                nextPiece = randomizer.next(),
                canHold = false
            )
        } else {
            currentState.copy(
                heldPiece = currentType,
                currentPiece = logic.spawnPiece(heldType),
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
