package com.yodgorbek.tetris.game.logic

import com.yodgorbek.tetris.game.model.*
import com.yodgorbek.tetris.game.state.GameState
import com.yodgorbek.tetris.game.state.GameStatus

class GameLogic {

    fun move(state: GameState, direction: Offset): GameState {
        if (state.status != GameStatus.RUNNING || state.currentPiece == null || state.clearingLines.isNotEmpty()) return state

        val nextOffset = state.currentPiece.offset + direction
        if (checkCollision(state.board, state.currentPiece.type, state.currentPiece.rotation, nextOffset)) {
            return state
        }

        return state.copy(currentPiece = state.currentPiece.copy(offset = nextOffset))
    }

    fun rotate(state: GameState, clockwise: Boolean): GameState {
        if (state.status != GameStatus.RUNNING || state.currentPiece == null || state.clearingLines.isNotEmpty()) return state

        val currentPiece = state.currentPiece
        val nextRotation = if (clockwise) currentPiece.rotation.rotateClockwise()
                           else currentPiece.rotation.rotateCounterClockwise()

        val kicks = RotationLogic.getKicks(currentPiece.type, currentPiece.rotation, nextRotation)

        for (kick in kicks) {
            val nextOffset = currentPiece.offset + kick
            if (!checkCollision(state.board, currentPiece.type, nextRotation, nextOffset)) {
                return state.copy(currentPiece = currentPiece.copy(rotation = nextRotation, offset = nextOffset))
            }
        }

        return state
    }

    fun checkCollision(
        board: Board,
        type: TetrominoType,
        rotation: Rotation,
        offset: Offset
    ): Boolean {
        val shape = Tetromino.getShape(type, rotation)
        return shape.any { block ->
            val x = block.x + offset.x
            val y = block.y + offset.y

            x < 0 || x >= board.columns || y >= board.rows || (y >= 0 && board.isOccupied(x, y))
        }
    }

    fun getGhostOffset(state: GameState): Offset? {
        val piece = state.currentPiece ?: return null
        var ghostOffset = piece.offset
        while (!checkCollision(state.board, piece.type, piece.rotation, ghostOffset + Offset(0, 1))) {
            ghostOffset += Offset(0, 1)
        }
        return ghostOffset
    }

    fun spawnPiece(type: TetrominoType): Tetromino {
        return Tetromino(type = type, offset = Offset(3, 0))
    }

    /**
     * Places the current piece onto the board grid without clearing lines yet.
     */
    fun lockPiece(state: GameState): GameState {
        val piece = state.currentPiece ?: return state
        val shape = piece.shape

        val newGrid = state.board.grid.map { it.toMutableList() }.toMutableList()
        shape.forEach { block ->
            val x = block.x + piece.offset.x
            val y = block.y + piece.offset.y
            if (y in 0 until state.board.rows && x in 0 until state.board.columns) {
                newGrid[y][x] = Cell(occupied = true, color = getPieceColor(piece.type))
            }
        }

        val filledLines = mutableListOf<Int>()
        newGrid.forEachIndexed { index, row ->
            if (row.all { it.occupied }) {
                filledLines.add(index)
            }
        }

        return state.copy(
            board = state.board.copy(grid = newGrid),
            clearingLines = filledLines,
            currentPiece = null,
            canHold = true
        )
    }

    /**
     * Finalizes the clearing of lines, updating score and levels.
     */
    fun finalizeLineClear(state: GameState): GameState {
        val linesToClear = state.clearingLines
        if (linesToClear.isEmpty()) return state

        val newGrid = state.board.grid.filterIndexed { index, _ ->
            index !in linesToClear
        }.toMutableList()

        repeat(linesToClear.size) {
            newGrid.add(0, List(state.board.columns) { Cell.Empty })
        }

        val linesCleared = linesToClear.size
        val nextScore = state.score + calculateScore(linesCleared, state.level)
        val nextLines = state.lines + linesCleared
        val nextLevel = (nextLines / 10) + 1

        return state.copy(
            board = state.board.copy(grid = newGrid),
            score = nextScore,
            lines = nextLines,
            level = nextLevel,
            clearingLines = emptyList()
        )
    }

    fun getPieceColor(type: TetrominoType): Long = when (type) {
        TetrominoType.I -> 0xFF00FFFF // Cyan
        TetrominoType.O -> 0xFFFFFF00 // Yellow
        TetrominoType.T -> 0xFF800080 // Purple
        TetrominoType.S -> 0xFF00FF00 // Green
        TetrominoType.Z -> 0xFFFF0000 // Red
        TetrominoType.J -> 0xFF0000FF // Blue
        TetrominoType.L -> 0xFFFFA500 // Orange
    }

    private fun calculateScore(lines: Int, level: Int): Int = when (lines) {
        1 -> 100 * level
        2 -> 300 * level
        3 -> 500 * level
        4 -> 800 * level
        else -> 0
    }
}
