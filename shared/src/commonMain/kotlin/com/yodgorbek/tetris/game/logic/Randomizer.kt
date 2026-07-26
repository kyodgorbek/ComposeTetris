package com.yodgorbek.tetris.game.logic

import com.yodgorbek.tetris.game.model.TetrominoType

class Randomizer {
    private var bag = mutableListOf<TetrominoType>()

    fun next(): TetrominoType {
        if (bag.isEmpty()) {
            bag.addAll(TetrominoType.entries)
            bag.shuffle()
        }
        return bag.removeAt(0)
    }
}
