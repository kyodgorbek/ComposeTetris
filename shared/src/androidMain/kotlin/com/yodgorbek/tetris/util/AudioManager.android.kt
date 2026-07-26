package com.yodgorbek.tetris.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

actual class AudioManager {
    // In a real app, we'd pass Context here or use a singleton
    // For now, keeping it simple

    actual fun playSound(type: SoundType) {
        println("Android: Playing sound $type")
    }

    actual fun playMusic() {
        println("Android: Playing music")
    }

    actual fun stopMusic() {
        println("Android: Stopping music")
    }

    actual fun release() {
    }
}
