package com.yodgorbek.tetris.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Tetris game colors and theme.
 */

// Tetromino colors (standard Tetris colors)
object TetrominoColors {
    val I = Color(0xFF00F0F0)  // Cyan
    val O = Color(0xFFF0F000)  // Yellow
    val T = Color(0xFFF000F0)  // Magenta
    val S = Color(0xFF00F000)  // Green
    val Z = Color(0xFFF00000)  // Red
    val J = Color(0xFF0000F0)  // Blue
    val L = Color(0xFFF0A000)  // Orange
}

// Game UI colors
object GameColors {
    val BoardBackground = Color(0xFF0A0A0A)      // Dark background
    val GridLine = Color(0xFF1A1A1A)             // Grid lines
    val BoardBorder = Color(0xFF333333)          // Border
    val EmptyCell = Color(0xFF151515)            // Empty cell
    val GhostPiece = Color(0xFF404040)           // Ghost piece (dim)
    val Text = Color(0xFFFFFFFF)                 // White text
    val TextSecondary = Color(0xFFB0B0B0)        // Secondary text
    val Panel = Color(0xFF1A1A1A)                // Panel background
    val PanelBorder = Color(0xFF404040)          // Panel border
}

// Dark theme
private val darkColorScheme = darkColorScheme(
    primary = Color(0xFF00F0F0),
    secondary = Color(0xFFF0F000),
    tertiary = Color(0xFFF000F0),
    background = GameColors.BoardBackground,
    surface = GameColors.Panel,
    onBackground = GameColors.Text,
    onSurface = GameColors.Text
)

/**
 * Tetris game theme.
 */
@Composable
fun TetrisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme,
        content = content
    )
}

/**
 * Get tetromino color by ID.
 */
fun getTetrominoColor(tetrominoId: Int): Color = when (tetrominoId) {
    0 -> TetrominoColors.I
    1 -> TetrominoColors.O
    2 -> TetrominoColors.T
    3 -> TetrominoColors.S
    4 -> TetrominoColors.Z
    5 -> TetrominoColors.J
    6 -> TetrominoColors.L
    else -> Color.Gray
}
