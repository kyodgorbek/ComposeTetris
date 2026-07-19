package com.yodgorbek.tetris.game.logic

/**
 * Handles line clearing logic for completed rows.
 */
class LineClearer {
    /**
     * Find all rows that are completely filled.
     * This is used to identify which rows should be cleared.
     * (Actual board clearing is handled by Board.clearRows)
     */
    fun findLinesToClear(filledRows: List<Int>): List<Int> {
        return filledRows.sorted()
    }

    /**
     * Calculate combo count based on consecutive line clears.
     */
    fun updateCombo(clearedCount: Int, previousCombo: Int): Int {
        return if (clearedCount > 0) {
            previousCombo + 1
        } else {
            0
        }
    }
}
