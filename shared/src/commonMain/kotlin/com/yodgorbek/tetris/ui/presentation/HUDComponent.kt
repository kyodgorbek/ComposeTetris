package com.yodgorbek.tetris.ui.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset as ComposeOffset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yodgorbek.tetris.game.model.Rotation
import com.yodgorbek.tetris.game.model.Tetromino
import com.yodgorbek.tetris.game.model.TetrominoType
import com.yodgorbek.tetris.ui.theme.TetrisColors

@Composable
fun HUDComponent(
    score: Int,
    level: Int,
    lines: Int,
    nextPiece: TetrominoType,
    heldPiece: TetrominoType?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatBox("SCORE", score.toString())
        StatBox("LEVEL", level.toString())
        StatBox("LINES", lines.toString())

        PiecePreview("NEXT", nextPiece)
        PiecePreview("HOLD", heldPiece)
    }
}

@Composable
private fun StatBox(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
    }
}

@Composable
private fun PiecePreview(label: String, type: TetrominoType?) {
    Column {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(TetrisColors.Background)
                .border(1.dp, Color.Black)
        ) {
            if (type != null) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val blockSize = size.width / 4
                    val shape = Tetromino.getShape(type, Rotation.ROT_0)

                    // Center the piece in the 4x4 preview
                    val minX = shape.minOf { it.x }
                    val maxX = shape.maxOf { it.x }
                    val minY = shape.minOf { it.y }
                    val maxY = shape.maxOf { it.y }

                    val offsetX = (4 - (maxX - minX + 1)) / 2f - minX
                    val offsetY = (4 - (maxY - minY + 1)) / 2f - minY

                    shape.forEach { block ->
                        drawBrick(
                            x = block.x + offsetX,
                            y = block.y + offsetY,
                            size = blockSize,
                            color = Color(getPieceColor(type))
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

    drawRect(
        color = color,
        topLeft = ComposeOffset(x * size + padding, y * size + padding),
        size = Size(innerSize, innerSize)
    )
    drawRect(
        color = Color.Black.copy(alpha = 0.1f),
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
