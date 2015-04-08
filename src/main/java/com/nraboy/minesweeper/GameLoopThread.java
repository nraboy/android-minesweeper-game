package com.nraboy.minesweeper;

import android.graphics.*;

public class GameLoopThread extends Thread {

    private GameView gameView;
    private boolean isRunning;

    public GameLoopThread(GameView gameView) {
        this.gameView = gameView;
        this.isRunning = false;
    }

    /*
     * Start or stop the thread which does all of our drawing
     *
     * @param    boolean isRunning
     * @return   void
     */
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    /*
     * Lock the drawable canvas to draw graphics to the screen, then release the lock
     * when all drawing has completed
     *
     * @param
     * @return   void
     */
    @Override
    public void run() {
        // When the thread stops, so do our renders
        while(this.isRunning) {
            Canvas canvas = null;
            try {
                canvas = this.gameView.getHolder().lockCanvas();
                synchronized(this.gameView.getHolder()) {
                    if(canvas != null) {
                        this.gameView.draw(canvas);
                    }
                }
            } finally {
                if (canvas != null) {
                    this.gameView.getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
