package com.yodgorbek.tetris.game.model

/**
 * Implements the official Tetris 7-Bag randomizer.
 * This ensures each tetromino appears exactly once before the bag is refilled,
 * providing fair randomization without consecutive duplicates.
 */
class Randomizer(
    private val bag: MutableList<Tetromino> = mutableListOf()
) {
    init {
        if (bag.isEmpty()) {
            refillBag()
        }
    }

    /**
     * Get the next piece from the bag.
     * Refills the bag when empty.
     */
    fun next(): Tetromino {
        if (bag.isEmpty()) {
            refillBag()
        }
        return bag.removeAt(0)
    }

    /**
     * Peek at the next piece without consuming it.
     */
    fun peek(): Tetromino {
        if (bag.isEmpty()) {
            refillBag()
        }
        return bag[0]
    }

    /**
     * Peek at multiple upcoming pieces.
     */
    fun peekNext(count: Int): List<Tetromino> {
        val result = mutableListOf<Tetromino>()
        var bagCopy = bag.toMutableList()

        while (result.size < count) {
            if (bagCopy.isEmpty()) {
                bagCopy = createBag()
            }
            result.add(bagCopy.removeAt(0))
        }
        return result
    }

    /**
     * Refill the bag with all 7 pieces in random order.
     */
    private fun refillBag() {
        bag.addAll(createBag())
    }

    /**
     * Create a new shuffled bag of all 7 tetromino pieces.
     */
    private fun createBag(): MutableList<Tetromino> {
        return mutableListOf(
            Tetromino.I(),
            Tetromino.O(),
            Tetromino.T(),
            Tetromino.S(),
            Tetromino.Z(),
            Tetromino.J(),
            Tetromino.L()
        ).apply { shuffle() }
    }
}
