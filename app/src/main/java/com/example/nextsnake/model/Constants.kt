package com.example.nextsnake.model

object Constants {
    const val BOARD_SIZE = 20 // Number of cells in width and height
    const val INITIAL_TICK_DELAY_MS = 300L // Initial game speed
    const val MIN_TICK_DELAY_MS = 100L // Fastest game speed
    const val SPEED_INCREMENT_FACTOR = 0.95f // Speed increase factor on eating food
}
