package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class PongBall {

    private float radius = 30;
    private float x,y,xAcc,yAcc;
    private PongMiniGame pongMiniGame;
    private Paint ballPaint;
    private boolean isGhostBall;

    public PongBall(PongMiniGame pongMiniGame,float x,float y,float xAcc, float yAcc,boolean isGhostBall){
        this.x=x;
        this.y=y;
        this.xAcc=xAcc;
        this.yAcc=yAcc;
        this.pongMiniGame=pongMiniGame;
        this.isGhostBall=isGhostBall;

        ballPaint = new Paint();
        ballPaint.setStyle(Paint.Style.FILL);
        if(isGhostBall){
            ballPaint.setColor(Color.RED);
        }
        else{
            ballPaint.setColor(Color.BLACK);
        }


    }

    public void tick(){
        x+=xAcc;
        y+=yAcc;
        if(y>pongMiniGame.getHeight()){
            pongMiniGame.gameOver();
        }
        else if(x <0 || x > pongMiniGame.getWidth() -radius){
            xAcc= -xAcc;
        }

        if(isGhostBall){
            if(y>=(pongMiniGame.getHeight()-pongMiniGame.getPlayerPaddle().getHeight())){
                yAcc = -yAcc;
            }
            if(y<=0){
                xAcc=0;
                yAcc=0;
            }
        }
        else{
            if(pongMiniGame.getPlayerPaddle().getCoordinates().contains(getCoordinates())){
                yAcc = -yAcc;
            }
            if(pongMiniGame.getEnemyPaddle().getCoordinates().contains(getCoordinates())){
                yAcc = -yAcc;
                pongMiniGame.resetGhostBall();
            }
        }



    }

    public void render(Canvas canvas){
        if(isGhostBall){
            canvas.drawCircle(x,y,radius,ballPaint);
        }
        else{
            canvas.drawCircle(x,y,radius,ballPaint);
        }

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

    public float getxAcc() {
        return xAcc;
    }

    public float getyAcc() {
        return yAcc;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setxAcc(float xAcc) {
        this.xAcc = xAcc;
    }

    public void setyAcc(float yAcc) {
        this.yAcc = yAcc;
    }
}
