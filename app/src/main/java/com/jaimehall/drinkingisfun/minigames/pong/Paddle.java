package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class Paddle {

    float x,xVel,xAcc;
    private float y;
    protected float width,height;
    private Paint paddlePaint;
    float targetX;

    Paddle(float x,float y, float width,float height,int paddleColor){
        this.x=x;
        this.y=y;
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

    Rect getCoordinates() {
        return new Rect((int)x,(int)y,(int)(x+width),(int)(y+height));
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
