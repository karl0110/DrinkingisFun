package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class Paddle {

    protected float x,y;
    protected Rect coordinates;
    protected Paint paddlePaint;

    public Paddle(float x,float y, Rect coordinates,int paddleColor){
        this.x=x;
        this.y=y;
        this.coordinates=coordinates;

        paddlePaint=new Paint();
        paddlePaint.setColor(paddleColor);
        paddlePaint.setStyle(Paint.Style.FILL);


    }

    public void render(Canvas canvas){
        canvas.drawRect(coordinates,paddlePaint);
    }
    public abstract void tick();
}
