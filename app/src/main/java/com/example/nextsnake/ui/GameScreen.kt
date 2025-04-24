package com.example.nextsnake.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nextsnake.model.Direction
import com.example.nextsnake.model.GameState
import com.example.nextsnake.ui.theme.NextSnakeTheme
import com.example.nextsnake.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameState by gameViewModel.gameState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Next Snake") },
                colors = TopAppBarDefaults.topAppBarColors(
                     containerColor = MaterialTheme.colorScheme.primary,
                     titleContentColor = MaterialTheme.colorScheme.onPrimary,
                     actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { gameViewModel.resetGame() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Restart Game")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp), // Add padding around the content
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween // Pushes controls to bottom
            ) {
                // Score Display
                Text(
                    text = "Score: ${gameState.score}",
                    style = MaterialTheme.typography.headlineMedium, // Use custom style
                    color = MaterialTheme.colorScheme.tertiary // Use accent color
                )

                Spacer(modifier = Modifier.height(16.dp)) // Space between score and board

                // Game Board
                GameBoard(
                    gameState = gameState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f) // Ensure square board
                )

                 Spacer(modifier = Modifier.weight(1f)) // Pushes controls down

                // Game Controls
                GameControls(
                     isRunning = gameState.isRunning,
                     isGameOver = gameState.isGameOver,
                     onDirectionChange = { gameViewModel.changeDirection(it) },
                     onPlayPauseToggle = { gameViewModel.togglePlayPause() },
                     onRestart = { gameViewModel.resetGame() } // Pass restart action
                 )
            }
        }
    )
}

@Composable
fun GameControls(
    isRunning: Boolean,
    isGameOver: Boolean,
    onDirectionChange: (Direction) -> Unit,
    onPlayPauseToggle: () -> Unit,
    onRestart: () -> Unit
) {
    val buttonSize = 64.dp
    val iconSize = 36.dp

     Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Directional Buttons (Grid Layout)
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(buttonSize)) // Placeholder for left button
            ControlButton(
                onClick = { onDirectionChange(Direction.UP) },
                icon = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Move Up",
                size = buttonSize,
                 iconSize = iconSize,
                 enabled = !isGameOver // Disable if game over
            )
            Spacer(modifier = Modifier.width(buttonSize)) // Placeholder for right button
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ControlButton(
                onClick = { onDirectionChange(Direction.LEFT) },
                icon = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Move Left",
                size = buttonSize,
                 iconSize = iconSize,
                 enabled = !isGameOver
            )
             // Play/Pause/Restart Button
             ControlButton(
                 onClick = { if (isGameOver) onRestart() else onPlayPauseToggle() },
                 icon = when {
                     isGameOver -> Icons.Filled.Refresh // Show Restart if game over
                     isRunning -> Icons.Filled.Pause    // Show Pause if running
                     else -> Icons.Filled.PlayArrow // Show Play if paused/stopped
                 },
                 contentDescription = when {
                    isGameOver -> "Restart Game"
                    isRunning -> "Pause Game"
                    else -> "Play Game"
                 },
                 size = buttonSize,
                 iconSize = iconSize,
                 containerColor = if (isGameOver) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary, // Yellow for restart
                 contentColor = if (isGameOver) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onPrimary
             )
            ControlButton(
                onClick = { onDirectionChange(Direction.RIGHT) },
                icon = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Move Right",
                size = buttonSize,
                 iconSize = iconSize,
                 enabled = !isGameOver
            )
        }
         Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
             Spacer(modifier = Modifier.width(buttonSize))
             ControlButton(
                 onClick = { onDirectionChange(Direction.DOWN) },
                 icon = Icons.Filled.KeyboardArrowDown,
                 contentDescription = "Move Down",
                 size = buttonSize,
                 iconSize = iconSize,
                 enabled = !isGameOver
             )
              Spacer(modifier = Modifier.width(buttonSize))
         }
    }
}

@Composable
fun ControlButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: androidx.compose.ui.unit.Dp = 64.dp,
    iconSize: androidx.compose.ui.unit.Dp = 36.dp,
    shape: androidx.compose.ui.graphics.Shape = MaterialTheme.shapes.medium,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(size),
        enabled = enabled,
        shape = shape,
        contentPadding = PaddingValues(0.dp), // Remove default padding
        colors = ButtonDefaults.buttonColors(
             containerColor = containerColor,
             contentColor = contentColor,
             disabledContainerColor = containerColor.copy(alpha = 0.5f), // Adjust disabled appearance
             disabledContentColor = contentColor.copy(alpha = 0.5f)
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    NextSnakeTheme {
        // Provide a dummy ViewModel or state for preview
        val previewViewModel = GameViewModel()
        // You might want to set a specific state for preview
        // previewViewModel._gameState.value = GameState(...)
        GameScreen(gameViewModel = previewViewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun GameControlsPreview() {
    NextSnakeTheme {
        GameControls(
            isRunning = true,
            isGameOver = false,
            onDirectionChange = {},
            onPlayPauseToggle = {},
            onRestart = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameControlsGameOverPreview() {
    NextSnakeTheme {
        GameControls(
            isRunning = false, // Usually false when game over
            isGameOver = true,
            onDirectionChange = {}, // Should be disabled visually too
            onPlayPauseToggle = {}, // Action changes to restart
            onRestart = {}
        )
    }
}
