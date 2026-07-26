package com.yodgorbek.tetris.game.engine

import com.yodgorbek.tetris.game.logic.GameLogic
import com.yodgorbek.tetris.game.logic.Randomizer
import com.yodgorbek.tetris.game.state.GameAction
import com.yodgorbek.tetris.game.state.GameStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class GameEngineTest {

    @Test
    fun testStartGame() = runTest {
        val engine = GameEngine(
            logic = GameLogic(),
            randomizer = Randomizer(),
            scope = backgroundScope
        )

        assertEquals(GameStatus.IDLE, engine.state.value.status)

        engine.dispatch(GameAction.Start)

        assertEquals(GameStatus.RUNNING, engine.state.value.status)
        assertNotNull(engine.state.value.currentPiece)
    }

    @Test
    fun testPauseResume() = runTest {
        val engine = GameEngine(scope = backgroundScope)
        engine.dispatch(GameAction.Start)

        engine.dispatch(GameAction.Pause)
        assertEquals(GameStatus.PAUSED, engine.state.value.status)

        engine.dispatch(GameAction.Resume)
        assertEquals(GameStatus.RUNNING, engine.state.value.status)
    }
}
