package com.yodgorbek.tetris.game.logic

import com.yodgorbek.tetris.game.model.Rotation
import com.yodgorbek.tetris.game.model.TetrominoType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RotationLogicTest {

    @Test
    fun testStandardKicks() {
        val kicks = RotationLogic.getKicks(TetrominoType.T, Rotation.ROT_0, Rotation.ROT_90)
        assertEquals(5, kicks.size)
        assertEquals(0, kicks[0].x)
        assertEquals(0, kicks[0].y)
    }

    @Test
    fun testIKicks() {
        val kicks = RotationLogic.getKicks(TetrominoType.I, Rotation.ROT_0, Rotation.ROT_90)
        assertEquals(5, kicks.size)
        // I piece has unique kick table
        assertTrue(kicks.any { it.x == -2 })
    }

    @Test
    fun testOKicks() {
        val kicks = RotationLogic.getKicks(TetrominoType.O, Rotation.ROT_0, Rotation.ROT_90)
        assertEquals(1, kicks.size)
        assertEquals(0, kicks[0].x)
        assertEquals(0, kicks[0].y)
    }
}
