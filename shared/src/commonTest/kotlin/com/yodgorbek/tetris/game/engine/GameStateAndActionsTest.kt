package com.yodgorbek.tetris.game.engine

import com.yodgorbek.tetris.game.model.Board
import com.yodgorbek.tetris.game.model.Position
import com.yodgorbek.tetris.game.model.Tetromino
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GameStateTest {
    @Test
    fun testGameStateInitialState() {
        val state = GameState()
        assertEquals(GameStatus.IDLE, state.gameStatus)
        assertTrue(state.board.isEmpty())
        assertNull(state.currentPiece)
        assertEquals(0L, state.score)
        assertEquals(0, state.lines)
        assertEquals(1, state.level)
    }

    @Test
    fun testGameStateIsActive() {
        val state = GameState(gameStatus = GameStatus.PLAYING, isPaused = false)
        assertTrue(state.isActive())
    }

    @Test
    fun testGameStateIsActivePaused() {
        val state = GameState(gameStatus = GameStatus.PLAYING, isPaused = true)
        assertFalse(state.isActive())
    }

    @Test
    fun testGameStateIsGameOver() {
        val state = GameState(gameStatus = GameStatus.GAME_OVER)
        assertTrue(state.isGameOver())
    }

    @Test
    fun testGameStateInitial() {
        val piece = Tetromino.I()
        val queue = listOf(Tetromino.O(), Tetromino.T())
        val state = GameState.initial(piece, queue)
        
        assertEquals(piece, state.currentPiece)
        assertEquals(queue, state.nextPieces)
        assertEquals(GameStatus.PLAYING, state.gameStatus)
    }
}

class GameActionTest {
    @Test
    fun testGameActionNames() {
        assertEquals("MoveLeft", GameAction.MoveLeft.toString())
        assertEquals("MoveRight", GameAction.MoveRight.toString())
        assertEquals("RotateClockwise", GameAction.RotateClockwise.toString())
        assertEquals("Pause", GameAction.Pause.toString())
    }
}

class GameEventTest {
    @Test
    fun testGameEventNames() {
        assertEquals("GameStarted", GameEvent.GameStarted.toString())
        assertEquals("GamePaused", GameEvent.GamePaused.toString())
        assertEquals("GameOver", GameEvent.GameOver.toString())
    }

    @Test
    fun testGameEventWithData() {
        val event = GameEvent.PieceSpawned(3, 0)
        assertTrue(event is GameEvent.PieceSpawned)
        assertEquals("PieceSpawned", event.toString())
    }
}
