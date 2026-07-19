package com.yodgorbek.tetris.game.model

import androidx.compose.ui.graphics.Color

/**
 * Represents a Tetromino piece type with rotation states and color.
 * Implements all 7 official Tetris pieces (I, O, T, S, Z, J, L).
 */
sealed class Tetromino(
    val id: Int,
    val color: Color,
    val rotations: List<List<Position>>
) {
    abstract fun rotate(clockwise: Boolean): Tetromino

    val currentRotation: Int = 0

    data class I(val rotation: Int = 0) : Tetromino(
        id = 0,
        color = Color(0xFF00F0F0), // Cyan
        rotations = listOf(
            // Horizontal
            listOf(
                Position(0, 0), Position(0, 1), Position(0, 2), Position(0, 3)
            ),
            // Vertical
            listOf(
                Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0)
            ),
            // Horizontal (same as rotation 0)
            listOf(
                Position(0, 0), Position(0, 1), Position(0, 2), Position(0, 3)
            ),
            // Vertical (same as rotation 1)
            listOf(
                Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0)
            )
        )
    ) {
        override fun rotate(clockwise: Boolean): Tetromino {
            val newRotation = if (clockwise) {
                (rotation + 1) % 4
            } else {
                (rotation - 1 + 4) % 4
            }
            return I(newRotation)
        }
    }

    data class O(val rotation: Int = 0) : Tetromino(
        id = 1,
        color = Color(0xFFF0F000), // Yellow
        rotations = listOf(
            // All rotations are the same for O piece
            listOf(
                Position(0, 0), Position(0, 1),
                Position(1, 0), Position(1, 1)
            ),
            listOf(
                Position(0, 0), Position(0, 1),
                Position(1, 0), Position(1, 1)
            ),
            listOf(
                Position(0, 0), Position(0, 1),
                Position(1, 0), Position(1, 1)
            ),
            listOf(
                Position(0, 0), Position(0, 1),
                Position(1, 0), Position(1, 1)
            )
        )
    ) {
        override fun rotate(clockwise: Boolean): Tetromino {
            return O(0) // O piece doesn't rotate
        }
    }

    data class T(val rotation: Int = 0) : Tetromino(
        id = 2,
        color = Color(0xFFF000F0), // Magenta
        rotations = listOf(
            // Up
            listOf(
                Position(0, 1),
                Position(1, 0), Position(1, 1), Position(1, 2)
            ),
            // Right
            listOf(
                Position(0, 1),
                Position(1, 1),
                Position(2, 0), Position(2, 1)
            ),
            // Down
            listOf(
                Position(1, 0), Position(1, 1), Position(1, 2),
                Position(2, 1)
            ),
            // Left
            listOf(
                Position(0, 0), Position(0, 1),
                Position(1, 1),
                Position(2, 1)
            )
        )
    ) {
        override fun rotate(clockwise: Boolean): Tetromino {
            val newRotation = if (clockwise) {
                (rotation + 1) % 4
            } else {
                (rotation - 1 + 4) % 4
            }
            return T(newRotation)
        }
    }

    data class S(val rotation: Int = 0) : Tetromino(
        id = 3,
        color = Color(0xFF00F000), // Green
        rotations = listOf(
            // Horizontal
            listOf(
                Position(0, 1), Position(0, 2),
                Position(1, 0), Position(1, 1)
            ),
            // Vertical
            listOf(
                Position(0, 0),
                Position(1, 0), Position(1, 1),
                Position(2, 1)
            ),
            // Horizontal
            listOf(
                Position(0, 1), Position(0, 2),
                Position(1, 0), Position(1, 1)
            ),
            // Vertical
            listOf(
                Position(0, 0),
                Position(1, 0), Position(1, 1),
                Position(2, 1)
            )
        )
    ) {
        override fun rotate(clockwise: Boolean): Tetromino {
            val newRotation = if (clockwise) {
                (rotation + 1) % 4
            } else {
                (rotation - 1 + 4) % 4
            }
            return S(newRotation)
        }
    }

    data class Z(val rotation: Int = 0) : Tetromino(
        id = 4,
        color = Color(0xFFF00000), // Red
        rotations = listOf(
            // Horizontal
            listOf(
                Position(0, 0), Position(0, 1),
                Position(1, 1), Position(1, 2)
            ),
            // Vertical
            listOf(
                Position(0, 1),
                Position(1, 0), Position(1, 1),
                Position(2, 0)
            ),
            // Horizontal
            listOf(
                Position(0, 0), Position(0, 1),
                Position(1, 1), Position(1, 2)
            ),
            // Vertical
            listOf(
                Position(0, 1),
                Position(1, 0), Position(1, 1),
                Position(2, 0)
            )
        )
    ) {
        override fun rotate(clockwise: Boolean): Tetromino {
            val newRotation = if (clockwise) {
                (rotation + 1) % 4
            } else {
                (rotation - 1 + 4) % 4
            }
            return Z(newRotation)
        }
    }

    data class J(val rotation: Int = 0) : Tetromino(
        id = 5,
        color = Color(0xFF0000F0), // Blue
        rotations = listOf(
            // Up
            listOf(
                Position(0, 0),
                Position(1, 0), Position(1, 1), Position(1, 2)
            ),
            // Right
            listOf(
                Position(0, 1), Position(0, 2),
                Position(1, 1),
                Position(2, 1)
            ),
            // Down
            listOf(
                Position(1, 0), Position(1, 1), Position(1, 2),
                Position(2, 2)
            ),
            // Left
            listOf(
                Position(0, 1),
                Position(1, 1),
                Position(2, 0), Position(2, 1)
            )
        )
    ) {
        override fun rotate(clockwise: Boolean): Tetromino {
            val newRotation = if (clockwise) {
                (rotation + 1) % 4
            } else {
                (rotation - 1 + 4) % 4
            }
            return J(newRotation)
        }
    }

    data class L(val rotation: Int = 0) : Tetromino(
        id = 6,
        color = Color(0xFFF0A000), // Orange
        rotations = listOf(
            // Up
            listOf(
                Position(0, 2),
                Position(1, 0), Position(1, 1), Position(1, 2)
            ),
            // Right
            listOf(
                Position(0, 1),
                Position(1, 1),
                Position(2, 1), Position(2, 2)
            ),
            // Down
            listOf(
                Position(1, 0), Position(1, 1), Position(1, 2),
                Position(2, 0)
            ),
            // Left
            listOf(
                Position(0, 0), Position(0, 1),
                Position(1, 1),
                Position(2, 1)
            )
        )
    ) {
        override fun rotate(clockwise: Boolean): Tetromino {
            val newRotation = if (clockwise) {
                (rotation + 1) % 4
            } else {
                (rotation - 1 + 4) % 4
            }
            return L(newRotation)
        }
    }

    /**
     * Get the blocks of the current rotation as absolute positions.
     */
    fun getBlocks(): List<Position> = rotations[currentRotation]

    companion object {
        fun allPieces(): List<Tetromino> = listOf(
            I(), O(), T(), S(), Z(), J(), L()
        )

        fun randomPiece(): Tetromino = allPieces().random()
    }
}
