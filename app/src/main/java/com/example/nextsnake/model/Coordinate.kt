package com.example.nextsnake.model

import kotlin.random.Random

data class Coordinate(val x: Int, val y: Int) {
    companion object {
        fun random(gridSize: Int, exclude: List<Coordinate> = emptyList()): Coordinate {
            var newCoord: Coordinate
            do {
                newCoord = Coordinate(
                    x = Random.nextInt(gridSize),
                    y = Random.nextInt(gridSize)
                )
            } while (exclude.contains(newCoord))
            return newCoord
        }
    }
}
