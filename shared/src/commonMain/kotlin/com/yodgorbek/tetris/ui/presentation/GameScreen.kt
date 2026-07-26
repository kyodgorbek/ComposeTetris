package com.yodgorbek.tetris.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yodgorbek.tetris.game.logic.GameLogic
import com.yodgorbek.tetris.game.state.GameAction
import com.yodgorbek.tetris.game.state.GameStatus
import com.yodgorbek.tetris.ui.theme.TetrisColors

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel { GameViewModel() }
) {
    val state by viewModel.state.collectAsState()
    val highScores by viewModel.highScores.collectAsState()
    val logic = remember { GameLogic() }
    val ghostOffset = remember(state) { logic.getGhostOffset(state) }
    val focusRequester = remember { FocusRequester() }
    var showSettings by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TetrisColors.Background)
                .padding(16.dp)
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown) {
                        when (keyEvent.key) {
                            Key.DirectionLeft -> { viewModel.dispatch(GameAction.MoveLeft); true }
                            Key.DirectionRight -> { viewModel.dispatch(GameAction.MoveRight); true }
                            Key.DirectionDown -> { viewModel.dispatch(GameAction.MoveDown); true }
                            Key.DirectionUp -> { viewModel.dispatch(GameAction.RotateClockwise); true }
                            Key.Spacebar -> { viewModel.dispatch(GameAction.HardDrop); true }
                            Key.ShiftLeft, Key.ShiftRight -> { viewModel.dispatch(GameAction.Hold); true }
                            else -> false
                        }
                    } else false
                }
                .focusRequester(focusRequester)
                .focusable(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                BoardComponent(
                    board = state.board,
                    currentPiece = state.currentPiece,
                    ghostOffset = ghostOffset,
                    clearingLines = state.clearingLines,
                    lastLockTimestamp = state.lastLockTimestamp,
                    modifier = Modifier.weight(2f)
                )

                Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                    HUDComponent(
                        score = state.score,
                        level = state.level,
                        lines = state.lines,
                        nextPiece = state.nextPiece,
                        heldPiece = state.heldPiece,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("HIGH SCORES", fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    highScores.forEach { score ->
                        Text("${score.score}", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = { showSettings = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            }

            if (state.status == GameStatus.IDLE || state.status == GameStatus.GAME_OVER) {
                StartOverlay(
                    status = state.status,
                    onStart = { viewModel.dispatch(GameAction.Start) }
                )
            } else {
                ControlComponent(
                    onAction = { viewModel.dispatch(it) },
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        if (showSettings) {
            SettingsDialog(
                isSoundEnabled = viewModel.isSoundEnabled(),
                onToggleSound = { viewModel.toggleSound() },
                onDismiss = { showSettings = false }
            )
        }
    }
}

@Composable
fun SettingsDialog(
    isSoundEnabled: Boolean,
    onToggleSound: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Settings") },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Sound Effects")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = isSoundEnabled, onCheckedChange = { onToggleSound() })
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun StartOverlay(
    status: GameStatus,
    onStart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onStart) {
            Text(if (status == GameStatus.IDLE) "START GAME" else "GAME OVER - RESTART")
        }
    }
}
