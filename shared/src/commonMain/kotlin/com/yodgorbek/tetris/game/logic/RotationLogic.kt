package com.yodgorbek.tetris.game.logic

import com.yodgorbek.tetris.game.model.Offset
import com.yodgorbek.tetris.game.model.Rotation
import com.yodgorbek.tetris.game.model.TetrominoType

object RotationLogic {

    // SRS Kick tables for J, L, S, T, Z pieces
    private val standardKicks = mapOf(
        (Rotation.ROT_0 to Rotation.ROT_90) to listOf(Offset(0, 0), Offset(-1, 0), Offset(-1, 1), Offset(0, -2), Offset(-1, -2)),
        (Rotation.ROT_90 to Rotation.ROT_0) to listOf(Offset(0, 0), Offset(1, 0), Offset(1, -1), Offset(0, 2), Offset(1, 2)),
        (Rotation.ROT_90 to Rotation.ROT_180) to listOf(Offset(0, 0), Offset(1, 0), Offset(1, -1), Offset(0, 2), Offset(1, 2)),
        (Rotation.ROT_180 to Rotation.ROT_90) to listOf(Offset(0, 0), Offset(-1, 0), Offset(-1, 1), Offset(0, -2), Offset(-1, -2)),
        (Rotation.ROT_180 to Rotation.ROT_270) to listOf(Offset(0, 0), Offset(1, 0), Offset(1, 1), Offset(0, -2), Offset(1, -2)),
        (Rotation.ROT_270 to Rotation.ROT_180) to listOf(Offset(0, 0), Offset(-1, 0), Offset(-1, -1), Offset(0, 2), Offset(-1, 2)),
        (Rotation.ROT_270 to Rotation.ROT_0) to listOf(Offset(0, 0), Offset(-1, 0), Offset(-1, -1), Offset(0, 2), Offset(-1, 2)),
        (Rotation.ROT_0 to Rotation.ROT_270) to listOf(Offset(0, 0), Offset(1, 0), Offset(1, 1), Offset(0, -2), Offset(1, -2))
    )

    // SRS Kick tables for I piece
    private val iKicks = mapOf(
        (Rotation.ROT_0 to Rotation.ROT_90) to listOf(Offset(0, 0), Offset(-2, 0), Offset(1, 0), Offset(-2, -1), Offset(1, 2)),
        (Rotation.ROT_90 to Rotation.ROT_0) to listOf(Offset(0, 0), Offset(2, 0), Offset(-1, 0), Offset(2, 1), Offset(-1, -2)),
        (Rotation.ROT_90 to Rotation.ROT_180) to listOf(Offset(0, 0), Offset(-1, 0), Offset(2, 0), Offset(-1, 2), Offset(2, -1)),
        (Rotation.ROT_180 to Rotation.ROT_90) to listOf(Offset(0, 0), Offset(1, 0), Offset(-2, 0), Offset(1, -2), Offset(-2, 1)),
        (Rotation.ROT_180 to Rotation.ROT_270) to listOf(Offset(0, 0), Offset(2, 0), Offset(-1, 0), Offset(2, 1), Offset(-1, -2)),
        (Rotation.ROT_270 to Rotation.ROT_180) to listOf(Offset(0, 0), Offset(-2, 0), Offset(1, 0), Offset(-2, -1), Offset(1, 2)),
        (Rotation.ROT_270 to Rotation.ROT_0) to listOf(Offset(0, 0), Offset(1, 0), Offset(-2, 0), Offset(1, -2), Offset(-2, 1)),
        (Rotation.ROT_0 to Rotation.ROT_270) to listOf(Offset(0, 0), Offset(-1, 0), Offset(2, 0), Offset(-1, 2), Offset(2, -1))
    )

    fun getKicks(type: TetrominoType, from: Rotation, to: Rotation): List<Offset> {
        if (type == TetrominoType.O) return listOf(Offset(0, 0))
        return if (type == TetrominoType.I) iKicks[from to to] ?: listOf(Offset(0, 0))
        else standardKicks[from to to] ?: listOf(Offset(0, 0))
    }
}
