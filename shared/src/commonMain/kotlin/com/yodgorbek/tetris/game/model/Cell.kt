package com.yodgorbek.tetris.game.model

data class Cell(
    val occupied: Boolean = false,
    val color: Long = 0xFFCCCCCC, // Default grey
    val tetrominoId: Int = -1
) {
    companion object {
        val Empty = Cell()
    }
}
