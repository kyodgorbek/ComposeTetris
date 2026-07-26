package com.yodgorbek.tetris.game.model

data class Offset(val x: Int, val y: Int) {
    operator fun plus(other: Offset) = Offset(x + other.x, y + other.y)
    operator fun minus(other: Offset) = Offset(x - other.x, y - other.y)
}
