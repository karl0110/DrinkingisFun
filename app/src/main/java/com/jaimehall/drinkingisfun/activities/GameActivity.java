package com.jaimehall.drinkingisfun.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Activity;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;

import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.game.LoadingScreen;

public class GameActivity extends Activity{

    private Game game;
    private LoadingScreen loadingScreen;
    private boolean initPhase = true;

    private ScaleGestureDetector scaleDetector;

    private int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display =getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        float width = size.x;
        float height = size.y+getNavBarHeight();

        setScreenOrientationLandscape();

        loadingScreen = new LoadingScreen(this,height ,width,20);

        setContentView(loadingScreen);

        loadingScreen.setSystemUiVisibility(uiOptions);


        Intent intent = getIntent();

        String[] playerNames = intent.getStringArrayExtra("playerNames");
        String[] playerIcons = intent.getStringArrayExtra("playerIcons");
        String[] playerSexes = intent.getStringArrayExtra("playerSexes");

        game = new Game(this , playerNames,playerIcons, playerSexes, height, width, loadingScreen);
        game.setSystemUiVisibility(uiOptions);


        game.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        scaleDetector.onTouchEvent(motionEvent);
                                        game.touch(motionEvent);
                                        return true;
                                    }
                                }
        );

        scaleDetector = new ScaleGestureDetector(this,new ScaleListener());


        System.out.println("Finished with gameActivity");
    }

    public void initComplete(){
        initPhase = false;
        game.resume();
        game.setSystemUiVisibility(uiOptions);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(game);
            }
        });
        loadingScreen.pause();

    }

    public void initGame(){
        final Thread initializerThread = new Thread(){
            @Override
            public void run() {
                super.run();
                game.init();
            }
        };

        initializerThread.start();
    }


    public void startGameOverAcitivty(String[] playerNames, long[] playerScores){
        Intent intent = new Intent(this,GameOverActivity.class);
        intent.putExtra("playerNames",playerNames);
        intent.putExtra("playerScores",playerScores);
        startActivity(intent);
    }


        protected void onResume() {
            super.onResume();
            if(initPhase){
                loadingScreen.resume();
            }
            else {
                game.resume();
                game.setSystemUiVisibility(uiOptions);
            }

        }


        protected void onPause() {
            if(initPhase){
                loadingScreen.pause();
            }
            else {
                super.onPause();
                game.pause();
            }
        }

        public void setScreenOrientationLandscape(){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        public void setScreenOrientationPortrait(){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    private int getNavBarHeight() {
        int result = 0;
        boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        if(!hasMenuKey && !hasBackKey) {
            //The device has a navigation bar
            Resources resources = getResources();

            int orientation = resources.getConfiguration().orientation;
            int resourceId;
            if (isTablet()){
                resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
            }  else {
                resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_width", "dimen", "android");
            }

            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }


    private boolean isTablet() {
        return (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }



    @Override
    public void onBackPressed() {
        reallyExit();
    }

    public void reallyExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,android.R.style.Theme_DeviceDefault));


        builder.setTitle("Spiel Beenden");
        builder.setMessage("Möchtest du wirklich das Spiel verlassen? Alle Spieldaten gehen verloren.");
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                GameActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            game.scale(detector.getScaleFactor());

            return true;
        }
    }
}


