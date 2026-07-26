package com.yodgorbek.tetris.game.state

import com.yodgorbek.tetris.game.model.Board
import com.yodgorbek.tetris.game.model.Tetromino
import com.yodgorbek.tetris.game.model.TetrominoType

enum class GameStatus {
    IDLE, RUNNING, PAUSED, GAME_OVER
}

data class GameState(
    val board: Board = Board(),
    val currentPiece: Tetromino? = null,
    val nextPiece: TetrominoType = TetrominoType.I,
    val heldPiece: TetrominoType? = null,
    val canHold: Boolean = true,
    val score: Int = 0,
    val level: Int = 1,
    val lines: Int = 0,
    val status: GameStatus = GameStatus.IDLE,
    val isGhostEnabled: Boolean = true,
    val clearingLines: List<Int> = emptyList(),
    val lastLockTimestamp: Long = 0L
)
