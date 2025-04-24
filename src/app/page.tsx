'use client';

import React from 'react';
import GameBoard from '@/components/game-board';
import { useSnakeGame } from '@/hooks/use-snake-game';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { ArrowUp, ArrowDown, ArrowLeft, ArrowRight, Play, Pause, RotateCcw } from 'lucide-react';

export default function Home() {
  const {
    snake,
    food,
    score,
    isGameOver,
    isRunning,
    gridSize,
    cellSize,
    startGame,
    pauseGame,
    resetGame,
    direction, // Added direction for display
  } = useSnakeGame();

  return (
    <main className="flex min-h-screen flex-col items-center justify-center p-4 md:p-12 bg-gradient-to-br from-background to-muted/30">
      <Card className="w-full max-w-lg mb-6 shadow-lg border-border">
        <CardHeader className="text-center">
          <CardTitle className="text-3xl font-bold text-primary">Next Snake</CardTitle>
          <CardDescription>Use arrow keys to move. Space to start/restart. P to pause.</CardDescription>
        </CardHeader>
        <CardContent className="flex justify-between items-center px-6 py-3 bg-muted/50 rounded-b-lg">
           <p className="text-lg font-semibold">
              Score: <span className="text-accent font-bold">{score}</span>
           </p>
            {/* Optional: Display current direction */}
           {/* <p className="text-sm text-muted-foreground">Direction: {direction}</p> */}
           <div className="flex gap-2">
             <Button onClick={resetGame} variant="outline" size="sm" aria-label="Reset Game">
               <RotateCcw className="mr-1 h-4 w-4"/> Reset
             </Button>
            {isRunning ? (
                <Button onClick={pauseGame} variant="secondary" size="sm" aria-label="Pause Game">
                    <Pause className="mr-1 h-4 w-4" /> Pause
                </Button>
            ) : (
                 <Button onClick={startGame} disabled={isGameOver} variant="default" size="sm" aria-label="Start Game">
                    <Play className="mr-1 h-4 w-4" /> Start
                 </Button>
            )}
           </div>
        </CardContent>
      </Card>

      <GameBoard
        gridSize={gridSize}
        cellSize={cellSize}
        snake={snake}
        food={food}
        isGameOver={isGameOver}
      />

       {/* Instructions / Key Info */}
       <div className="mt-6 text-center text-muted-foreground text-sm max-w-md">
           <p className="mb-2">
               <span className="font-semibold">Controls:</span>{' '}
                <ArrowUp className="inline h-4 w-4 mx-1" />
                <ArrowDown className="inline h-4 w-4 mx-1" />
                <ArrowLeft className="inline h-4 w-4 mx-1" />
                <ArrowRight className="inline h-4 w-4 mx-1" /> (Arrow Keys)
                 &nbsp;|&nbsp; <span className="font-mono">[SPACE]</span> Start/Restart
                 &nbsp;|&nbsp; <span className="font-mono">[P]</span> Pause/Resume
           </p>
           <p>Avoid walls and eating yourself!</p>
       </div>
    </main>
  );
}
