package com.yodgorbek.tetris.game.model

enum class TetrominoType {
    I, O, T, S, Z, J, L
}

enum class Rotation {
    ROT_0, ROT_90, ROT_180, ROT_270;

    fun rotateClockwise(): Rotation = when (this) {
        ROT_0 -> ROT_90
        ROT_90 -> ROT_180
        ROT_180 -> ROT_270
        ROT_270 -> ROT_0
    }

    fun rotateCounterClockwise(): Rotation = when (this) {
        ROT_0 -> ROT_270
        ROT_270 -> ROT_180
        ROT_180 -> ROT_90
        ROT_90 -> ROT_0
    }
}

data class Tetromino(
    val type: TetrominoType,
    val offset: Offset = Offset(0, 0),
    val rotation: Rotation = Rotation.ROT_0
) {
    val shape: List<Offset>
        get() = getShape(type, rotation)

    companion object {
        fun getShape(type: TetrominoType, rotation: Rotation): List<Offset> = when (type) {
            TetrominoType.I -> getIShape(rotation)
            TetrominoType.O -> listOf(Offset(1, 0), Offset(2, 0), Offset(1, 1), Offset(2, 1))
            TetrominoType.T -> getTShape(rotation)
            TetrominoType.S -> getSShape(rotation)
            TetrominoType.Z -> getZShape(rotation)
            TetrominoType.J -> getJShape(rotation)
            TetrominoType.L -> getLShape(rotation)
        }

        private fun getIShape(rotation: Rotation) = when (rotation) {
            Rotation.ROT_0 -> listOf(Offset(0, 1), Offset(1, 1), Offset(2, 1), Offset(3, 1))
            Rotation.ROT_90 -> listOf(Offset(2, 0), Offset(2, 1), Offset(2, 2), Offset(2, 3))
            Rotation.ROT_180 -> listOf(Offset(0, 2), Offset(1, 2), Offset(2, 2), Offset(3, 2))
            Rotation.ROT_270 -> listOf(Offset(1, 0), Offset(1, 1), Offset(1, 2), Offset(1, 3))
        }

        private fun getTShape(rotation: Rotation) = when (rotation) {
            Rotation.ROT_0 -> listOf(Offset(1, 0), Offset(0, 1), Offset(1, 1), Offset(2, 1))
            Rotation.ROT_90 -> listOf(Offset(1, 0), Offset(1, 1), Offset(2, 1), Offset(1, 2))
            Rotation.ROT_180 -> listOf(Offset(0, 1), Offset(1, 1), Offset(2, 1), Offset(1, 2))
            Rotation.ROT_270 -> listOf(Offset(1, 0), Offset(0, 1), Offset(1, 1), Offset(1, 2))
        }

        private fun getSShape(rotation: Rotation) = when (rotation) {
            Rotation.ROT_0 -> listOf(Offset(1, 0), Offset(2, 0), Offset(0, 1), Offset(1, 1))
            Rotation.ROT_90 -> listOf(Offset(1, 0), Offset(1, 1), Offset(2, 1), Offset(2, 2))
            Rotation.ROT_180 -> listOf(Offset(1, 1), Offset(2, 1), Offset(0, 2), Offset(1, 2))
            Rotation.ROT_270 -> listOf(Offset(0, 0), Offset(0, 1), Offset(1, 1), Offset(1, 2))
        }

        private fun getZShape(rotation: Rotation) = when (rotation) {
            Rotation.ROT_0 -> listOf(Offset(0, 0), Offset(1, 0), Offset(1, 1), Offset(2, 1))
            Rotation.ROT_90 -> listOf(Offset(2, 0), Offset(1, 1), Offset(2, 1), Offset(1, 2))
            Rotation.ROT_180 -> listOf(Offset(0, 1), Offset(1, 1), Offset(1, 2), Offset(2, 2))
            Rotation.ROT_270 -> listOf(Offset(1, 0), Offset(0, 1), Offset(1, 1), Offset(0, 2))
        }

        private fun getJShape(rotation: Rotation) = when (rotation) {
            Rotation.ROT_0 -> listOf(Offset(0, 0), Offset(0, 1), Offset(1, 1), Offset(2, 1))
            Rotation.ROT_90 -> listOf(Offset(1, 0), Offset(2, 0), Offset(1, 1), Offset(1, 2))
            Rotation.ROT_180 -> listOf(Offset(0, 1), Offset(1, 1), Offset(2, 1), Offset(2, 2))
            Rotation.ROT_270 -> listOf(Offset(1, 0), Offset(1, 1), Offset(0, 2), Offset(1, 2))
        }

        private fun getLShape(rotation: Rotation) = when (rotation) {
            Rotation.ROT_0 -> listOf(Offset(2, 0), Offset(0, 1), Offset(1, 1), Offset(2, 1))
            Rotation.ROT_90 -> listOf(Offset(1, 0), Offset(1, 1), Offset(1, 2), Offset(2, 2))
            Rotation.ROT_180 -> listOf(Offset(0, 1), Offset(1, 1), Offset(2, 1), Offset(0, 2))
            Rotation.ROT_270 -> listOf(Offset(0, 0), Offset(1, 0), Offset(1, 1), Offset(1, 2))
        }
    }
}
