package com.nraboy.minesweeper;

import android.graphics.*;

public class HUD extends Sprite {

    /*
     * Construct the HUD sprite.  In this game there will be two HUD sprites, one
     * for starting a new game and another for cheating
     *
     * @param    GameView gameView
     * @param    Bitmap spriteSheet
     * @param    int x
     * @param    int y
     */
    public HUD(GameView gameView, Bitmap spriteSheet, int x, int y) {
        super(gameView, spriteSheet, x, y, 1, 3);   // The parent class Sprite also expects to know how many columns and rows exist in the sheet
    }

}
