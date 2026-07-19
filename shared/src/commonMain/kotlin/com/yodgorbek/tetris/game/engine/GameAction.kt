package com.yodgorbek.tetris.game.engine

/**
 * Sealed class representing all possible user actions and game events.
 * These actions are processed by the GameEngine to update the GameState.
 */
sealed class GameAction {
    // Control actions
    data object MoveLeft : GameAction()
    data object MoveRight : GameAction()
    data object RotateClockwise : GameAction()
    data object RotateCounterClockwise : GameAction()
    data object SoftDrop : GameAction()
    data object HardDrop : GameAction()
    data object Hold : GameAction()
    
    // Game control
    data object Pause : GameAction()
    data object Resume : GameAction()
    data object Restart : GameAction()
    
    // Internal actions
    data object Tick : GameAction()
    data object LockPiece : GameAction()
    data object SpawnNewPiece : GameAction()
    data object ClearLines : GameAction()
    
    override fun toString(): String = this::class.simpleName ?: "GameAction"
}
