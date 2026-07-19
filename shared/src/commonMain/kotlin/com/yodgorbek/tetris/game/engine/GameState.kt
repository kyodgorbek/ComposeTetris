package com.yodgorbek.tetris.game.engine

import com.yodgorbek.tetris.game.model.Board
import com.yodgorbek.tetris.game.model.Tetromino
import kotlin.time.Duration

/**
 * Immutable representation of the complete game state.
 * This is the Single Source of Truth for the game.
 */
data class GameState(
    // Board state
    val board: Board = Board(),
    
    // Current active piece
    val currentPiece: Tetromino? = null,
    val currentPieceX: Int = 0,
    val currentPieceY: Int = 0,
    
    // Next pieces queue
    val nextPieces: List<Tetromino> = emptyList(),
    
    // Hold piece
    val heldPiece: Tetromino? = null,
    val canHold: Boolean = true,
    
    // Scoring
    val score: Long = 0,
    val lines: Int = 0,
    val level: Int = 1,
    
    // Combo counters
    val combo: Int = 0,
    val backToBackTetris: Boolean = false,
    
    // Game state
    val gameStatus: GameStatus = GameStatus.IDLE,
    val isPaused: Boolean = false,
    
    // Timing
    val timeElapsed: Duration = Duration.ZERO,
    val dropDeltaTime: Duration = Duration.ZERO,
    
    // Statistics
    val piecesPlaced: Int = 0,
    val linesCleared: Int = 0,
    val tetrisCount: Int = 0
) {
    /**
     * Check if the game is currently active.
     */
    fun isActive(): Boolean = gameStatus == GameStatus.PLAYING && !isPaused

    /**
     * Check if the game is over.
     */
    fun isGameOver(): Boolean = gameStatus == GameStatus.GAME_OVER

    /**
     * Check if the game is idle or paused.
     */
    fun isWaiting(): Boolean = gameStatus == GameStatus.IDLE || isPaused

    companion object {
        /**
         * Create initial game state with starting piece and queue.
         */
        fun initial(firstPiece: Tetromino, nextQueue: List<Tetromino>): GameState {
            return GameState(
                currentPiece = firstPiece,
                nextPieces = nextQueue,
                gameStatus = GameStatus.PLAYING
            )
        }
    }
}

/**
 * Represents the overall game status.
 */
enum class GameStatus {
    IDLE,
    PLAYING,
    PAUSED,
    GAME_OVER
}
