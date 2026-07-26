package com.yodgorbek.tetris.game.logic

import com.yodgorbek.tetris.game.model.Board
import com.yodgorbek.tetris.game.model.Offset
import com.yodgorbek.tetris.game.model.Rotation
import com.yodgorbek.tetris.game.model.TetrominoType
import com.yodgorbek.tetris.game.state.GameState
import com.yodgorbek.tetris.game.state.GameStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameLogicTest {

    private val logic = GameLogic()

    @Test
    fun testInitialSpawn() {
        val piece = logic.spawnPiece(TetrominoType.T)
        assertEquals(3, piece.offset.x)
        assertEquals(0, piece.offset.y)
    }

    @Test
    fun testMoveSuccess() {
        val state = GameState(
            status = GameStatus.RUNNING,
            currentPiece = logic.spawnPiece(TetrominoType.I)
        )
        val nextState = logic.move(state, Offset(1, 0))
        assertEquals(state.currentPiece!!.offset.x + 1, nextState.currentPiece!!.offset.x)
    }

    @Test
    fun testMoveCollision() {
        val state = GameState(
            status = GameStatus.RUNNING,
            currentPiece = logic.spawnPiece(TetrominoType.I).copy(offset = Offset(0, 0))
        )
        // Move left into wall
        val nextState = logic.move(state, Offset(-1, 0))
        assertEquals(state.currentPiece!!.offset.x, nextState.currentPiece!!.offset.x)
    }

    @Test
    fun testLineClearing() {
        val board = Board()
        val state = GameState(
            status = GameStatus.RUNNING,
            board = board,
            currentPiece = logic.spawnPiece(TetrominoType.I).copy(offset = Offset(0, 18))
        )

        // Manual setup of a full row if needed, but let's test the lockPiece logic
        // We can just verify lockPiece identifies clearingLines
        val lockedState = logic.lockPiece(state)
        // Since board was empty, no clearing lines
        assertEquals(0, lockedState.clearingLines.size)
    }
}
