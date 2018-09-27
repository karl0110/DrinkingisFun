package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.Color;
import android.graphics.Rect;

public class EnemyPaddle extends Paddle {

    private PongBall pongBall;
    private PongBall ghostPongBall;



    public EnemyPaddle(PongMiniGame pongMiniGame,float x, float y,float width, float height,PongBall pongBall,PongBall ghostPongBall) {
        super(x, y,width,height, Color.BLACK,pongMiniGame);
        this.pongBall=pongBall;
        this.ghostPongBall=ghostPongBall;
    }


    @Override
    public void tick() {
        xVel-=xVel/4;
        x+=xVel;
        xVel+=xAcc;
        if(Math.signum(-1) != Math.signum(pongBall.getyVel())){
            xAcc=0;
            xVel=0;
        }
        else{
           targetX = (ghostPongBall.getX()-(width/2));

       }

        if(targetX > x){
            xAcc = +10;
        }
        else if(targetX < x){
            xAcc =-10;
        }
        else{
            xAcc = 0;

        }

    }
}
