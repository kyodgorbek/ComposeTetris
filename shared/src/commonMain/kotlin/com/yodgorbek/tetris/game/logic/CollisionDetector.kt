package com.yodgorbek.tetris.game.logic

import com.yodgorbek.tetris.game.model.Board
import com.yodgorbek.tetris.game.model.Position
import com.yodgorbek.tetris.game.model.Tetromino

/**
 * Handles collision detection for tetromino pieces.
 */
class CollisionDetector {
    /**
     * Check if a piece at position collides with the board.
     */
    fun hasCollision(board: Board, piece: Tetromino, x: Int, y: Int): Boolean {
        return piece.getBlocks().any { block ->
            val posX = x + block.column
            val posY = y + block.row

            // Out of bounds on sides
            if (posX < 0 || posX >= Board.COLUMNS) return@any true

            // Below board is collision only if occupied
            if (posY >= Board.ROWS) return@any true

            // Above board is OK
            if (posY < 0) return@any false

            // Check board occupancy
            board.isOccupied(Position(posY, posX))
        }
    }

    /**
     * Check if piece is fully above the board.
     */
    fun isAboveBoard(piece: Tetromino, y: Int): Boolean {
        return piece.getBlocks().all { block ->
            y + block.row < 0
        }
    }

    /**
     * Find the lowest valid Y position for a piece (for ghost piece).
     */
    fun findLowestY(board: Board, piece: Tetromino, x: Int, startY: Int): Int {
        var y = startY
        while (!hasCollision(board, piece, x, y + 1)) {
            y++
        }
        return y
    }
}
