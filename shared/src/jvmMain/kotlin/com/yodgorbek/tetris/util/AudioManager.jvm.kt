package com.yodgorbek.tetris.util

actual class AudioManager {
    actual fun playSound(type: SoundType) {
        println("JVM: Playing sound $type")
    }

    actual fun playMusic() {
        println("JVM: Playing music")
    }

    actual fun stopMusic() {
        println("JVM: Stopping music")
    }

    actual fun release() {
    }
}
