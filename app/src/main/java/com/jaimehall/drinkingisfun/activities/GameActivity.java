package com.jaimehall.drinkingisfun.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Activity;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.jaimehall.drinkingisfun.game.Game;

import java.util.ArrayList;

public class GameActivity extends Activity {

    private Game game;
    private GestureDetector mGestureDetector;


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


        Display display =getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        float width = size.x;
        float height = size.y+getNavBarHeight();

        String[] playerNames = intent.getStringArrayExtra("playerNames");
        String[] playerIcons = intent.getStringArrayExtra("playerIcons");
        boolean[] playerSexes = intent.getBooleanArrayExtra("playerSexes");

        game = new Game(this , playerNames,playerIcons, playerSexes, height, width);

        setContentView(game);

        game.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        game.progress(motionEvent);
                                        mGestureDetector.onTouchEvent(motionEvent);
                                        return true;
                                    }
                                }
        );

        game.setSystemUiVisibility(uiOptions);
        setScreenOrientationLandscape();

        CustomGestureListener customGestureListener = new CustomGestureListener();

        mGestureDetector = new GestureDetector(this,customGestureListener);

    }



        protected void onResume() {
            super.onResume();
            game.resume();

        }


        protected void onPause() {
            super.onPause();
            game.pause();
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

    class CustomGestureListener implements GestureDetector.OnGestureListener{
        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            game.fling(motionEvent,motionEvent1,v,v1);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }
    }

}


