package com.yodgorbek.tetris.game.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import androidx.compose.ui.graphics.Color

class PositionTest {
    @Test
    fun testPositionCreation() {
        val pos = Position(5, 3)
        assertEquals(5, pos.row)
        assertEquals(3, pos.column)
    }

    @Test
    fun testPositionAddition() {
        val pos1 = Position(5, 3)
        val pos2 = Position(2, 1)
        val result = pos1 + pos2
        assertEquals(Position(7, 4), result)
    }

    @Test
    fun testPositionSubtraction() {
        val pos1 = Position(7, 4)
        val pos2 = Position(2, 1)
        val result = pos1 - pos2
        assertEquals(Position(5, 3), result)
    }

    @Test
    fun testOrigin() {
        assertEquals(Position(0, 0), Position.ORIGIN)
    }
}

class CellTest {
    @Test
    fun testEmptyCell() {
        val cell = Cell()
        assertFalse(cell.occupied)
        assertEquals(null, cell.tetrominoId)
    }

    @Test
    fun testOccupiedCell() {
        val cell = Cell(occupied = true, tetrominoId = 0)
        assertTrue(cell.occupied)
        assertEquals(0, cell.tetrominoId)
    }

    @Test
    fun testCellWithColor() {
        val cell = Cell(occupied = true, color = Color.Red)
        assertTrue(cell.occupied)
        assertEquals(Color.Red, cell.color)
    }
}

class BoardTest {
    @Test
    fun testBoardCreation() {
        val board = Board()
        assertEquals(Board.ROWS, board.grid.size)
        assertEquals(Board.COLUMNS, board.grid[0].size)
    }

    @Test
    fun testBoardInitiallyEmpty() {
        val board = Board()
        assertTrue(board.isEmpty())
    }

    @Test
    fun testGetCell() {
        val board = Board()
        val cell = board.getCell(Position(5, 5))
        assertFalse(cell.occupied)
    }

    @Test
    fun testGetCellOutOfBounds() {
        val board = Board()
        val cell = board.getCell(Position(-1, 5))
        assertFalse(cell.occupied)
    }

    @Test
    fun testSetCell() {
        val board = Board()
        val occupiedCell = Cell(occupied = true, tetrominoId = 0)
        val newBoard = board.setCell(Position(5, 5), occupiedCell)
        
        assertFalse(newBoard.isEmpty())
        assertTrue(newBoard.getCell(Position(5, 5)).occupied)
        assertEquals(0, newBoard.getCell(Position(5, 5)).tetrominoId)
    }

    @Test
    fun testSetCellOutOfBounds() {
        val board = Board()
        val occupiedCell = Cell(occupied = true)
        val newBoard = board.setCell(Position(-1, 5), occupiedCell)
        
        assertTrue(newBoard.isEmpty())
    }

    @Test
    fun testSetMultipleCells() {
        val board = Board()
        val cells = listOf(
            Position(5, 5) to Cell(occupied = true, tetrominoId = 0),
            Position(6, 5) to Cell(occupied = true, tetrominoId = 0)
        )
        val newBoard = board.setCells(cells)
        
        assertTrue(newBoard.getCell(Position(5, 5)).occupied)
        assertTrue(newBoard.getCell(Position(6, 5)).occupied)
    }

    @Test
    fun testIsRowFilled() {
        val board = Board()
        assertFalse(board.isRowFilled(0))
        
        val filledRow = (0 until Board.COLUMNS).map { col ->
            Position(0, col) to Cell(occupied = true)
        }
        val newBoard = board.setCells(filledRow)
        assertTrue(newBoard.isRowFilled(0))
    }

    @Test
    fun testGetFilledRows() {
        val board = Board()
        assertEquals(emptyList(), board.getFilledRows())
        
        // Fill first row
        val row0 = (0 until Board.COLUMNS).map { col ->
            Position(0, col) to Cell(occupied = true)
        }
        // Fill third row
        val row2 = (0 until Board.COLUMNS).map { col ->
            Position(2, col) to Cell(occupied = true)
        }
        
        val newBoard = board.setCells(row0 + row2)
        assertEquals(listOf(0, 2), newBoard.getFilledRows())
    }

    @Test
    fun testClearRows() {
        val board = Board()
        
        // Fill rows 0, 1, 2
        val filledCells = (0..2).flatMap { row ->
            (0 until Board.COLUMNS).map { col ->
                Position(row, col) to Cell(occupied = true, tetrominoId = 0)
            }
        }
        val filledBoard = board.setCells(filledCells)
        
        // Clear rows 1 and 2
        val clearedBoard = filledBoard.clearRows(listOf(1, 2))
        
        // Check that row 0 is still filled
        assertTrue(clearedBoard.isRowFilled(0))
        // Check that new empty rows were added at top
        assertFalse(clearedBoard.isRowFilled(1))
        assertFalse(clearedBoard.isRowFilled(2))
    }

