package com.nraboy.minesweeper;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));  // Set the view to display content from our GameView SurfaceView
    }

}
