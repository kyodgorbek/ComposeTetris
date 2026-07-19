package com.yodgorbek.tetris.game.model

/**
 * Immutable representation of the Tetris game board.
 * The board is 20 rows by 10 columns.
 */
data class Board(
    val grid: List<List<Cell>> = List(ROWS) { List(COLUMNS) { Cell() } }
) {
    /**
     * Get a cell at the specified position.
     * Returns an empty Cell if the position is out of bounds.
     */
    fun getCell(position: Position): Cell {
        if (position.row !in 0 until ROWS || position.column !in 0 until COLUMNS) {
            return Cell()
        }
        return grid[position.row][position.column]
    }

    /**
     * Set a cell at the specified position.
     * Returns a new Board if the position is valid, otherwise returns this unchanged.
     */
    fun setCell(position: Position, cell: Cell): Board {
        if (position.row !in 0 until ROWS || position.column !in 0 until COLUMNS) {
            return this
        }
        val newGrid = grid.mapIndexed { rowIndex, row ->
            if (rowIndex == position.row) {
                row.mapIndexed { colIndex, col ->
                    if (colIndex == position.column) cell else col
                }
            } else {
                row
            }
        }
        return Board(newGrid)
    }

    /**
     * Set multiple cells at once.
     * Returns a new Board with all cells set.
     */
    fun setCells(cells: List<Pair<Position, Cell>>): Board {
        var result = this
        for ((position, cell) in cells) {
            result = result.setCell(position, cell)
        }
        return result
    }

    /**
     * Check if a row is completely filled.
     */
    fun isRowFilled(row: Int): Boolean {
        if (row !in 0 until ROWS) return false
        return grid[row].all { it.occupied }
    }

    /**
     * Get all filled rows.
     */
    fun getFilledRows(): List<Int> {
        return (0 until ROWS).filter { isRowFilled(it) }
    }

    /**
     * Clear specified rows and shift remaining rows down.
     */
    fun clearRows(rowsToDelete: List<Int>): Board {
        if (rowsToDelete.isEmpty()) return this

        val newGrid = grid.filterIndexed { index, _ ->
            index !in rowsToDelete
        }.toMutableList()

        // Add empty rows at the top
        repeat(rowsToDelete.size) {
            newGrid.add(0, List(COLUMNS) { Cell() })
        }

        return Board(newGrid)
    }

    /**
     * Check if a position is occupied.
     */
    fun isOccupied(position: Position): Boolean {
        if (position.row !in 0 until ROWS || position.column !in 0 until COLUMNS) {
            return true // Out of bounds is considered occupied
        }
        return grid[position.row][position.column].occupied
    }

    /**
     * Check if multiple positions are occupied.
     */
    fun areOccupied(positions: List<Position>): Boolean {
        return positions.any { isOccupied(it) }
    }

    /**
     * Check if the board is clear (no occupied cells).
     */
    fun isEmpty(): Boolean {
        return grid.all { row -> row.all { !it.occupied } }
    }

    companion object {
        const val ROWS = 20
        const val COLUMNS = 10
    }
}
