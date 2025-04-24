'use client';

import { useState, useEffect, useCallback } from 'react';
import type { Coordinate, Direction } from '@/lib/types';
import {
  GRID_SIZE,
  CELL_SIZE, // <-- Import CELL_SIZE
  INITIAL_SNAKE_POSITION,
  INITIAL_DIRECTION,
  GAME_SPEED_MS,
} from '@/lib/constants';
import { useToast } from "@/hooks/use-toast";

const getRandomCoordinate = (exclude: Coordinate[] = []): Coordinate => {
  let newCoord: Coordinate;
  do {
    newCoord = {
      x: Math.floor(Math.random() * GRID_SIZE),
      y: Math.floor(Math.random() * GRID_SIZE),
    };
  } while (exclude.some(coord => coord.x === newCoord.x && coord.y === newCoord.y));
  return newCoord;
};

export const useSnakeGame = () => {
  const [snake, setSnake] = useState<Coordinate[]>(INITIAL_SNAKE_POSITION);
  const [food, setFood] = useState<Coordinate>(getRandomCoordinate(INITIAL_SNAKE_POSITION));
  const [direction, setDirection] = useState<Direction>(INITIAL_DIRECTION);
  const [score, setScore] = useState<number>(0);
  const [isGameOver, setIsGameOver] = useState<boolean>(false);
  const [isRunning, setIsRunning] = useState<boolean>(false);
  const [directionQueue, setDirectionQueue] = useState<Direction[]>([]);

  const { toast } = useToast();

  const resetGame = useCallback(() => {
    setSnake(INITIAL_SNAKE_POSITION);
    setFood(getRandomCoordinate(INITIAL_SNAKE_POSITION));
    setDirection(INITIAL_DIRECTION);
    setDirectionQueue([]);
    setScore(0);
    setIsGameOver(false);
    setIsRunning(true); // Start game immediately on reset
  }, []);

  const handleKeyDown = useCallback((event: KeyboardEvent) => {
    let newDirection: Direction | null = null;
    switch (event.key) {
      case 'ArrowUp':
        if (direction !== 'DOWN') newDirection = 'UP';
        break;
      case 'ArrowDown':
        if (direction !== 'UP') newDirection = 'DOWN';
        break;
      case 'ArrowLeft':
        if (direction !== 'RIGHT') newDirection = 'LEFT';
        break;
      case 'ArrowRight':
        if (direction !== 'LEFT') newDirection = 'RIGHT';
        break;
      case ' ': // Space bar to start/reset
        if (isGameOver || !isRunning) {
             resetGame();
        }
        break;
       case 'p': // Pause toggle
       case 'P':
          setIsRunning(prev => !prev);
          break;

    }

    if (newDirection) {
       setDirectionQueue(prev => {
          const lastQueued = prev[prev.length - 1] ?? direction;
          // Prevent queuing opposite direction immediately after current
          if (
            (newDirection === 'UP' && lastQueued !== 'DOWN') ||
            (newDirection === 'DOWN' && lastQueued !== 'UP') ||
            (newDirection === 'LEFT' && lastQueued !== 'RIGHT') ||
            (newDirection === 'RIGHT' && lastQueued !== 'LEFT')
          ) {
              // Limit queue size to prevent too many buffered moves
               return [...prev, newDirection].slice(-2);
          }
          return prev;
       });
    }
  }, [direction, isGameOver, isRunning, resetGame]);


  useEffect(() => {
    if (!isRunning || isGameOver) return;

    const moveSnake = () => {
       let currentDirection = direction;
       if (directionQueue.length > 0) {
            const nextDirection = directionQueue[0];
             setDirectionQueue(prev => prev.slice(1));
             setDirection(nextDirection);
             currentDirection = nextDirection;
       }


      setSnake((prevSnake) => {
        const head = { ...prevSnake[0] };

        switch (currentDirection) {
          case 'UP':
            head.y -= 1;
            break;
          case 'DOWN':
            head.y += 1;
            break;
          case 'LEFT':
            head.x -= 1;
            break;
          case 'RIGHT':
            head.x += 1;
            break;
        }

        // Wall collision check
        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE) {
          setIsGameOver(true);
          setIsRunning(false);
          toast({ title: "Game Over!", description: `Wall collision! Final Score: ${score}`, variant: "destructive" });
          return prevSnake;
        }

        // Self collision check
        for (let i = 1; i < prevSnake.length; i++) {
          if (prevSnake[i].x === head.x && prevSnake[i].y === head.y) {
            setIsGameOver(true);
            setIsRunning(false);
             toast({ title: "Game Over!", description: `Ate yourself! Final Score: ${score}`, variant: "destructive" });
            return prevSnake;
          }
        }

        const newSnake = [head, ...prevSnake];

        // Food consumption check
        if (head.x === food.x && head.y === food.y) {
          setScore((prevScore) => prevScore + 1);
          setFood(getRandomCoordinate(newSnake)); // Pass newSnake to avoid placing food on the growing snake
            toast({ title: "Yum!", description: `Score: ${score + 1}`, className: "bg-accent text-accent-foreground border-accent" });
        } else {
          newSnake.pop(); // Remove tail if no food eaten
        }

        return newSnake;
      });
    };

    const gameInterval = setInterval(moveSnake, GAME_SPEED_MS);

    return () => clearInterval(gameInterval);
  }, [snake, direction, food, score, isGameOver, isRunning, toast, directionQueue]);


  useEffect(() => {
    window.addEventListener('keydown', handleKeyDown);
    return () => {
      window.removeEventListener('keydown', handleKeyDown);
    };
  }, [handleKeyDown]); // Re-attach listener if handleKeyDown changes

  return {
    snake,
    food,
    score,
    isGameOver,
    isRunning,
    direction,
    gridSize: GRID_SIZE,
    cellSize: CELL_SIZE,
    startGame: () => setIsRunning(true),
    pauseGame: () => setIsRunning(false),
    resetGame,
  };
};
