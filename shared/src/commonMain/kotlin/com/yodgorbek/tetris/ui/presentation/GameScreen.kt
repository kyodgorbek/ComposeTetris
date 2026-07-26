package com.yodgorbek.tetris.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val logic = remember { GameLogic() }
    val ghostOffset = remember(state) { logic.getGhostOffset(state) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TetrisColors.Background)
            .padding(16.dp),
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
                modifier = Modifier.weight(2f)
            )

            HUDComponent(
                score = state.score,
                level = state.level,
                lines = state.lines,
                nextPiece = state.nextPiece,
                heldPiece = state.heldPiece,
                modifier = Modifier.weight(1f)
            )
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
        androidx.compose.material3.Button(onClick = onStart) {
            androidx.compose.material3.Text(if (status == GameStatus.IDLE) "START GAME" else "GAME OVER - RESTART")
        }
    }
}
