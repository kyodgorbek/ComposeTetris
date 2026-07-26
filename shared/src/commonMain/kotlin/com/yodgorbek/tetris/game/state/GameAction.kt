package com.yodgorbek.tetris.game.state

sealed class GameAction {
    data object Start : GameAction()
    data object Pause : GameAction()
    data object Resume : GameAction()
    data object Restart : GameAction()

    data object MoveLeft : GameAction()
    data object MoveRight : GameAction()
    data object MoveDown : GameAction()
    data object HardDrop : GameAction()

    data object RotateClockwise : GameAction()
    data object RotateCounterClockwise : GameAction()

    data object Hold : GameAction()
}
