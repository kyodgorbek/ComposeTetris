package com.yodgorbek.tetris.game.model

data class Board(
    val rows: Int = 20,
    val columns: Int = 10,
    val grid: List<List<Cell>> = List(rows) { List(columns) { Cell.Empty } }
) {
    fun getCell(x: Int, y: Int): Cell? {
        if (y !in 0 until rows || x !in 0 until columns) return null
        return grid[y][x]
    }

    fun isOccupied(x: Int, y: Int): Boolean {
        return getCell(x, y)?.occupied ?: true // Out of bounds is considered occupied
    }
}
