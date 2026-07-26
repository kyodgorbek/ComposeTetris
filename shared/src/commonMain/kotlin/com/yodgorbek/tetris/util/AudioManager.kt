package com.yodgorbek.tetris.util

enum class SoundType {
    MOVE, ROTATE, LOCK, CLEAR, GAME_OVER, START
}

expect class AudioManager() {
    fun playSound(type: SoundType)
    fun playMusic()
    fun stopMusic()
    fun release()
}
