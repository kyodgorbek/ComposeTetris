package com.yodgorbek.tetris.game.engine

import com.yodgorbek.tetris.game.logic.CollisionDetector
import com.yodgorbek.tetris.game.logic.LineClearer
import com.yodgorbek.tetris.game.logic.Scorer
import com.yodgorbek.tetris.game.model.Board
import com.yodgorbek.tetris.game.model.Position
import com.yodgorbek.tetris.game.model.Randomizer
import com.yodgorbek.tetris.game.model.Tetromino
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Core game engine that manages game logic and state.
 * This is the brain of the game, completely independent of UI.
 */
class GameEngine(
    private val scope: CoroutineScope,
    private val randomizer: Randomizer = Randomizer()
) {
    private val _state = MutableStateFlow<GameState>(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<GameEvent>()
    val events: SharedFlow<GameEvent> = _events.asSharedFlow()

    private var gameLoopJob: Job? = null

    private val collisionDetector = CollisionDetector()
    private val lineClearer = LineClearer()
    private val scorer = Scorer()

    /**
     * Initialize and start a new game.
     */
    fun startGame() {
        val firstPiece = randomizer.next()
        val nextQueue = randomizer.peekNext(3)
        
        val initialState = GameState.initial(firstPiece, nextQueue)
        _state.value = initialState
        
        scope.launch { _events.emit(GameEvent.GameStarted) }
        startGameLoop()
    }

    /**
     * Handle a game action.
     */
    fun handleAction(action: GameAction) {
        val currentState = _state.value

        if (!currentState.isActive() && action !in listOf(
            GameAction.Resume,
            GameAction.Restart,
            GameAction.Pause
        )) {
            return // Ignore game actions when not active
        }

        when (action) {
            GameAction.MoveLeft -> handleMoveLeft()
            GameAction.MoveRight -> handleMoveRight()
            GameAction.RotateClockwise -> handleRotate(clockwise = true)
            GameAction.RotateCounterClockwise -> handleRotate(clockwise = false)
            GameAction.SoftDrop -> handleSoftDrop()
            GameAction.HardDrop -> handleHardDrop()
            GameAction.Hold -> handleHold()
            GameAction.Pause -> handlePause()
            GameAction.Resume -> handleResume()
            GameAction.Restart -> handleRestart()
            GameAction.Tick -> handleTick()
            GameAction.LockPiece -> handleLockPiece()
            GameAction.SpawnNewPiece -> handleSpawnNewPiece()
            GameAction.ClearLines -> handleClearLines()
        }
    }

    /**
     * Process game tick (called by game loop).
     */
    private fun handleTick() {
        val currentState = _state.value

        if (!currentState.isActive()) return

        // Update drop delta time
        val newDropDeltaTime = currentState.dropDeltaTime + TICK_DURATION

        // Check if piece should drop
        val dropSpeed = getDropSpeed(currentState.level)
        if (newDropDeltaTime >= dropSpeed) {
            // Try to move piece down
            if (canMovePiece(currentState, 0, 1)) {
                // Move down
                _state.value = currentState.copy(
                    currentPieceY = currentState.currentPieceY + 1,
                    dropDeltaTime = Duration.ZERO
                )
                scope.launch { _events.emit(GameEvent.PieceMoved(currentState.currentPieceX, currentState.currentPieceY + 1)) }
            } else {
                // Piece cannot move down, lock it
                handleAction(GameAction.LockPiece)
            }
        } else {
            _state.value = currentState.copy(dropDeltaTime = newDropDeltaTime)
        }

        // Update time elapsed
        _state.value = _state.value.copy(
            timeElapsed = _state.value.timeElapsed + TICK_DURATION
        )
    }

    /**
     * Move piece left.
     */
    private fun handleMoveLeft() {
        val currentState = _state.value
        if (canMovePiece(currentState, -1, 0)) {
            _state.value = currentState.copy(currentPieceX = currentState.currentPieceX - 1)
            scope.launch { _events.emit(GameEvent.PieceMoved(currentState.currentPieceX - 1, currentState.currentPieceY)) }
        } else {
            scope.launch { _events.emit(GameEvent.MovementFailed("left")) }
        }
    }

    /**
     * Move piece right.
     */
    private fun handleMoveRight() {
        val currentState = _state.value
        if (canMovePiece(currentState, 1, 0)) {
            _state.value = currentState.copy(currentPieceX = currentState.currentPieceX + 1)
            scope.launch { _events.emit(GameEvent.PieceMoved(currentState.currentPieceX + 1, currentState.currentPieceY)) }
        } else {
            scope.launch { _events.emit(GameEvent.MovementFailed("right")) }
        }
    }

    /**
     * Rotate piece.
     */
    private fun handleRotate(clockwise: Boolean) {
        val currentState = _state.value
        val currentPiece = currentState.currentPiece ?: return

        val rotatedPiece = currentPiece.rotate(clockwise)
        val canRotate = canPlacePiece(
            currentState,
            rotatedPiece,
            currentState.currentPieceX,
            currentState.currentPieceY
        )

        if (canRotate) {
            _state.value = currentState.copy(currentPiece = rotatedPiece)
            scope.launch { _events.emit(GameEvent.PieceRotated(clockwise)) }
        } else {
            scope.launch { _events.emit(GameEvent.RotationFailed("collision")) }
        }
    }

    /**
     * Soft drop (increase fall speed).
     */
    private fun handleSoftDrop() {
        val currentState = _state.value
        if (canMovePiece(currentState, 0, 1)) {
            _state.value = currentState.copy(
                currentPieceY = currentState.currentPieceY + 1,
                dropDeltaTime = Duration.ZERO,
                score = currentState.score + 1 // Soft drop score
            )
            scope.launch { _events.emit(GameEvent.SoftDropped) }
        }
    }

    /**
     * Hard drop (instant drop).
     */
    private fun handleHardDrop() {
        val currentState = _state.value
        var dropDistance = 0

        var newY = currentState.currentPieceY
        while (canMovePiece(currentState, 0, newY - currentState.currentPieceY + 1)) {
            newY++
            dropDistance++
        }

        val scoreGain = dropDistance * 2 // Hard drop score
        _state.value = currentState.copy(
            currentPieceY = newY,
            score = currentState.score + scoreGain,
            dropDeltaTime = Duration.ZERO
        )

        scope.launch { _events.emit(GameEvent.HardDropped) }
        handleAction(GameAction.LockPiece)
    }

    /**
     * Hold current piece.
     */
    private fun handleHold() {
        val currentState = _state.value
        if (!currentState.canHold) return

        val currentPiece = currentState.currentPiece ?: return
        val heldPiece = currentState.heldPiece

        val newHeld = currentPiece
        val newCurrent = heldPiece ?: randomizer.next()

        _state.value = currentState.copy(
            currentPiece = newCurrent,
            heldPiece = newHeld,
            currentPieceX = 3, // Reset position
            currentPieceY = 0,
            canHold = false
        )

        scope.launch { _events.emit(GameEvent.PieceHeld(newHeld::class.simpleName ?: "Unknown")) }
    }

    /**
     * Lock piece on board.
     */
    private fun handleLockPiece() {
        val currentState = _state.value
        val currentPiece = currentState.currentPiece ?: return

        // Place piece on board
        val blockPositions = currentPiece.getBlocks().map { block ->
            Position(
                currentState.currentPieceY + block.row,
                currentState.currentPieceX + block.column
            )
        }

        var newBoard = currentState.board
        val cells = blockPositions.map { pos ->
            pos to com.yodgorbek.tetris.game.model.Cell(
                occupied = true,
                color = currentPiece.color,
                tetrominoId = currentPiece.id
            )
        }
        newBoard = newBoard.setCells(cells)

        // Check for filled rows
        val filledRows = newBoard.getFilledRows()

        if (filledRows.isNotEmpty()) {
            // Clear lines
            newBoard = newBoard.clearRows(filledRows)
            
            val (scoreGain, updatedCombo) = scorer.calculateScore(
                filledRows.size,
                currentState.combo,
                currentState.backToBackTetris
            )

            _state.value = currentState.copy(
                board = newBoard,
                currentPiece = null,
                nextPieces = currentState.nextPieces.drop(1) + randomizer.next(),
                score = currentState.score + scoreGain,
                lines = currentState.lines + filledRows.size,
                combo = updatedCombo,
                piecesPlaced = currentState.piecesPlaced + 1,
                linesCleared = currentState.linesCleared + filledRows.size,
                canHold = true
            )

            scope.launch { _events.emit(GameEvent.LinesCleared(filledRows.size, filledRows)) }
        } else {
            _state.value = currentState.copy(
                board = newBoard,
                currentPiece = null,
                nextPieces = currentState.nextPieces.drop(1) + randomizer.next(),
                combo = 0,
                piecesPlaced = currentState.piecesPlaced + 1,
                canHold = true
            )
        }

        // Check for level up
        val newLevel = (currentState.lines / 10) + 1
        if (newLevel > currentState.level) {
            _state.value = _state.value.copy(level = newLevel)
            scope.launch { _events.emit(GameEvent.LevelUp(newLevel)) }
        }

        scope.launch { _events.emit(GameEvent.PieceLocked(currentState.currentPieceX, currentState.currentPieceY)) }
        handleAction(GameAction.SpawnNewPiece)
    }

    /**
     * Spawn new piece.
     */
    private fun handleSpawnNewPiece() {
        val currentState = _state.value
        val nextPiece = currentState.nextPieces.firstOrNull() ?: randomizer.next()

        val spawnX = 3
        val spawnY = 0

        // Check if spawn position is valid (game over if not)
        if (!canPlacePiece(currentState, nextPiece, spawnX, spawnY)) {
            _state.value = currentState.copy(gameStatus = GameStatus.GAME_OVER)
            scope.launch { _events.emit(GameEvent.GameOver) }
            stopGameLoop()
            return
        }

        _state.value = currentState.copy(
            currentPiece = nextPiece,
            currentPieceX = spawnX,
            currentPieceY = spawnY
        )

        scope.launch { _events.emit(GameEvent.PieceSpawned(spawnX, spawnY)) }
    }

    /**
     * Clear lines.
     */
    private fun handleClearLines() {
        scope.launch { _events.emit(GameEvent.LineClearAnimation) }
    }

    /**
     * Pause game.
     */
    private fun handlePause() {
        if (_state.value.gameStatus == GameStatus.PLAYING && !_state.value.isPaused) {
            _state.value = _state.value.copy(isPaused = true)
            scope.launch { _events.emit(GameEvent.GamePaused) }
        }
    }

    /**
     * Resume game.
     */
    private fun handleResume() {
        if (_state.value.isPaused) {
            _state.value = _state.value.copy(isPaused = false)
            scope.launch { _events.emit(GameEvent.GameResumed) }
        }
    }

    /**
     * Restart game.
     */
    private fun handleRestart() {
        stopGameLoop()
        startGame()
        scope.launch { _events.emit(GameEvent.GameRestarted) }
    }

    /**
     * Check if piece can be placed at position.
     */
    private fun canPlacePiece(state: GameState, piece: Tetromino, x: Int, y: Int): Boolean {
        val blocks = piece.getBlocks()
        return blocks.all { block ->
            val posX = x + block.column
            val posY = y + block.row
            posX in 0 until Board.COLUMNS &&
                posY in 0 until Board.ROWS &&
                !state.board.isOccupied(Position(posY, posX))
        }
    }

    /**
     * Check if piece can move.
     */
    private fun canMovePiece(state: GameState, dx: Int, dy: Int): Boolean {
        val piece = state.currentPiece ?: return false
        return canPlacePiece(state, piece, state.currentPieceX + dx, state.currentPieceY + dy)
    }

    /**
     * Get drop speed based on level.
     */
    private fun getDropSpeed(level: Int): Duration {
        // Base speed: 500ms, decreases with level
        val speedMs = maxOf(100, 500 - (level - 1) * 50)
        return speedMs.milliseconds
    }

    /**
     * Start the game loop.
     */
    private fun startGameLoop() {
        gameLoopJob?.cancel()
        gameLoopJob = scope.launch {
            while (true) {
                delay(TICK_DURATION.inWholeMilliseconds)
                handleAction(GameAction.Tick)
            }
        }
    }

    /**
     * Stop the game loop.
     */
    private fun stopGameLoop() {
        gameLoopJob?.cancel()
        gameLoopJob = null
    }

    /**
     * Dispose the engine and cleanup resources.
     */
    fun dispose() {
        stopGameLoop()
    }

    companion object {
        private val TICK_DURATION = 16.milliseconds // ~60 FPS
    }
}
