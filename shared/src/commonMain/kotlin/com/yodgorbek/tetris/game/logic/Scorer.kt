package com.yodgorbek.tetris.game.logic

/**
 * Handles all scoring calculations for the Tetris game.
 * 
 * Scoring rules (reference repository):
 * - 1 line: 100 points
 * - 2 lines: 300 points
 * - 3 lines: 700 points
 * - 4 lines (Tetris): 1500 points
 * 
 * Combos and bonuses:
 * - Consecutive clears increase combo multiplier
 * - Back-to-back tetris bonus
 * - Perfect clear bonus
 */
class Scorer {
    /**
     * Calculate score gained from clearing lines.
     * Returns Pair of (scoreGain, newComboCount)
     */
    fun calculateScore(
        clearedLineCount: Int,
        currentCombo: Int,
        backToBackTetris: Boolean
    ): Pair<Long, Int> {
        if (clearedLineCount == 0) {
            return 0L to 0
        }

        // Base score for lines cleared
        val baseScore = when (clearedLineCount) {
            1 -> 100L
            2 -> 300L
            3 -> 700L
            4 -> 1500L // Tetris!
            else -> 0L // Invalid, but defensive
        }

        // Combo multiplier (starts at 1x for first clear, increases by 0.25x per combo)
        val comboMultiplier = 1.0 + (currentCombo * 0.25)
        val comboBonus = (baseScore * (comboMultiplier - 1.0)).toLong()

        // Back-to-back tetris bonus
        val backToBackBonus = if (clearedLineCount == 4 && backToBackTetris) {
            (baseScore * 0.5).toLong()
        } else {
            0L
        }

        val totalScore = baseScore + comboBonus + backToBackBonus
        val newCombo = currentCombo + 1

        return totalScore to newCombo
    }

    /**
     * Check if this is back-to-back tetris.
     */
    fun isBackToBackTetris(clearedLineCount: Int, previousBackToBack: Boolean): Boolean {
        return if (clearedLineCount == 4) {
            true
        } else if (clearedLineCount == 0) {
            false
        } else {
            previousBackToBack
        }
    }

    /**
     * Check for perfect clear (all lines cleared from board).
     */
    fun isPerfectClear(filledLinesCount: Int, totalLinesOnBoard: Int): Boolean {
        return filledLinesCount > 0 && filledLinesCount == totalLinesOnBoard
    }

    /**
     * Calculate level from total lines cleared.
     * Level increases every 10 lines.
     */
    fun calculateLevel(totalLinesCleared: Int): Int {
        return (totalLinesCleared / 10) + 1
    }
}
