package com.example.nextsnake.model

import kotlin.random.Random

data class GameState(
    val snake: List<Coordinate> = listOf(Coordinate(Constants.BOARD_SIZE / 2, Constants.BOARD_SIZE / 2)),
    val food: Coordinate = generateRandomFood(snake),
    val direction: Direction = Direction.RIGHT,
    val score: Int = 0,
    val isGameOver: Boolean = false,
    val isRunning: Boolean = false, // To handle pause/resume
    val tickDelayMs: Long = Constants.INITIAL_TICK_DELAY_MS
)

fun generateRandomFood(snake: List<Coordinate>): Coordinate {
    var foodPosition: Coordinate
    do {
        foodPosition = Coordinate(
            x = Random.nextInt(Constants.BOARD_SIZE),
            y = Random.nextInt(Constants.BOARD_SIZE)
        )
    } while (snake.contains(foodPosition))
    return foodPosition
}
