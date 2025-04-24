package com.example.nextsnake.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nextsnake.model.Constants
import com.example.nextsnake.model.Coordinate
import com.example.nextsnake.model.Direction
import com.example.nextsnake.model.GameState
import com.example.nextsnake.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsState()

    Scaffold(
        topBar = { GameTopBar(gameState = gameState, viewModel = viewModel) },
        bottomBar = { GameBottomBar(viewModel = viewModel, isRunning = gameState.isRunning) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            GameBoard(
                gameState = gameState,
                modifier = Modifier
                    .aspectRatio(1f) // Make the board square
                    .fillMaxWidth(0.9f) // Adjust width as needed
                    .border(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(gameState: GameState, viewModel: GameViewModel) {
    TopAppBar(
        title = { Text("Next Snake", fontWeight = FontWeight.Bold) },
        actions = {
            Text(
                text = "Score: ${gameState.score}",
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            IconButton(onClick = { viewModel.resetGame() }) {
                Icon(Icons.Filled.Refresh, contentDescription = "Reset Game")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun GameBottomBar(viewModel: GameViewModel, isRunning: Boolean) {
    BottomAppBar(
         containerColor = MaterialTheme.colorScheme.primaryContainer,
         contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
             // Central Play/Pause Button
             FloatingActionButton(
                 onClick = {
                     if (isRunning) viewModel.pauseGame() else viewModel.resumeGame()
                 },
                 containerColor = MaterialTheme.colorScheme.secondaryContainer,
                 contentColor = MaterialTheme.colorScheme.onSecondaryContainer
             ) {
                 Icon(
                     if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                     contentDescription = if (isRunning) "Pause" else "Play"
                 )
             }

            Spacer(Modifier.width(16.dp)) // Add some space

            // Directional Buttons in a Cross Layout
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ControlButton(icon = Icons.Filled.ArrowUpward, onClick = { viewModel.changeDirection(Direction.UP) })
                Row {
                    ControlButton(icon = Icons.Filled.ArrowBack, onClick = { viewModel.changeDirection(Direction.LEFT) })
                    Spacer(modifier = Modifier.width(60.dp)) // Space for the central button
                    ControlButton(icon = Icons.Filled.ArrowForward, onClick = { viewModel.changeDirection(Direction.RIGHT) })
                }
                 ControlButton(icon = Icons.Filled.ArrowDownward, onClick = { viewModel.changeDirection(Direction.DOWN) })
            }
        }
    }
}


@Composable
fun ControlButton(icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(50.dp),
        contentPadding = PaddingValues(0.dp) // Remove default padding
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(30.dp))
    }
}


@Composable
fun GameBoard(gameState: GameState, modifier: Modifier = Modifier) {
    val cellSizePx = with(LocalDensity.current) { Constants.CELL_SIZE.dp.toPx() }
    val snakeColor = MaterialTheme.colorScheme.primary
    val foodColor = MaterialTheme.colorScheme.secondary
    val headColor = MaterialTheme.colorScheme.tertiary // Different color for the head
    val gridColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val boardWidth = size.width
            val boardHeight = size.height
            val numCells = gameState.gridSize
            val actualCellSize = minOf(boardWidth / numCells, boardHeight / numCells) // Calculate cell size based on available space

            // Draw Grid (Optional)
            for (i in 0..numCells) {
                drawLine(
                    color = gridColor,
                    start = Offset(x = i * actualCellSize, y = 0f),
                    end = Offset(x = i * actualCellSize, y = boardHeight),
                    strokeWidth = 1f
                )
                drawLine(
                    color = gridColor,
                    start = Offset(x = 0f, y = i * actualCellSize),
                    end = Offset(x = boardWidth, y = i * actualCellSize),
                    strokeWidth = 1f
                )
            }

            // Draw Food
            drawRect(
                color = foodColor,
                topLeft = Offset(gameState.food.x * actualCellSize, gameState.food.y * actualCellSize),
                size = Size(actualCellSize, actualCellSize)
            )

            // Draw Snake
            gameState.snake.forEachIndexed { index, segment ->
                 val color = if (index == 0) headColor else snakeColor
                 drawRealisticSnakeSegment(
                     this,
                     segment,
                     actualCellSize,
                     color,
                     gameState.snake.getOrNull(index + 1), // Pass next segment for smooth drawing
                     gameState.direction // Pass current direction
                 )
            }
             // Game Over Overlay
             if (gameState.isGameOver) {
                 drawRect(
                     color = Color.Black.copy(alpha = 0.7f),
                     size = size
                 )
                 // You can draw text here using DrawScope.drawContext.canvas.nativeCanvas
                 // but it's generally preferred to use a Text composable overlaid on the Box
             }
        }
         // Game Over Text Overlay (preferred way)
         if (gameState.isGameOver) {
             Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .background(Color.Black.copy(alpha = 0.6f)) // Semi-transparent background
                     .pointerInput(Unit) { detectTapGestures(onTap = { /* Could add restart here */ }) }, // Optional: Tap to restart
                 contentAlignment = Alignment.Center
             ) {
                 Text(
                     "Game Over!",
                     color = MaterialTheme.colorScheme.error,
                     fontSize = 32.sp,
                     fontWeight = FontWeight.Bold
                 )
                 // Add score and restart instruction if needed
                  Text(
                      "\nScore: ${gameState.score}\nTap controls to restart",
                      color = Color.White,
                      fontSize = 18.sp,
                       modifier = Modifier.padding(top = 50.dp) // Add space below "Game Over"
                  )
             }
         }
    }
}


fun drawRealisticSnakeSegment(
    drawScope: DrawScope,
    segment: Coordinate,
    cellSize: Float,
    color: Color,
    nextSegment: Coordinate?, // To help determine direction for smoother connections
    currentDirection: Direction // Overall snake direction
) {
    val center = Offset(
        segment.x * cellSize + cellSize / 2,
        segment.y * cellSize + cellSize / 2
    )
    val radius = cellSize / 2.2f // Slightly smaller radius for spacing

    drawScope.drawCircle(
        color = color,
        radius = radius,
        center = center,
        style = Fill
    )

   // Add subtle gradient or shadow for depth (optional)
    val shadowColor = Color.Black.copy(alpha = 0.3f)
     drawScope.drawCircle(
         color = shadowColor,
         radius = radius * 1.05f, // Slightly larger shadow
         center = center.copy(x = center.x + 1, y = center.y + 1), // Offset shadow
         style = Fill
     )
      drawScope.drawCircle( // Redraw main segment on top of shadow
         color = color,
         radius = radius,
         center = center,
         style = Fill
     )

    // Attempt to draw smoother connections (simplified)
    // This part can be complex; a simple overlap might suffice visually
    if (nextSegment != null) {
        val nextCenter = Offset(
            nextSegment.x * cellSize + cellSize / 2,
            nextSegment.y * cellSize + cellSize / 2
        )
         val midPoint = Offset(
             (center.x + nextCenter.x) / 2,
             (center.y + nextCenter.y) / 2
         )
         // Draw a slightly larger rectangle/oval to connect the circles
         // This requires more complex geometry for perfect smoothness
         // For simplicity, we'll just let the circles overlap slightly
    }
}
