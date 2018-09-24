package com.jaimehall.drinkingisfun.minigames;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.jaimehall.drinkingisfun.game.Game;

public abstract class MiniGame {

    protected Resources resources;
    protected Game game;
    protected boolean tutorialFinished =false;
    protected int tickCounter = 0;

    public MiniGame(Game game){
        this.game=game;
        resources = game.getResources();
    }

    public abstract void render(Canvas canvas);
    public abstract void tick();
    public abstract void touched(MotionEvent motionEvent);
    public abstract void reset();
    public abstract boolean isPortrait();

}
