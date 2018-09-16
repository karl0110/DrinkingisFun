package com.jaimehall.drinkingisfun.activities;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.game.MiniGame;

public class MiniGameActivity extends Activity {

    private MiniGame miniGame;

    private int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            |View.SYSTEM_UI_FLAG_FULLSCREEN
            |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        miniGame=new MiniGame(this);
        setContentView(miniGame);

        miniGame.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener(){
                    @Override
                    public void onSystemUiVisibilityChange(int i) {
                        if((i & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)==0){
                            miniGame.setSystemUiVisibility(uiOptions);

                        }
                    }
                }

        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        miniGame.setSystemUiVisibility(uiOptions);
        miniGame.resume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        miniGame.pause();
    }
}
