'use client';

import React from 'react';
import type { Coordinate } from '@/lib/types';
import { cn } from '@/lib/utils';

interface GameBoardProps {
  gridSize: number;
  cellSize: number;
  snake: Coordinate[];
  food: Coordinate;
  isGameOver: boolean;
}

const GameBoard: React.FC<GameBoardProps> = ({
  gridSize,
  cellSize,
  snake,
  food,
  isGameOver,
}) => {
  const boardWidth = gridSize * cellSize;
  const boardHeight = gridSize * cellSize;

  return (
    <div
      className={cn(
        "relative border border-muted bg-background shadow-md",
        "grid", // Use CSS Grid for layout
        `grid-cols-${gridSize}` // Dynamically set grid columns based on gridSize
      )}
      style={{
        width: `${boardWidth}px`,
        height: `${boardHeight}px`,
        gridTemplateColumns: `repeat(${gridSize}, minmax(0, 1fr))`, // Explicit grid template columns
        gridTemplateRows: `repeat(${gridSize}, minmax(0, 1fr))`, // Explicit grid template rows
      }}
      aria-label="Snake game board"
    >
      {/* Render Grid Cells (optional, for visual grid lines) */}
       {Array.from({ length: gridSize * gridSize }).map((_, index) => (
        <div key={index} className="border border-muted/20"></div>
       ))}

      {/* Render Snake */}
      {snake.map((segment, index) => (
        <div
          key={`snake-${index}-${segment.x}-${segment.y}`}
          className={cn(
             "absolute rounded-sm transition-all duration-100 ease-linear border border-primary/30", // Slightly more rounded, add border
             // Apply gradient for a more 'realistic' look
             "bg-gradient-to-b from-primary via-green-600 to-primary/80",
             // Head styling (optional: could be slightly different gradient or brighter)
             index === 0 && "z-10 shadow-lg from-green-500 via-primary to-green-700",
             // Game over styling overrides regular style
             isGameOver && "bg-gradient-to-b from-destructive/70 to-destructive/50 border-destructive/50"
          )}
          style={{
            left: `${segment.x * cellSize}px`,
            top: `${segment.y * cellSize}px`,
            width: `${cellSize}px`,
            height: `${cellSize}px`,
          }}
          aria-label={`Snake segment at ${segment.x}, ${segment.y}`}
        />
      ))}

      {/* Render Food */}
      <div
        // Keep food simple red circle/square
        className="absolute bg-secondary rounded-sm shadow-md animate-pulse border border-red-800/50"
        style={{
          left: `${food.x * cellSize}px`,
          top: `${food.y * cellSize}px`,
          width: `${cellSize}px`,
          height: `${cellSize}px`,
        }}
        aria-label={`Food at ${food.x}, ${food.y}`}
      />

      {/* Game Over Overlay */}
      {isGameOver && (
        <div className="absolute inset-0 bg-black/60 flex items-center justify-center text-destructive-foreground z-20 rounded-md">
          <div className="text-center p-6 bg-destructive rounded-lg shadow-xl">
              <h2 className="text-4xl font-bold mb-2">Game Over!</h2>
              <p className="text-lg">Press Space to Play Again</p>
          </div>
        </div>
      )}
    </div>
  );
};

export default GameBoard;
