package com.example.nextsnake.model

data class GameState(
    val snake: List<Coordinate> = listOf(Coordinate(Constants.GRID_SIZE / 2, Constants.GRID_SIZE / 2)),
    val food: Coordinate = Coordinate.random(Constants.GRID_SIZE, snake),
    val direction: Direction = Direction.RIGHT,
    val score: Int = 0,
    val isGameOver: Boolean = false,
    val isRunning: Boolean = false,
    val gridSize: Int = Constants.GRID_SIZE,
    val cellSize: Int = Constants.CELL_SIZE // Keep cellSize if needed for drawing calculations
)
