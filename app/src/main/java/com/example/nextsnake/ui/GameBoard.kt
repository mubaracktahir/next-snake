package com.example.nextsnake.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.nextsnake.model.Constants
import com.example.nextsnake.model.Coordinate
import com.example.nextsnake.model.Direction
import com.example.nextsnake.model.GameState
import com.example.nextsnake.ui.theme.*

@Composable
fun GameBoard(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .border(2.dp, MaterialTheme.colorScheme.primary) // Add border around the board
    ) {
        val cellSize = maxWidth / Constants.BOARD_SIZE

        // Draw Grid Lines (Optional)
        // DrawGrid(cellSize)

        // Draw Food
        GameElement(
            coordinate = gameState.food,
            cellSize = cellSize,
            color = MaterialTheme.colorScheme.secondary, // Red color for food
            shape = CircleShape // Make food circular
        )

        // Draw Snake
        gameState.snake.forEachIndexed { index, coordinate ->
            GameElement(
                coordinate = coordinate,
                cellSize = cellSize,
                color = MaterialTheme.colorScheme.primary, // Dark Green for snake
                // Optional: Different shape/color for the head
                // shape = if (index == 0) RoundedCornerShape(4.dp) else RectangleShape
            )
        }

         // Game Over Overlay
        if (gameState.isGameOver) {
             Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .background(GameOverOverlay), // Semi-transparent overlay
                 contentAlignment = Alignment.Center
             ) {
                 androidx.compose.material3.Text(
                     text = "Game Over!",
                     style = MaterialTheme.typography.headlineMedium,
                     color = Red, // Use food color for Game Over text
                     fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                 )
             }
         }
    }
}

@Composable
fun DrawGrid(cellSize: Dp) {
     val lightGray = Color.LightGray.copy(alpha = 0.5f)
    for (i in 0 until Constants.BOARD_SIZE) {
        // Vertical line
        Box(modifier = Modifier
            .offset(x = cellSize * i)
            .fillMaxHeight()
            .width(1.dp)
            .background(lightGray)
        )
        // Horizontal line
         Box(modifier = Modifier
            .offset(y = cellSize * i)
            .fillMaxWidth()
            .height(1.dp)
            .background(lightGray)
        )
    }
}


@Composable
fun GameElement(
    coordinate: Coordinate,
    cellSize: Dp,
    color: Color,
    shape: androidx.compose.ui.graphics.Shape = androidx.compose.ui.graphics.RectangleShape // Default to square
) {
    val density = LocalDensity.current
    val sizePx = with(density) { cellSize.toPx() }
    // Add a small padding inside the cell for visual separation
    val padding = (sizePx * 0.08f).coerceAtLeast(1f) // 8% padding, minimum 1 pixel
    val elementSize = with(density) { (sizePx - 2 * padding).toDp() }

    Box(
        modifier = Modifier
            .offset(x = coordinate.x * cellSize + with(density) { padding.toDp() },
                    y = coordinate.y * cellSize + with(density) { padding.toDp() } )
            .size(elementSize)
            .clip(shape) // Apply the shape
            .background(color)
    )
}

// Preview for the GameBoard
@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun GameBoardPreview() {
    NextSnakeTheme {
        val previewState = GameState(
            snake = listOf(Coordinate(5, 5), Coordinate(4, 5), Coordinate(3, 5)),
            food = Coordinate(10, 10),
             direction = Direction.RIGHT,
             score = 3,
            isGameOver = false,
            isRunning = true
        )
        GameBoard(gameState = previewState, modifier = Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun GameBoardGameOverPreview() {
    NextSnakeTheme {
        val previewState = GameState(
            snake = listOf(Coordinate(5, 5), Coordinate(4, 5), Coordinate(3, 5)),
            food = Coordinate(10, 10),
             direction = Direction.RIGHT,
             score = 3,
             isGameOver = true, // Game Over state
             isRunning = false
        )
        GameBoard(gameState = previewState, modifier = Modifier.fillMaxSize())
    }
}
