package com.nraboy.minesweeper;

import android.view.*;
import android.widget.*;
import android.graphics.*;

public class Game {

    private GameView gameView;
    public Board gameBoard;
    private Bitmap hudSpriteSheet;
    private int boardSize;
    private int bombCount;
    private boolean isGameOver;
    private int score;
    public HUD[] hud;

    /*
     * Initialize various game components in the constructor.  Record the size of the board,
     * how many bombs must exist on it, and also prepare various HUD components.
     *
     * @param    GameView gameView
     * @param    int boardSize
     * @param    int bombCount
     */
    public Game(GameView gameView, int boardSize, int bombCount) {
        this.gameView = gameView;
        this.boardSize = boardSize;
        this.bombCount = bombCount;
        this.gameBoard = new Board(gameView, boardSize, bombCount);
        this.hud = new HUD[3];
        this.hudSpriteSheet = BitmapFactory.decodeResource(this.gameView.context.getResources(), R.drawable.hud_spritesheet_md);
        this.hud[0] = new HUD(this.gameView, this.hudSpriteSheet, 0, 0);
        this.hud[1] = new HUD(this.gameView, this.hudSpriteSheet, 160, 0);
        this.hud[2] = new HUD(this.gameView, this.hudSpriteSheet, 80, 40);
    }

    /*
     * Start a new game by resetting the board and clearing the game over indicator
     *
     * @param
     * @return   void
     */
    public void start() {
        this.isGameOver = false;
        this.score = 0;
        this.gameBoard.reset();
    }

    /*
     * Cheat in the game by marking one of the known bombs with a flag
     *
     * @param
     * @return   void
     */
    public void cheat() {
        outerLoop:
        for(int i = 0; i < this.boardSize; i++) {
            for(int j = 0; j < this.boardSize; j++) {
                if(!this.gameBoard.grid[i][j].isRevealed && this.gameBoard.grid[i][j].isBomb) {
                    this.gameBoard.grid[i][j].isCheat = true;
                    this.gameBoard.grid[i][j].reveal();
                    break outerLoop;
                }
            }
        }
    }

    /*
     * Game over has ocurred because we hit a bomb.  Show all bombs that were remaining and set
     * the game over indicator to true so that way further cells cannot be unlocked
     *
     * @param
     * @return   void
     */
    public void gameOver() {
        this.isGameOver = true;
        for(int i = 0; i < this.boardSize; i++) {
            for(int j = 0; j < this.boardSize; j++) {
                this.gameBoard.showBombs(this.gameBoard.grid[i][j]);
            }
        }
        Toast.makeText(this.gameView.context, "Game over!", Toast.LENGTH_LONG).show();
    }

    /*
     * The game was completed without triggering a bomb.  Lock the game and present a victory message
     *
     * @param
     * @return   void
     */
    public void gameFinished() {
        this.isGameOver = true;
        Toast.makeText(this.gameView.context, "You've beat the game!", Toast.LENGTH_LONG).show();
    }

    /*
     * Manually validate to see if the game was won.  If the game truly was not completed, it is
     * instant game over
     *
     * @param
     * @return   void
     */
    public void validate() {
        if(this.score == (this.boardSize * this.boardSize) - this.bombCount) {
            this.gameFinished();
        } else {
            this.gameOver();
        }
    }

    /*
     * Draw the two HUD components to the screen.  The two components being a new game
     * and cheat button in a sprite sheet
     *
     * @param    Canvas canvas
     * @return   void
     */
    public void draw(Canvas canvas) {
        this.hud[0].onDraw(canvas, 0, 0);
        this.hud[1].onDraw(canvas, 0, 1);
        this.hud[2].onDraw(canvas, 0, 2);
    }

    /*
     * Check for touch collisions on any of the components in our game.  Components are anything
     * from HUD elements to board cells.  If a touch event collides with the new game button, then start
     * a new game.  If a touch event collides with the cheat button, then cheat.  However, things get
     * more complex with board cells because they are in an array.  On every touch event, check to see if
     * any of the board cells were hit.  If a bomb is touched or the score matches the max possible, then
     * end the game
     *
     * @param    MotionEvent event
     * @return   void
     */
    public void registerTouch(MotionEvent event) {
        if(this.hud[0].hasCollided(event.getX(), event.getY())) {
            this.start();
        }
        if(this.hud[1].hasCollided(event.getX(), event.getY())) {
            this.cheat();
        }
        if(this.hud[2].hasCollided(event.getX(), event.getY())) {
            this.validate();
        }
        if(!this.isGameOver) {
            for(int i = 0; i < this.boardSize; i++) {
                for(int j = 0; j < this.boardSize; j++) {
                    if(this.gameBoard.grid[i][j].hasCollided(event.getX(), event.getY())) {
                        // If the cell touched was a bomb, then game over, otherwise check the score and see if we've won
                        if(this.gameBoard.reveal(this.gameBoard.grid[i][j])) {
                            this.gameOver();
                        } else {
                            this.score = this.gameBoard.getRevealedCount();
                            if(this.score == (this.boardSize * this.boardSize) - this.bombCount) {
                                this.gameFinished();
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

}
