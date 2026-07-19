package com.yodgorbek.tetris.game.model

/**
 * Represents a position on the game board.
 * Row 0 is at the top, column 0 is at the left.
 */
data class Position(
    val row: Int,
    val column: Int
) {
    operator fun plus(other: Position): Position =
        Position(row + other.row, column + other.column)

    operator fun minus(other: Position): Position =
        Position(row - other.row, column - other.column)

    companion object {
        val ORIGIN = Position(0, 0)
    }
}
