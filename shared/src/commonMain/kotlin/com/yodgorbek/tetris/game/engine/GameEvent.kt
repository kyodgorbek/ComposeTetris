package com.yodgorbek.tetris.game.engine

/**
 * Sealed class representing significant game events that occur during gameplay.
 * Events are emitted by the GameEngine and can be observed by the UI.
 */
sealed class GameEvent {
    // Piece events
    data class PieceSpawned(val x: Int, val y: Int) : GameEvent()
    data class PieceMoved(val x: Int, val y: Int) : GameEvent()
    data class PieceRotated(val clockwise: Boolean) : GameEvent()
    data class PieceLocked(val x: Int, val y: Int) : GameEvent()
    
    // Hold events
    data class PieceHeld(val heldPiece: String) : GameEvent()
    
    // Drop events
    data object SoftDropped : GameEvent()
    data object HardDropped : GameEvent()
    
    // Line clear events
    data class LinesCleared(val count: Int, val rows: List<Int>) : GameEvent()
    data object LineClearAnimation : GameEvent()
    
    // Scoring events
    data class ScoreUpdated(val score: Long, val lines: Int) : GameEvent()
    data class LevelUp(val newLevel: Int) : GameEvent()
    
    // Game state events
    data object GameStarted : GameEvent()
    data object GamePaused : GameEvent()
    data object GameResumed : GameEvent()
    data object GameOver : GameEvent()
    data object GameRestarted : GameEvent()
    
    // Ghost piece events
    data class GhostPieceUpdated(val x: Int, val y: Int) : GameEvent()
    
    // Error events
    data class RotationFailed(val reason: String) : GameEvent()
    data class MovementFailed(val direction: String) : GameEvent()
    
    override fun toString(): String = this::class.simpleName ?: "GameEvent"
}
