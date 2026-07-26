package com.yodgorbek.tetris.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yodgorbek.tetris.game.engine.GameEngine
import com.yodgorbek.tetris.game.logic.GameLogic
import com.yodgorbek.tetris.game.logic.Randomizer
import com.yodgorbek.tetris.game.state.GameAction
import com.yodgorbek.tetris.game.state.GameState
import kotlinx.coroutines.flow.StateFlow

class GameViewModel : ViewModel() {

    private val engine = GameEngine(
        logic = GameLogic(),
        randomizer = Randomizer(),
        scope = viewModelScope
    )

    val state: StateFlow<GameState> = engine.state

    fun dispatch(action: GameAction) {
        engine.dispatch(action)
    }
}
