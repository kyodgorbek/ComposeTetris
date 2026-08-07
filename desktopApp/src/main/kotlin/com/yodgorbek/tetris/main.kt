package com.yodgorbek.tetris

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.yodgorbek.tetris.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ComposeTetris",
        ) {
            App()
        }
    }
}
