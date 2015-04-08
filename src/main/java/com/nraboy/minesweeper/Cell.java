package com.nraboy.minesweeper;

import java.util.*;
import android.graphics.*;

public class Cell extends Sprite {

    public boolean isRevealed;
    public boolean isBomb;
    public boolean isCheat;
    private ArrayList<Cell> neighbors;
    private int bombNeighborCount;

    public Cell(GameView gameView, Bitmap spriteSheet, int x, int y, boolean isBomb) {
        super(gameView, spriteSheet, x, y, 3, 4);
        this.isBomb = isBomb;
        this.isRevealed = false;
        this.isCheat = false;
        this.bombNeighborCount = 0;
        this.neighbors = new ArrayList<Cell>();
    }

    /*
     * Keep track of every neighbor that touches the current cell.  Will have a maximum of
     * eight neighbors if not an edge or corner cell.  If the neighbor is a bomb, then add
     * it to the count.
     *
     * @param    Cell neighbor
     * @return   void
     */
    public void addNeighbor(Cell neighbor) {
        this.neighbors.add(neighbor);
        if(neighbor.isBomb) {
            this.bombNeighborCount++;
        }
    }

    /*
     * Get all neighbors that touch the current cell.  Will have a maximum of eight neighbors if
     * not an edge or corner cell
     *
     * @param
     * @return    ArrayList<Cell> neighbors
     */
    public ArrayList<Cell> getNeighbors() {
        return this.neighbors;
    }

    /*
     * Get the total number of neighbors to the current cell that have bombs.  Can be a number from
     * zero to eight
     *
     * @param
     * @return    int total
     */
    public int getBombNeighborCount() {
        return this.bombNeighborCount;
    }

    /*
     * Reveal the current cell
     *
     * @param
     * @return    void
     */
    public void reveal() {
        this.isRevealed = true;
    }

}
