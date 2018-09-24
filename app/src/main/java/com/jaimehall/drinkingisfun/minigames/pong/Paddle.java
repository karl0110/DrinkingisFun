package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class Paddle {

    protected float x,y,xAcc;
    protected float width,height;
    protected Paint paddlePaint;
    protected PongMiniGame pongMiniGame;

    public Paddle(float x,float y, float width,float height,int paddleColor,PongMiniGame pongMiniGame){
        this.x=x;
        this.y=y;
        this.pongMiniGame=pongMiniGame;
        this.width=width;
        this.height=height;
        paddlePaint=new Paint();
        paddlePaint.setColor(paddleColor);
        paddlePaint.setStyle(Paint.Style.FILL);


    }

    public void render(Canvas canvas){
        canvas.drawRect(getCoordinates(),paddlePaint);
    }
    public abstract void tick();

    public Rect getCoordinates() {
        return new Rect((int)x,(int)y,(int)(x+width),(int)(y+height));
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
