package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.Color;
import android.graphics.Rect;

public class EnemyPaddle extends Paddle {

    public EnemyPaddle(float x, float y,float width, float height) {
        super(x, y,new Rect((int)x,(int)y,(int)(x+width),(int)(height+y)), Color.BLACK);
    }


    @Override
    public void tick() {

    }
}
