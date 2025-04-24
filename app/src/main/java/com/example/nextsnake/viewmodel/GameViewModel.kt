package com.example.nextsnake.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextsnake.model.Constants
import com.example.nextsnake.model.Coordinate
import com.example.nextsnake.model.Direction
import com.example.nextsnake.model.GameState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var gameLoopJob: Job? = null
    private var directionQueue = mutableListOf<Direction>()

    fun startGame() {
        if (_gameState.value.isRunning || _gameState.value.isGameOver) {
            resetGame() // If already running or over, reset first
        }
        _gameState.update { it.copy(isRunning = true, isGameOver = false) }
        startGameLoop()
    }

    fun pauseGame() {
        _gameState.update { it.copy(isRunning = false) }
        gameLoopJob?.cancel()
    }

    fun resumeGame() {
        if (!_gameState.value.isGameOver) {
            _gameState.update { it.copy(isRunning = true) }
            startGameLoop()
        }
    }

    fun resetGame() {
        gameLoopJob?.cancel()
        directionQueue.clear()
        _gameState.value = GameState(food = Coordinate.random(Constants.GRID_SIZE)) // Generate initial food
        // Optionally start game immediately after reset
        // startGame()
    }

     fun changeDirection(newDirection: Direction) {
        val currentDirection = directionQueue.lastOrNull() ?: _gameState.value.direction
        if (isValidDirectionChange(currentDirection, newDirection)) {
             if (directionQueue.size < 2) { // Limit queue size
                 directionQueue.add(newDirection)
             }
        }
    }

    private fun isValidDirectionChange(current: Direction, new: Direction): Boolean {
        return when (current) {
            Direction.UP -> new != Direction.DOWN
            Direction.DOWN -> new != Direction.UP
            Direction.LEFT -> new != Direction.RIGHT
            Direction.RIGHT -> new != Direction.LEFT
        }
    }

    private fun startGameLoop() {
        gameLoopJob?.cancel() // Ensure only one loop runs
        gameLoopJob = viewModelScope.launch {
            while (_gameState.value.isRunning && !_gameState.value.isGameOver) {
                delay(Constants.GAME_SPEED_MS)
                moveSnake()
            }
        }
    }

    private fun moveSnake() {
        _gameState.update { currentState ->
            if (!currentState.isRunning || currentState.isGameOver) return@update currentState

            val currentDirection = if (directionQueue.isNotEmpty()) {
                 directionQueue.removeAt(0)
             } else {
                 currentState.direction
             }


            val currentSnake = currentState.snake
            val head = currentSnake.first()
            val newHead = when (currentDirection) {
                Direction.UP -> head.copy(y = head.y - 1)
                Direction.DOWN -> head.copy(y = head.y + 1)
                Direction.LEFT -> head.copy(x = head.x - 1)
                Direction.RIGHT -> head.copy(x = head.x + 1)
            }

            // Check for wall collisions
            if (newHead.x < 0 || newHead.x >= currentState.gridSize || newHead.y < 0 || newHead.y >= currentState.gridSize) {
                return@update currentState.copy(isGameOver = true, isRunning = false)
            }

            // Check for self collisions
            if (currentSnake.drop(1).contains(newHead)) {
                 return@update currentState.copy(isGameOver = true, isRunning = false)
            }


            val newSnake = mutableListOf(newHead)
            newSnake.addAll(currentSnake)

            // Check for food consumption
            if (newHead == currentState.food) {
                val newScore = currentState.score + 1
                val newFood = Coordinate.random(currentState.gridSize, newSnake)
                currentState.copy(
                    snake = newSnake, // Keep the grown snake
                    food = newFood,
                    score = newScore,
                    direction = currentDirection // Update direction in state
                )
            } else {
                newSnake.removeLast() // Remove tail if no food is eaten
                currentState.copy(
                    snake = newSnake,
                    direction = currentDirection // Update direction in state
                )
            }
        }
    }
}
