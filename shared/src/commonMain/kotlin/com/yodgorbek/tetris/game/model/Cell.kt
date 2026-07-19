package com.yodgorbek.tetris.game.model

import androidx.compose.ui.graphics.Color

/**
 * Represents a single cell on the game board.
 * Each cell has an occupied state, color, and optional tetromino ID.
 */
data class Cell(
    val occupied: Boolean = false,
    val color: Color = Color.Black,
    val tetrominoId: Int? = null
)
