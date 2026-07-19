package com.yodgorbek.tetris.game.logic

import com.yodgorbek.tetris.game.model.Board
import com.yodgorbek.tetris.game.model.Cell
import com.yodgorbek.tetris.game.model.Position
import com.yodgorbek.tetris.game.model.Tetromino
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollisionDetectorTest {
    private val detector = CollisionDetector()

    @Test
    fun testNoCollisionEmptyBoard() {
        val board = Board()
        val piece = Tetromino.I()
        
        assertFalse(detector.hasCollision(board, piece, 3, 0))
    }

    @Test
    fun testCollisionBottomBoundary() {
        val board = Board()
        val piece = Tetromino.I()
        
        // I piece at bottom should collide
        assertTrue(detector.hasCollision(board, piece, 3, Board.ROWS - 1))
    }

    @Test
    fun testCollisionLeftBoundary() {
        val board = Board()
        val piece = Tetromino.I()
        
        // I piece going off left edge
        assertTrue(detector.hasCollision(board, piece, -1, 10))
    }

    @Test
    fun testCollisionRightBoundary() {
        val board = Board()
        val piece = Tetromino.I()
        
        // I piece going off right edge
        assertTrue(detector.hasCollision(board, piece, Board.COLUMNS, 10))
    }

    @Test
    fun testNoCollisionAboveBoard() {
        val board = Board()
        val piece = Tetromino.I()
        
        assertFalse(detector.hasCollision(board, piece, 3, -4))
    }

    @Test
    fun testCollisionWithOccupiedCell() {
        var board = Board()
        // Place a block at position (10, 5)
        val occupiedCell = Cell(occupied = true)
        board = board.setCell(Position(10, 5), occupiedCell)
        
        val piece = Tetromino.O() // 2x2 square
        
        // Should collide if trying to place at (4, 10) since O piece occupies (10,4) and (10,5)
        assertTrue(detector.hasCollision(board, piece, 4, 10))
    }

    @Test
    fun testIsAboveBoard() {
        val piece = Tetromino.I()
        
        assertTrue(detector.isAboveBoard(piece, -4))
        assertFalse(detector.isAboveBoard(piece, 0))
    }

    @Test
    fun testFindLowestY() {
        val board = Board()
        val piece = Tetromino.I()
        
        val lowestY = detector.findLowestY(board, piece, 3, 0)
        assertEquals(Board.ROWS - 1, lowestY)
    }
}

class LineClearerTest {
    private val clearer = LineClearer()

    @Test
    fun testFindLinesToClear() {
        val rows = listOf(3, 1, 5)
        val result = clearer.findLinesToClear(rows)
        
        assertEquals(listOf(1, 3, 5), result)
    }

    @Test
    fun testUpdateCombo() {
        var combo = 0
        combo = clearer.updateCombo(1, combo)
        assertEquals(1, combo)
        
        combo = clearer.updateCombo(2, combo)
        assertEquals(2, combo)
        
        combo = clearer.updateCombo(0, combo)
        assertEquals(0, combo)
    }
}

class ScorerTest {
    private val scorer = Scorer()

    @Test
    fun testScoreSingleLine() {
        val (score, combo) = scorer.calculateScore(1, 0, false)
        assertEquals(100L, score)
        assertEquals(1, combo)
    }

    @Test
    fun testScoreDoubleLine() {
        val (score, combo) = scorer.calculateScore(2, 0, false)
        assertEquals(300L, score)
        assertEquals(1, combo)
    }

    @Test
    fun testScoreTripleLine() {
        val (score, combo) = scorer.calculateScore(3, 0, false)
        assertEquals(700L, score)
        assertEquals(1, combo)
    }

    @Test
    fun testScoreTetris() {
        val (score, combo) = scorer.calculateScore(4, 0, false)
        assertEquals(1500L, score)
        assertEquals(1, combo)
    }

    @Test
    fun testScoreWithCombo() {
        val (score, combo) = scorer.calculateScore(1, 2, false)
        // 100 base + 50 (100 * 0.5 combo bonus)
        assertEquals(150L, score)
        assertEquals(3, combo)
    }

    @Test
    fun testIsBackToBackTetris() {
        assertTrue(scorer.isBackToBackTetris(4, true))
        assertTrue(scorer.isBackToBackTetris(4, false))
        
        assertFalse(scorer.isBackToBackTetris(3, true))
        assertFalse(scorer.isBackToBackTetris(0, true))
    }

    @Test
    fun testCalculateLevel() {
        assertEquals(1, scorer.calculateLevel(0))
        assertEquals(1, scorer.calculateLevel(9))
        assertEquals(2, scorer.calculateLevel(10))
        assertEquals(3, scorer.calculateLevel(20))
    }
}
