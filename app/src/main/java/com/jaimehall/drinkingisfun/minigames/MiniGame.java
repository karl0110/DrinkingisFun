package com.jaimehall.drinkingisfun.minigames;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.jaimehall.drinkingisfun.game.Game;

public abstract class MiniGame {

    protected Resources resources;
    protected Game game;
    protected boolean tutorialFinished =false;
    protected int tickCounter = 0;
    protected Bitmap tutorial;

    protected Rect tutorialRect;
    protected Paint tutorialPaint;

    public MiniGame(Game game){
        this.game=game;
        resources = game.getResources();


        tutorialPaint = new Paint();
        tutorialPaint.setAlpha(180);
    }

    public abstract void render(Canvas canvas);
    public abstract void tick();
    public abstract void touched(MotionEvent motionEvent);
    public abstract void reset();

    protected void renderTutorial(Canvas canvas){
        canvas.drawBitmap(tutorial,null,tutorialRect,tutorialPaint);
    }

    protected void tickUniversalMinigame(){
        tickCounter++;
    }

}
