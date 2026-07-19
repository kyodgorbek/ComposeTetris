package com.yodgorbek.tetris.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yodgorbek.tetris.game.engine.GameAction
import com.yodgorbek.tetris.game.engine.GameEngine
import com.yodgorbek.tetris.game.engine.GameEvent
import com.yodgorbek.tetris.game.engine.GameState
import com.yodgorbek.tetris.game.model.Randomizer
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel that connects the game engine to the UI layer.
 * Handles game lifecycle and exposes game state/events to Composables.
 */
class GameViewModel : ViewModel() {
    private val gameEngine = GameEngine(viewModelScope, Randomizer())

    val gameState: StateFlow<GameState> = gameEngine.state
    val gameEvents: SharedFlow<GameEvent> = gameEngine.events

    /**
     * Start a new game.
     */
    fun startGame() {
        gameEngine.startGame()
    }

    /**
     * Send an action to the game engine.
     */
    fun sendAction(action: GameAction) {
        gameEngine.handleAction(action)
    }

    /**
     * Perform move left action.
     */
    fun moveLeft() {
        sendAction(GameAction.MoveLeft)
    }

    /**
     * Perform move right action.
     */
    fun moveRight() {
        sendAction(GameAction.MoveRight)
    }

    /**
     * Perform rotate clockwise action.
     */
    fun rotateClockwise() {
        sendAction(GameAction.RotateClockwise)
    }

    /**
     * Perform rotate counter-clockwise action.
     */
    fun rotateCounterClockwise() {
        sendAction(GameAction.RotateCounterClockwise)
    }

    /**
     * Perform soft drop action.
     */
    fun softDrop() {
        sendAction(GameAction.SoftDrop)
    }

    /**
     * Perform hard drop action.
     */
    fun hardDrop() {
        sendAction(GameAction.HardDrop)
    }

    /**
     * Perform hold action.
     */
    fun hold() {
        sendAction(GameAction.Hold)
    }

    /**
     * Pause the game.
     */
    fun pause() {
        sendAction(GameAction.Pause)
    }

    /**
     * Resume the game.
     */
    fun resume() {
        sendAction(GameAction.Resume)
    }

    /**
     * Restart the game.
     */
    fun restart() {
        sendAction(GameAction.Restart)
    }

    /**
     * Cleanup resources when ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        gameEngine.dispose()
    }
}
