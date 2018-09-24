package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;

public class PlayerPaddle extends Paddle {


    public PlayerPaddle(PongMiniGame pongMiniGame,float x, float y,float width,float height) {
        super(x, y,width,height, Color.BLACK,pongMiniGame);
        targetX = pongMiniGame.getWidth()/2;
    }

    @Override
    public void tick() {
        x-=xAcc/4;
        x += xVel;
        xVel+=xAcc;

        if(targetX > (x+((width/8)*6))){
            xAcc = +10;
        }
        else if(targetX < (x+((width/8)*2))){
            xAcc =-10;
        }
        else{
            xAcc = 0;
            xVel-=xVel/2;
        }

    }

    public void touched(MotionEvent motionEvent){

        targetX=motionEvent.getX();

    }
}
