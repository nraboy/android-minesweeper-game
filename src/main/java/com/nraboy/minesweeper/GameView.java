package com.nraboy.minesweeper;

import android.content.*;
import android.view.*;
import android.graphics.*;

public class GameView extends SurfaceView {

    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    public Context context;
    private Board gameBoard;
    private Game game;
    private long lastClick;

    public GameView(Context context) {
        super(context);
        this.context = context;
        this.gameLoopThread = new GameLoopThread(this);
        this.game = new Game(this, 8, 10);              // The game will be 8x8 with 10 bombs
        this.game.start();
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            /*
             * If the game is closed, the view needs to be gracefully destroyed. This
             * means that the rendering thread must be stopped so it doesn't chew through
             * the device battery or throw errors while the game is closed
             *
             * @param    SurfaceHolder holder
             * @return   void
             */
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = false;
                gameLoopThread.setRunning(false);
                while(retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            /*
             * As soon as the drawable surface is created, activate the thread that will
             * be in charge of all rendering
             *
             * @param    SurfaceHolder holder
             * @return   void
             */
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            /*
             * Not used in our code, but required to prevent errors.  Just detects changes in the
             * SurfaceView
             *
             * @param    SurfaceHolder holder
             * @param    int format
             * @param    int width
             * @param    int height
             * @return   void
             */
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

        });
    }

    /*
     * All sprite components to be drawn in the game.  This includes anything from HUD
     * components to board cells.  If it inherits the Sprite class, it should probably be
     * in this method
     *
     * @param    Canvas canvas
     * @return   void
     */
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        this.game.draw(canvas);
        this.game.gameBoard.draw(canvas);
    }


    /*
     * Detect click events on the canvas.  Time restrictions are placed on the click to prevent
     * like a million clicks from being registered if it is held too long.  In other words, it
     * is to prevent accidental clicks
     *
     * @param    MotionEvent event
     * @return   boolean clicked
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(System.currentTimeMillis() - lastClick > 500) {
            lastClick = System.currentTimeMillis();
            synchronized (getHolder()) {
                this.game.registerTouch(event);
            }
        }
        return true;
    }

}
