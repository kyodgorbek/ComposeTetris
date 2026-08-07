package com.yodgorbek.tetris.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yodgorbek.tetris.game.engine.GameEngine
import com.yodgorbek.tetris.game.logic.GameLogic
import com.yodgorbek.tetris.game.logic.Randomizer
import com.yodgorbek.tetris.game.state.GameAction
import com.yodgorbek.tetris.game.state.GameState
import com.yodgorbek.tetris.game.state.GameStatus
import com.yodgorbek.tetris.util.AudioManager
import com.yodgorbek.tetris.util.PreferenceManager
import kotlinx.coroutines.flow.*

class GameViewModel(
    private val preferenceManager: PreferenceManager,
    private val audioManager: AudioManager,
    private val logic: GameLogic,
    private val randomizer: Randomizer
) : ViewModel() {

    private val engine = GameEngine(
        logic = logic,
        randomizer = randomizer,
        audioManager = audioManager,
        scope = viewModelScope
    )

    val state: StateFlow<GameState> = engine.state

    private val _highScores = MutableStateFlow(preferenceManager.getHighScores())
    val highScores = _highScores.asStateFlow()

    init {
        // Observe game status to save high scores
        state.map { it.status }
            .distinctUntilChanged()
            .onEach { status ->
                if (status == GameStatus.GAME_OVER) {
                    preferenceManager.saveHighScore(state.value.score)
                    _highScores.value = preferenceManager.getHighScores()
                }
            }
            .launchIn(viewModelScope)
    }

    fun dispatch(action: GameAction) {
        engine.dispatch(action)
    }

    fun toggleSound() {
        preferenceManager.isSoundEnabled = !preferenceManager.isSoundEnabled
    }

    fun isSoundEnabled() = preferenceManager.isSoundEnabled

    override fun onCleared() {
        super.onCleared()
        audioManager.release()
    }
}
