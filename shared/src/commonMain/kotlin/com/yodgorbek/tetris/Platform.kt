package com.yodgorbek.tetris

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect fun currentTimeMillis(): Long