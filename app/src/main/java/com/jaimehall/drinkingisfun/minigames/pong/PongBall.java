package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class PongBall {

    private float radius;
    private float x,y,xVel,yVel,acceleration;
    private PongMiniGame pongMiniGame;
    private Paint ballPaint;
    private boolean isGhostBall;

    public PongBall(PongMiniGame pongMiniGame,float x,float y,float radius,float xVel, float yVel,float acceleration,boolean isGhostBall){
        this.x=x;
        this.y=y;
        this.xVel=xVel;
        this.yVel=yVel;
        this.pongMiniGame=pongMiniGame;
        this.isGhostBall=isGhostBall;
        this.radius=radius;
        this.acceleration=acceleration;

        ballPaint = new Paint();
        ballPaint.setStyle(Paint.Style.FILL);
        if(isGhostBall){
            ballPaint.setColor(Color.RED);
        }
        else {
            ballPaint.setColor(Color.BLACK);
        }




    }

    public void tick(){
        x+=xVel;
        if(isGhostBall && (y+yVel) < (pongMiniGame.getEnemyPaddle().getY()+pongMiniGame.getEnemyPaddle().getHeight())){
            y=pongMiniGame.getEnemyPaddle().getY()+pongMiniGame.getEnemyPaddle().getHeight();
            yVel=0;
            xVel=0;
        }
        else{
            y+=yVel;
        }

        if(xVel>0 && yVel>0){
            xVel+=acceleration;
            yVel+=acceleration;
        }
        if(x <0 || x > pongMiniGame.getWidth() -radius){
            xVel= -xVel;
        }
        if(isGhostBall){
            if(y <= (pongMiniGame.getEnemyPaddle().getY()+pongMiniGame.getEnemyPaddle().getHeight())){

                if(Math.signum(yVel) == Math.signum(-1)) {
                    xVel=0;
                    yVel=0;
                }
            }
        }
        else{
            if(y>pongMiniGame.getHeight()){
                pongMiniGame.gameOver();
            }
            if(y<0){
                pongMiniGame.gameOver(2);
            }
            if(Rect.intersects(getCoordinates(),pongMiniGame.getPlayerPaddle().getCoordinates())){
                if(Math.signum(yVel) == Math.signum(1)){
                    yVel = -yVel;
                    pongMiniGame.resetGhostBall();
                    pongMiniGame.ballHit();
                }
            }
            if(Rect.intersects(getCoordinates(),pongMiniGame.getEnemyPaddle().getCoordinates())){
                if(Math.signum(yVel) == (-1)) {
                    yVel = -yVel;

                }
            }
        }



    }

    public void render(Canvas canvas){

        canvas.drawCircle(x,y,radius,ballPaint);

    }

    public Rect getCoordinates(){
        return new Rect((int)x,(int)y,(int)(x+radius),(int)(y+radius));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getxVel() {
        return xVel;
    }

    public float getyVel() {
        return yVel;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setxVel(float xVel) {
        this.xVel = xVel;
    }

    public void setyVel(float yVel) {
        this.yVel = yVel;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }
}
