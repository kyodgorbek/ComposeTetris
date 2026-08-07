package com.yodgorbek.tetris.ui.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset as ComposeOffset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.yodgorbek.tetris.game.model.Board
import com.yodgorbek.tetris.game.model.Tetromino
import com.yodgorbek.tetris.game.model.TetrominoType
import com.yodgorbek.tetris.ui.theme.TetrisColors

@Composable
fun BoardComponent(
    board: Board,
    currentPiece: Tetromino?,
    ghostOffset: com.yodgorbek.tetris.game.model.Offset?,
    clearingLines: List<Int>,
    lastLockTimestamp: Long,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val clearAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxHeight()
            .background(TetrisColors.Background)
            .border(2.dp, Color.Black)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        val boardWidth = maxWidth
        val boardHeight = maxHeight

        val potentialBlockSizeByWidth = boardWidth / board.columns
        val potentialBlockSizeByHeight = boardHeight / board.rows
        val blockSizeDp = minOf(potentialBlockSizeByWidth, potentialBlockSizeByHeight)

        val actualBoardWidth = blockSizeDp * board.columns
        val actualBoardHeight = blockSizeDp * board.rows

        Canvas(modifier = Modifier.size(actualBoardWidth, actualBoardHeight)) {
            val blockSize = size.width / board.columns

            // Draw board grid and cells
            for (y in 0 until board.rows) {
                val isClearing = y in clearingLines
                val rowAlpha = if (isClearing) clearAlpha else 1f

                for (x in 0 until board.columns) {
                    val cell = board.grid[y][x]
                    drawBrick(
                        x = x.toFloat(),
                        y = y.toFloat(),
                        size = blockSize,
                        color = if (cell.occupied) Color(cell.color).copy(alpha = rowAlpha) else TetrisColors.BrickNone
                    )
                }
            }

            // Draw ghost piece
            if (ghostOffset != null && currentPiece != null && clearingLines.isEmpty()) {
                currentPiece.shape.forEach { block ->
                    val x = block.x + ghostOffset.x
                    val y = block.y + ghostOffset.y
                    if (y in 0 until board.rows) {
                        drawBrick(
                            x = x.toFloat(),
                            y = y.toFloat(),
                            size = blockSize,
                            color = Color.Black.copy(alpha = 0.1f)
                        )
                    }
                }
            }

            // Draw current piece
            currentPiece?.let { piece ->
                piece.shape.forEach { block ->
                    val x = block.x + piece.offset.x
                    val y = block.y + piece.offset.y
                    if (y in 0 until board.rows) {
                        drawBrick(
                            x = x.toFloat(),
                            y = y.toFloat(),
                            size = blockSize,
                            color = Color(getPieceColor(piece.type))
                        )
                    }
                }
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBrick(
    x: Float,
    y: Float,
    size: Float,
    color: Color
) {
    val padding = size * 0.05f
    val innerSize = size - 2 * padding

    // Outer border
    drawRect(
        color = Color.Black.copy(alpha = 0.2f * color.alpha),
        topLeft = ComposeOffset(x * size, y * size),
        size = Size(size, size),
        style = Stroke(width = 1f)
    )

    // Main body
    drawRect(
        color = color,
        topLeft = ComposeOffset(x * size + padding, y * size + padding),
        size = Size(innerSize, innerSize)
    )

    // Inner square (Brick effect)
    drawRect(
        color = Color.Black.copy(alpha = 0.1f * color.alpha),
        topLeft = ComposeOffset(x * size + size * 0.25f, y * size + size * 0.25f),
        size = Size(size * 0.5f, size * 0.5f),
        style = Stroke(width = 1f)
    )
}

private fun getPieceColor(type: TetrominoType): Long = when (type) {
    TetrominoType.I -> 0xFF00FFFF // Cyan
    TetrominoType.O -> 0xFFFFFF00 // Yellow
    TetrominoType.T -> 0xFF800080 // Purple
    TetrominoType.S -> 0xFF00FF00 // Green
    TetrominoType.Z -> 0xFFFF0000 // Red
    TetrominoType.J -> 0xFF0000FF // Blue
    TetrominoType.L -> 0xFFFFA500 // Orange
}
