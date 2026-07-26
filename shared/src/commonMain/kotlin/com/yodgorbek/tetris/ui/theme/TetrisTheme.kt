package com.yodgorbek.tetris.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFFEFEFEF),
    surface = Color(0xFFFFFFFF),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
)

object TetrisColors {
    val Background = Color(0xFF9EAD86) // LCD Greenish/Yellowish
    val GridLine = Color(0x1F000000)
    val BrickNone = Color(0x0F000000)
    val BrickOccupied = Color(0xCC000000)

    // Colorful bricks if enabled
    val Cyan = Color(0xFF00FFFF)
    val Yellow = Color(0xFFFFFF00)
    val Purple = Color(0xFF800080)
    val Green = Color(0xFF00FF00)
    val Red = Color(0xFFFF0000)
    val Blue = Color(0xFF0000FF)
    val Orange = Color(0xFFFFA500)
}

@Composable
fun TetrisTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
