package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;

public class PlayerPaddle extends Paddle {


    public PlayerPaddle(float x, float y,float width,float height) {
        super(x, y,new Rect((int)x,(int)y,(int)(x+width),(int)(y+height)), Color.BLUE);
    }

    @Override
    public void tick() {

    }

    public void touched(MotionEvent motionEvent){

    }
}
