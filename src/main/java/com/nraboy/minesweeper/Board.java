package com.nraboy.minesweeper;

import java.util.*;
import android.graphics.*;
import android.content.*;
import android.view.*;

public class Board {

    public Cell[][] grid;
    private GameView gameView;
    private Bitmap cellSpriteSheet;
    private int boardSize;
    private int bombCount;
    private int cellsRevealed;

    public Board(GameView gameView, int boardSize, int bombCount) {
        this.grid = new Cell[boardSize][boardSize];
        this.gameView = gameView;
        this.cellSpriteSheet = BitmapFactory.decodeResource(this.gameView.context.getResources(), R.drawable.cell_spritesheet_md);
        this.boardSize = boardSize;
        this.bombCount = bombCount;
    }

    /*
     * Draw a particular sprite from the sprite sheet to the screen.  If the cell has
     * not yet been revealed, show the blank sprite.  If the cell has been revealed and is
     * an indication of cheating, show a flag sprite.  If the cell has been revealed, show a
     * bomb.  Otherwise, show the number of neighbors that contain bombs in the cell.
     *
     * @param    Canvas canvas
     * @return   void
     */
    public void draw(Canvas canvas) {
        for(int i = 0; i < this.grid.length; i++) {
            for(int j = 0; j < this.grid.length; j++) {
                if(this.grid[i][j].isRevealed) {
                    if(this.grid[i][j].isCheat) {
                        this.grid[i][j].onDraw(canvas, 2, 3);
                    } else if(this.grid[i][j].isBomb) {
                        this.grid[i][j].onDraw(canvas, 1, 0);
                    } else {
                        switch(this.grid[i][j].getBombNeighborCount()) {
                            case 0:
                                this.grid[i][j].onDraw(canvas, 2, 0);
                                break;
                            case 1:
                                this.grid[i][j].onDraw(canvas, 0, 1);
                                break;
                            case 2:
                                this.grid[i][j].onDraw(canvas, 1, 1);
                                break;
                            case 3:
                                this.grid[i][j].onDraw(canvas, 2, 1);
                                break;
                            case 4:
                                this.grid[i][j].onDraw(canvas, 0, 2);
                                break;
                            case 5:
                                this.grid[i][j].onDraw(canvas, 1, 2);
                                break;
                            case 6:
                                this.grid[i][j].onDraw(canvas, 2, 2);
                                break;
                            case 7:
                                this.grid[i][j].onDraw(canvas, 0, 3);
                                break;
                            case 8:
                                this.grid[i][j].onDraw(canvas, 1, 3);
                                break;
                            default:
                                this.grid[i][j].onDraw(canvas, 0, 0);
                                break;
                        }
                    }
                } else {
                    this.grid[i][j].onDraw(canvas, 0, 0);
                }
            }
        }
    }

    /*
     * Reset the board to the initial state.  This can act the same as an initialization function.
     * Resetting involves creating a new board, and placing bombs in new positions.
     *
     * @param
     * @return   void
     */
    public void reset() {
        for(int i = 0; i < this.grid.length; i++) {
            for(int j = 0; j < this.grid.length; j++) {
                this.grid[i][j] = new Cell(this.gameView, this.cellSpriteSheet, i, j, false);
            }
        }
        this.shuffleBombs(this.bombCount);
        this.calculateCellNeighbors();
        this.setPositions();
        this.cellsRevealed = 0;
    }

    /*
     * Pick random locations for bombs to appear in the board grid.  If the grid location
     * already contains a bomb, loop until it finds a spot that is available.
     *
     * @param    int bombCount
     * @return   void
     */
    public void shuffleBombs(int bombCount) {
        boolean spotAvailable = true;
        Random random = new Random();
        int row;
        int column;
        for(int i = 0; i < bombCount; i++) {
            do {
                column = random.nextInt(8);
                row = random.nextInt(8);
                spotAvailable = this.grid[column][row].isBomb;
            } while (spotAvailable);
            this.grid[column][row].isBomb = true;
        }
    }

    /*
     * Determine all the cells that touch a particular cell
     *
     * @param
     * @return   void
     */
    public void calculateCellNeighbors() {
        for(int x = 0; x < this.grid.length; x++) {
            for(int y = 0; y < this.grid.length; y++) {
                for(int i = this.grid[x][y].getX() - 1; i <= this.grid[x][y].getX() + 1; i++) {
                    for(int j = this.grid[x][y].getY() - 1; j <= this.grid[x][y].getY() + 1; j++) {
                        if(i >= 0 && i < this.grid.length && j >= 0 && j < this.grid.length) {
                            this.grid[x][y].addNeighbor(this.grid[i][j]);
                        }
                    }
                }
            }
        }
    }

    /*
     * Set the position of each cell on the board.  To make things a little more pleasing visually,
     * determine an offset so that the grid is a little more centered in the screen
     *
     * @param
     * @return   void
     */
    public void setPositions() {
        int horizontalOffset = (320 - (this.boardSize * 25)) / 2;
        for(int i = 0; i < this.grid.length; i++) {
            for(int j = 0; j < this.grid.length; j++) {
                this.grid[i][j].setX(horizontalOffset + i * 25);
                this.grid[i][j].setY(90 + j * 25);
            }
        }
    }

    /*
     * Reveal a particular cell on the board.  If it is not a bomb, check to see if it has zero
     * neighbors that are bombs.  Reveal all cells recursively that are neighbors until it finds
     * a neighboring cell that is a bomb and stop.
     *
     * @param    Cell c
     * @return   boolean bomb
     */
    public boolean reveal(Cell c) {
        c.reveal();
        if(!c.isBomb) {
            this.cellsRevealed++;
            if(c.getBombNeighborCount() == 0) {
                ArrayList<Cell> neighbors = c.getNeighbors();
                for(int i = 0; i < neighbors.size(); i++) {
                    if(!neighbors.get(i).isRevealed) {
                        reveal(neighbors.get(i));
                    }
                }
            }
        }
        return c.isBomb;
    }

    /*
     * Reveal a board cell only if it is a bomb.  This is useful for scenarios like game
     * over, where you might want to show the user all the bombs on the board
     *
     * @param    Cell c
     * @return   void
     */
    public void showBombs(Cell c) {
        if(c.isBomb) {
            c.reveal();
        }
    }

    /*
     * Return how many cells on the board have been revealed.  Useful for calculating score
     *
     * @param
     * @return   int count
     */
    public int getRevealedCount() {
        return this.cellsRevealed;
    }

}
