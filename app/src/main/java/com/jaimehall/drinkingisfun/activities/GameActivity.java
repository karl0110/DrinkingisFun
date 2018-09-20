package com.jaimehall.drinkingisfun.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.minigames.MiniGameHandler;

import java.util.ArrayList;

public class GameActivity extends Activity {

    private Game game;

    private int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        ArrayList<String> playerNames = intent.getStringArrayListExtra("playerNames");
        boolean[] playerSexes = intent.getBooleanArrayExtra("playerSexes");

        game = new Game(this,playerNames,playerSexes);
        setContentView(game);

        game.setOnTouchListener(new View.OnTouchListener() {
            @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    game.progress(motionEvent);

                    return true;
                }
            }
        );
        game.setSystemUiVisibility(uiOptions);
        setScreenOrientationLandscape();

    }

        protected void onResume() {
            super.onResume();
            game.setSystemUiVisibility(uiOptions);
            game.resume();

        }


        protected void onPause() {
            super.onPause();
            game.pause();
        }

        public void startMiniGame(){
            Intent intent = new Intent(this, MiniGameHandler.class);
            startActivity(intent);
        }

        public void setScreenOrientationLandscape(){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        public void setScreenOrientationPortrait(){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

}


