package com.yodgorbek.tetris.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yodgorbek.tetris.game.engine.GameStatus
import com.yodgorbek.tetris.ui.theme.GameColors
import com.yodgorbek.tetris.ui.theme.TetrisTheme

/**
 * Main game screen composable.
 * Displays the board, HUD, and controls.
 */
@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
) {
    val gameState = viewModel.gameState.collectAsState()
    
    // Start game on first composition
    LaunchedEffect(Unit) {
        viewModel.startGame()
    }
    
    TetrisTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GameColors.BoardBackground),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Left panel - Hold piece
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HoldPanel(
                        heldPiece = gameState.value.heldPiece,
                        canHold = gameState.value.canHold,
                        cellSize = 15f,
                        modifier = Modifier.width(120.dp)
                    )
                }
                
                // Center - Game board
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Board
                    GameBoard(
                        gameState = gameState.value,
                        cellSize = 20f,
                        modifier = Modifier
                            .background(GameColors.BoardBackground)
                    )
                    
                    // Control buttons
                    GameControls(viewModel, gameState)
                }
                
                // Right panel - HUD and Next pieces
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HUDPanel(
                        gameState = gameState.value,
                        modifier = Modifier.width(140.dp)
                    )
                    
                    NextPiecesPanel(
                        nextPieces = gameState.value.nextPieces,
                        cellSize = 15f,
                        modifier = Modifier.width(140.dp)
                    )
                }
            }
            
            // Game over overlay
            if (gameState.value.gameStatus == GameStatus.GAME_OVER) {
                GameOverOverlay(onRestart = { viewModel.restart() })
            }
            
            // Pause overlay
            if (gameState.value.isPaused && gameState.value.gameStatus == GameStatus.PLAYING) {
                PauseOverlay(
                    onResume = { viewModel.resume() },
                    onRestart = { viewModel.restart() }
                )
            }
        }
    }
}

/**
 * Game control buttons.
 */
@Composable
fun GameControls(
    viewModel: GameViewModel,
    gameState: androidx.compose.runtime.State<com.yodgorbek.tetris.game.engine.GameState>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        // Rotation buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.rotateCounterClockwise() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GameColors.Panel
                ),
                modifier = Modifier.width(80.dp)
            ) {
                Text("↺", fontSize = 16.sp)
            }
            Button(
                onClick = { viewModel.rotateClockwise() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GameColors.Panel
                ),
                modifier = Modifier.width(80.dp)
            ) {
                Text("↻", fontSize = 16.sp)
            }
        }
        
        // Movement buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.moveLeft() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GameColors.Panel
                ),
                modifier = Modifier.width(50.dp)
            ) {
                Text("←", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(60.dp))
            Button(
                onClick = { viewModel.moveRight() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GameColors.Panel
                ),
                modifier = Modifier.width(50.dp)
            ) {
                Text("→", fontSize = 16.sp)
            }
        }
        
        // Drop buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.softDrop() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GameColors.Panel
                ),
                modifier = Modifier.width(80.dp)
            ) {
                Text("SOFT", fontSize = 12.sp)
            }
            Button(
                onClick = { viewModel.hardDrop() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GameColors.Panel
                ),
                modifier = Modifier.width(80.dp)
            ) {
                Text("HARD", fontSize = 12.sp)
            }
        }
        
        // Hold button
        Button(
            onClick = { viewModel.hold() },
            colors = ButtonDefaults.buttonColors(
                containerColor = GameColors.Panel
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("HOLD")
        }
        
        // Pause/Resume
        Button(
            onClick = {
                if (gameState.value.isPaused) {
                    viewModel.resume()
                } else {
                    viewModel.pause()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = GameColors.Panel
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (gameState.value.isPaused) "RESUME" else "PAUSE")
        }
        
        // Restart
        Button(
            onClick = { viewModel.restart() },
            colors = ButtonDefaults.buttonColors(
                containerColor = GameColors.Panel
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("RESTART")
        }
    }
}

/**
 * Game over overlay.
 */
@Composable
fun GameOverOverlay(
    onRestart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.BoardBackground.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(32.dp)
                .background(GameColors.Panel)
                .padding(32.dp)
        ) {
            Text(
                text = "GAME OVER",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = GameColors.Text
            )
            
            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GameColors.Panel
                )
            ) {
                Text("NEW GAME")
            }
        }
    }
}

/**
 * Pause overlay.
 */
@Composable
fun PauseOverlay(
    onResume: () -> Unit,
    onRestart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.BoardBackground.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(32.dp)
                .background(GameColors.Panel)
                .padding(32.dp)
        ) {
            Text(
                text = "PAUSED",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = GameColors.Text
            )
            
            Button(
                onClick = onResume,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GameColors.Panel
                )
            ) {
                Text("RESUME")
            }
            
            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GameColors.Panel
                )
            ) {
                Text("RESTART")
            }
        }
    }
}
