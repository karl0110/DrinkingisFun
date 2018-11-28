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
    protected boolean minigameFinished = false;
    protected int endScore = 0;
    protected int tickCounter = 120;
    protected Bitmap tutorial;
    protected Bitmap endBitmap;

    private Rect fullScreenRect;
    private Paint fullScreenPaint;

    public MiniGame(Game game,float width,float height){
        this.game=game;
        resources = game.getResources();


        fullScreenPaint = new Paint();
        fullScreenPaint.setAlpha(180);

        fullScreenRect = new Rect(0,0,(int)width,(int)height);
    }

    public abstract void render(Canvas canvas);
    public abstract void tick();
    public abstract void touched(MotionEvent motionEvent);
    public abstract void reset();
    public abstract void tutorialFinished();

    protected void universalReset(){
        tutorialFinished = false;
        minigameFinished = false;
        tickCounter=90;

    }

    protected void universalRender(Canvas canvas){
        if(!tutorialFinished){
            canvas.drawBitmap(tutorial,null,fullScreenRect,fullScreenPaint);
        }
        if(minigameFinished){
            canvas.drawBitmap(endBitmap,null,fullScreenRect,fullScreenPaint);
        }
    }

    protected void universalTouch(){

        if(minigameFinished && tickCounter == 0){
            game.finishMiniGame(endScore);
        }
        if(!tutorialFinished && tickCounter == 0){
            tutorialFinished = true;
            tutorialFinished();
        }

        tickCounter = 90;
    }

    protected void tickUniversalMinigame(){
        if(tickCounter >0) {
            tickCounter--;
        }
    }


}
