package com.yodgorbek.tetris.util

actual class AudioManager {
    actual fun playSound(type: SoundType) {
        println("iOS: Playing sound $type")
    }

    actual fun playMusic() {
        println("iOS: Playing music")
    }

    actual fun stopMusic() {
        println("iOS: Stopping music")
    }

    actual fun release() {
    }
}