    @Test
    fun testIsOccupied() {
        val board = Board()
        assertFalse(board.isOccupied(Position(5, 5)))
        
        val occupiedCell = Cell(occupied = true)
        val newBoard = board.setCell(Position(5, 5), occupiedCell)
        assertTrue(newBoard.isOccupied(Position(5, 5)))
    }

    @Test
    fun testIsOccupiedOutOfBounds() {
        val board = Board()
        // Out of bounds positions are considered occupied
        assertTrue(board.isOccupied(Position(-1, 5)))
        assertTrue(board.isOccupied(Position(25, 5)))
        assertTrue(board.isOccupied(Position(5, -1)))
        assertTrue(board.isOccupied(Position(5, 15)))
    }

    @Test
    fun testAreOccupied() {
        val board = Board()
        val occupiedCell = Cell(occupied = true)
        val newBoard = board.setCell(Position(5, 5), occupiedCell)
        
        val positions = listOf(Position(5, 5), Position(6, 6))
        assertTrue(newBoard.areOccupied(positions))
    }
}

class RandomizerTest {
    @Test
    fun testRandomizerCreation() {
        val randomizer = Randomizer()
        val piece = randomizer.peek()
        assertTrue(piece is Tetromino)
    }

    @Test
    fun testNextPiece() {
        val randomizer = Randomizer()
        val piece1 = randomizer.next()
        val piece2 = randomizer.next()
        
        assertTrue(piece1 is Tetromino)
        assertTrue(piece2 is Tetromino)
    }

    @Test
    fun testPeekDoesNotConsume() {
        val randomizer = Randomizer()
        val peeked = randomizer.peek()
        val next = randomizer.next()
        
        assertEquals(peeked, next)
    }

    @Test
    fun testSevenBagRandomness() {
        val randomizer = Randomizer()
        val pieces = mutableListOf<Tetromino>()
        
        // Get 7 pieces (one complete bag)
        repeat(7) {
            pieces.add(randomizer.next())
        }
        
        // Each type should appear exactly once
        val iCount = pieces.count { it is Tetromino.I }
        val oCount = pieces.count { it is Tetromino.O }
        val tCount = pieces.count { it is Tetromino.T }
        val sCount = pieces.count { it is Tetromino.S }
        val zCount = pieces.count { it is Tetromino.Z }
        val jCount = pieces.count { it is Tetromino.J }
        val lCount = pieces.count { it is Tetromino.L }
        
        assertEquals(1, iCount)
        assertEquals(1, oCount)
        assertEquals(1, tCount)
        assertEquals(1, sCount)
        assertEquals(1, zCount)
        assertEquals(1, jCount)
        assertEquals(1, lCount)
    }

    @Test
    fun testPeekNext() {
        val randomizer = Randomizer()
        val nextPieces = randomizer.peekNext(10)
        
        assertEquals(10, nextPieces.size)
        nextPieces.forEach { assertTrue(it is Tetromino) }
    }
}

class TetrominoTest {
    @Test
    fun testIPieceRotation() {
        val i = Tetromino.I()
        val rotated = i.rotate(true)
        
        assertTrue(rotated is Tetromino.I)
        assertEquals(1, rotated.rotation)
    }

    @Test
    fun testOPieceDoesNotRotate() {
        val o = Tetromino.O()
        val rotated = o.rotate(true)
        
        assertTrue(rotated is Tetromino.O)
        assertEquals(0, rotated.rotation)
    }

    @Test
    fun testTPieceRotation() {
        val t = Tetromino.T()
        val rotated = t.rotate(true)
        
        assertTrue(rotated is Tetromino.T)
        assertEquals(1, rotated.rotation)
    }

    @Test
    fun testPieceColors() {
        assertEquals(Color(0xFF00F0F0), Tetromino.I().color)
        assertEquals(Color(0xFFF0F000), Tetromino.O().color)
        assertEquals(Color(0xFFF000F0), Tetromino.T().color)
        assertEquals(Color(0xFF00F000), Tetromino.S().color)
        assertEquals(Color(0xFFF00000), Tetromino.Z().color)
        assertEquals(Color(0xFF0000F0), Tetromino.J().color)
        assertEquals(Color(0xFFF0A000), Tetromino.L().color)
    }

    @Test
    fun testGetBlocks() {
        val i = Tetromino.I()
        val blocks = i.getBlocks()
        
        assertEquals(4, blocks.size)
        assertTrue(blocks.contains(Position(0, 0)))
        assertTrue(blocks.contains(Position(0, 1)))
        assertTrue(blocks.contains(Position(0, 2)))
        assertTrue(blocks.contains(Position(0, 3)))
    }
}
