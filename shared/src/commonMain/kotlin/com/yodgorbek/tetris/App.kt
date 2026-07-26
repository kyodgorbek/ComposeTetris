package com.yodgorbek.tetris

import androidx.compose.runtime.Composable
import com.yodgorbek.tetris.ui.presentation.GameScreen
import com.yodgorbek.tetris.ui.theme.TetrisTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    TetrisTheme {
        GameScreen()
    }
}
