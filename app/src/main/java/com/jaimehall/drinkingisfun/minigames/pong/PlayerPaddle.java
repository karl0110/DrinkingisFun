package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;

public class PlayerPaddle extends Paddle {


    public PlayerPaddle(PongMiniGame pongMiniGame,float x, float y,float width,float height) {
        super(x, y,width,height, Color.BLUE,pongMiniGame);
    }

    @Override
    public void tick() {
        if (x > 0 && x < pongMiniGame.getWidth() - width )
            x += xAcc;
        else if (x == 0)
            x++;
        else if (x == pongMiniGame.getWidth() - width)
            x--;
    }

    public void touched(MotionEvent motionEvent){
        x=motionEvent.getX();
    }
}
