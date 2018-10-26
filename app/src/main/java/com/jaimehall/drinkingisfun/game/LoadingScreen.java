package com.jaimehall.drinkingisfun.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class LoadingScreen {

    private Rect strokeLoadingBarRect;
    private Rect loadingBarRect;
    private Paint strokeLoadingBar;
    private Paint fillLoadingBar;

    private int progress;
    private int maxProgress;


    LoadingScreen(float width,float height,int maxProgress){
        this.maxProgress = maxProgress;

        strokeLoadingBarRect = new Rect((int)(width/8),(int)((height/8)*3),(int)((width/8)*7),(int)((height/8)*5));
        loadingBarRect = new Rect();

        strokeLoadingBar = new Paint();
        strokeLoadingBar.setStyle(Paint.Style.STROKE);
        strokeLoadingBar.setColor(Color.BLACK);

        fillLoadingBar = new Paint();
        fillLoadingBar.setStyle(Paint.Style.FILL);
        fillLoadingBar.setColor(Color.WHITE);

    }


    private void tick(){
        loadingBarRect.set(strokeLoadingBarRect.left,strokeLoadingBarRect.top,strokeLoadingBarRect.left+(strokeLoadingBarRect.width()*(progress/maxProgress)),strokeLoadingBarRect.bottom);
    }

    public void render(Canvas canvas){
        tick();

        canvas.drawRect(loadingBarRect, fillLoadingBar);
        canvas.drawRect(strokeLoadingBarRect, strokeLoadingBar);

    }

    void progressOne(){
        progress++;
    }





}
