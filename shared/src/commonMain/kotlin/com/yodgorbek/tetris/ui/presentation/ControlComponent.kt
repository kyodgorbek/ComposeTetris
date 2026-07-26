package com.yodgorbek.tetris.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yodgorbek.tetris.game.state.GameAction

@Composable
fun ControlComponent(
    onAction: (GameAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Directional controls
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ControlButton(onClick = { onAction(GameAction.RotateClockwise) }) {
                Icon(Icons.Default.Refresh, contentDescription = "Rotate")
            }
            Row {
                ControlButton(onClick = { onAction(GameAction.MoveLeft) }) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Left")
                }
                ControlButton(onClick = { onAction(GameAction.MoveDown) }) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down")
                }
                ControlButton(onClick = { onAction(GameAction.MoveRight) }) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Right")
                }
            }
        }

        // Action controls
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ControlButton(onClick = { onAction(GameAction.Hold) }, color = Color.Blue.copy(alpha = 0.6f)) {
                Icon(Icons.Default.Share, contentDescription = "Hold") // Using Share as placeholder for Hold
            }
            ControlButton(onClick = { onAction(GameAction.HardDrop) }, color = Color.Red.copy(alpha = 0.6f)) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Hard Drop")
            }
        }
    }
}

@Composable
private fun ControlButton(
    onClick: () -> Unit,
    color: Color = Color.DarkGray.copy(alpha = 0.6f),
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(color)
    ) {
        content()
    }
}
