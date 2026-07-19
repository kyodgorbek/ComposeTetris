package com.yodgorbek.tetris.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yodgorbek.tetris.game.engine.GameState
import com.yodgorbek.tetris.game.model.Board
import com.yodgorbek.tetris.game.model.Position
import com.yodgorbek.tetris.game.model.Tetromino
import com.yodgorbek.tetris.ui.theme.GameColors
import com.yodgorbek.tetris.ui.theme.getTetrominoColor

/**
 * Renders a single cell in the game board.
 */
@Composable
fun GameCell(
    occupied: Boolean,
    color: Color,
    cellSize: Float,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (occupied) color else GameColors.EmptyCell
    
    Box(
        modifier = modifier
            .size(cellSize.dp)
            .background(backgroundColor)
            .border(0.5.dp, GameColors.GridLine)
    )
}

/**
 * Renders the game board with all placed pieces.
 */
@Composable
fun GameBoard(
    gameState: GameState,
    cellSize: Float = 20f,
    modifier: Modifier = Modifier
) {
    val board = gameState.board
    val boardWidth = Board.COLUMNS
    
    Box(
        modifier = modifier
            .background(GameColors.BoardBackground)
            .border(2.dp, GameColors.BoardBorder)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(boardWidth),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(board.grid.size * boardWidth) { index ->
                val row = index / boardWidth
                val col = index % boardWidth
                val cell = board.grid[row][col]
                
                GameCell(
                    occupied = cell.occupied,
                    color = cell.color,
                    cellSize = cellSize
                )
            }
        }
    }
}

/**
 * Renders the HUD panel with score, level, and lines.
 */
@Composable
fun HUDPanel(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(GameColors.Panel)
            .border(1.dp, GameColors.PanelBorder)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Score
        Column {
            Text(
                text = "SCORE",
                color = GameColors.TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = gameState.score.toString(),
                color = GameColors.Text,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Level
        Column {
            Text(
                text = "LEVEL",
                color = GameColors.TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = gameState.level.toString(),
                color = GameColors.Text,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Lines
        Column {
            Text(
                text = "LINES",
                color = GameColors.TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = gameState.lines.toString(),
                color = GameColors.Text,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Renders the next pieces preview panel.
 */
@Composable
fun NextPiecesPanel(
    nextPieces: List<Tetromino>,
    cellSize: Float = 15f,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(GameColors.Panel)
            .border(1.dp, GameColors.PanelBorder)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "NEXT",
            color = GameColors.TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        
        nextPieces.take(3).forEach { piece ->
            PiecePreview(piece, cellSize)
        }
    }
}

/**
 * Renders a small preview of a tetromino piece.
 */
@Composable
fun PiecePreview(
    piece: Tetromino,
    cellSize: Float = 15f,
    modifier: Modifier = Modifier
) {
    val blocks = piece.getBlocks()
    val maxCol = (blocks.maxOfOrNull { it.column } ?: 0) + 1
    val maxRow = (blocks.maxOfOrNull { it.row } ?: 0) + 1
    
    Box(
        modifier = modifier
            .background(GameColors.BoardBackground)
            .border(0.5.dp, GameColors.GridLine)
            .padding(4.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(maxCol.coerceAtLeast(1)),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(maxRow * maxCol) { index ->
                val row = index / maxCol
                val col = index % maxCol
                val hasBlock = blocks.any { it.row == row && it.column == col }
                
                GameCell(
                    occupied = hasBlock,
                    color = if (hasBlock) piece.color else Color.Transparent,
                    cellSize = cellSize
                )
            }
        }
    }
}

/**
 * Renders the held piece panel.
 */
@Composable
fun HoldPanel(
    heldPiece: Tetromino?,
    canHold: Boolean,
    cellSize: Float = 15f,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(GameColors.Panel)
            .border(1.dp, GameColors.PanelBorder)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "HOLD",
            color = GameColors.TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        
        if (heldPiece != null) {
            PiecePreview(heldPiece, cellSize)
            if (!canHold) {
                Text(
                    text = "(used)",
                    color = GameColors.TextSecondary,
                    fontSize = 10.sp
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(GameColors.BoardBackground)
                    .border(0.5.dp, GameColors.GridLine),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "---",
                    color = GameColors.TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}
