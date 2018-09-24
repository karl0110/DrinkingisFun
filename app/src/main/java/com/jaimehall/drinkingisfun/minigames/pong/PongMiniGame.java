package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.minigames.MiniGame;

public class PongMiniGame extends MiniGame {

    private EnemyPaddle enemyPaddle;
    private PlayerPaddle playerPaddle;
    private PongBall pongBall;
    private PongBall ghostPongBall;
    private float width,height;


    public PongMiniGame(Game game,float width, float height) {
        super(game);
        this.width=width;
        this.height=height;
        pongBall = new PongBall(this,width/2,height/2,30,-30,false);
        ghostPongBall = new PongBall(this,width/2,height/2,100,-100,true);
        enemyPaddle=new EnemyPaddle(this,0,0,300,50,pongBall,ghostPongBall);
        playerPaddle=new PlayerPaddle(this,0,1800,300,50);
    }

    public void resetGhostBall(){
        ghostPongBall.setX(pongBall.getX());
        ghostPongBall.setxAcc((pongBall.getxAcc()*3));
        ghostPongBall.setY(pongBall.getY());
        ghostPongBall.setyAcc((pongBall.getyAcc()*3));
    }

    public boolean isPortrait(){
        return true;
    }

    @Override
    public void render(Canvas canvas) {

        canvas.drawColor(Color.WHITE);

        pongBall.render(canvas);
        ghostPongBall.render(canvas);
        enemyPaddle.render(canvas);
        playerPaddle.render(canvas);
    }

    @Override
    public void tick() {
        ghostPongBall.tick();
        enemyPaddle.tick();
        playerPaddle.tick();
        pongBall.tick();
    }

    @Override
    public void touched(MotionEvent motionEvent) {
        playerPaddle.touched(motionEvent);
    }

    @Override
    public void reset() {

    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void gameOver(){

    }

    public EnemyPaddle getEnemyPaddle() {
        return enemyPaddle;
    }

    public PlayerPaddle getPlayerPaddle() {
        return playerPaddle;
    }

}
