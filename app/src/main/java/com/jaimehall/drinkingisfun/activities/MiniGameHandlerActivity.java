package com.jaimehall.drinkingisfun.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.minigames.exBeer.ExBeer;
import com.jaimehall.drinkingisfun.minigames.MiniGameInterface;
import com.jaimehall.drinkingisfun.minigames.MiniGameType;

import java.util.ArrayList;

public class MiniGameHandlerActivity extends Activity {


    private MiniGameInterface miniGame;
    private ExBeer exBeer;
    private int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exBeer = new ExBeer(this);
        setContentView(exBeer);
        exBeer.setSystemUiVisibility(uiOptions);

        //ArrayList<MiniGameInterface> miniGames= new ArrayList<>();
        //miniGames.add(new ExBeer(this));

        //miniGame=miniGames.get(0);

        //setContentView(R.layout.activity_mini_game_handler);
        //playMiniGame((View)miniGame);
        //setContentView((View)miniGame);


    }

    @SuppressLint("ClickableViewAccessibility")
    public void playMiniGame(View view){
        //((View)miniGame).setSystemUiVisibility(uiOptions);
        if(miniGame.isSurfaceView()){
            ((View) miniGame).setSystemUiVisibility(uiOptions);
            MiniGameType tempMiniGameType = miniGame.getMiniGameType();

            if(tempMiniGameType == MiniGameType.EXBEER){

                ((ExBeer) miniGame).setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        ((ExBeer) miniGame).touched(motionEvent);
                        return true;
                    }
                });
            }

        }
        else{

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        exBeer.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        exBeer.resume();

    }
}
