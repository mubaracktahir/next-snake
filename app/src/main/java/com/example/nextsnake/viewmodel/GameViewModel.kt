package com.example.nextsnake.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextsnake.model.Constants
import com.example.nextsnake.model.Coordinate
import com.example.nextsnake.model.Direction
import com.example.nextsnake.model.GameState
import com.example.nextsnake.model.generateRandomFood
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max

class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var gameJob: Job? = null
    private var currentDirection = Direction.RIGHT

    init {
        // Optional: Start paused or running
        // startGameLoop()
    }

    fun startGame() {
        if (_gameState.value.isGameOver) {
            resetGame()
        }
        if (!_gameState.value.isRunning) {
             _gameState.update { it.copy(isRunning = true) }
            currentDirection = _gameState.value.direction // Ensure direction is correct on start/resume
            startGameLoop()
        }
    }

    fun pauseGame() {
        _gameState.update { it.copy(isRunning = false) }
        gameJob?.cancel()
    }

    fun togglePlayPause() {
        if (_gameState.value.isRunning) {
            pauseGame()
        } else {
            startGame()
        }
    }


    fun resetGame() {
        gameJob?.cancel()
        val initialState = GameState()
        _gameState.value = initialState
        currentDirection = initialState.direction
        // Optionally restart loop immediately or wait for user action
        // startGame()
    }

    fun changeDirection(newDirection: Direction) {
       val current = currentDirection // Use the buffered direction
        // Prevent moving directly opposite
        if (_gameState.value.isRunning) {
             if ((newDirection == Direction.UP && current != Direction.DOWN) ||
                (newDirection == Direction.DOWN && current != Direction.UP) ||
                (newDirection == Direction.LEFT && current != Direction.RIGHT) ||
                (newDirection == Direction.RIGHT && current != Direction.LEFT)) {
                currentDirection = newDirection
            }
        }
    }

    private fun startGameLoop() {
        gameJob?.cancel() // Cancel any existing loop
        gameJob = viewModelScope.launch {
            while (_gameState.value.isRunning && !_gameState.value.isGameOver) {
                delay(_gameState.value.tickDelayMs)
                moveSnake()
            }
        }
    }

    private fun moveSnake() {
        _gameState.update { currentState ->
            if (!currentState.isRunning || currentState.isGameOver) return@update currentState

             // Update direction for the next move calculation based on buffered input
            val actualDirection = currentDirection
            _gameState.update { it.copy(direction = actualDirection) }


            val head = currentState.snake.first()
            val newHead = when (actualDirection) {
                Direction.UP -> head.copy(y = (head.y - 1 + Constants.BOARD_SIZE) % Constants.BOARD_SIZE)
                Direction.DOWN -> head.copy(y = (head.y + 1) % Constants.BOARD_SIZE)
                Direction.LEFT -> head.copy(x = (head.x - 1 + Constants.BOARD_SIZE) % Constants.BOARD_SIZE)
                Direction.RIGHT -> head.copy(x = (head.x + 1) % Constants.BOARD_SIZE)
            }

            // Check for game over conditions
            if (currentState.snake.contains(newHead)) {
                 pauseGame() // Stop the loop immediately
                return@update currentState.copy(isGameOver = true, isRunning = false)
            }

            // Check for food consumption
            val ateFood = newHead == currentState.food
            val newSnake = if (ateFood) {
                listOf(newHead) + currentState.snake
            } else {
                listOf(newHead) + currentState.snake.dropLast(1)
            }

            val newScore = if (ateFood) currentState.score + 1 else currentState.score
            val newFood = if (ateFood) generateRandomFood(newSnake) else currentState.food
            val newDelay = if (ateFood) {
                max(Constants.MIN_TICK_DELAY_MS, (currentState.tickDelayMs * Constants.SPEED_INCREMENT_FACTOR).toLong())
            } else {
                currentState.tickDelayMs
            }


            currentState.copy(
                snake = newSnake,
                food = newFood,
                score = newScore,
                isGameOver = false, // Ensure it's false if we reached here
                tickDelayMs = newDelay
            )
        }
    }

     override fun onCleared() {
        super.onCleared()
        gameJob?.cancel() // Ensure coroutine is cancelled when ViewModel is cleared
    }
}
