package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.minigames.MiniGame;

public class PongMiniGame extends MiniGame {

    private EnemyPaddle enemyPaddle;
    private PlayerPaddle playerPaddle;
    private PongBall pongBall;
    private PongBall ghostPongBall;
    private float width,height;
    private int ballHit;


    public PongMiniGame(Game game,float width, float height) {
        super(game);
        this.width=width;
        this.height=height;
        pongBall = new PongBall(this,width/2,height/2,height/64,15,-15,0.1f,false);
        ghostPongBall = new PongBall(this,width/2,height/2,30,30,-30,0.1f,true);
        enemyPaddle=new EnemyPaddle(this,(width/8)*3,(height/32),width/8*2,height/32,pongBall,ghostPongBall);
        playerPaddle=new PlayerPaddle(this,(width/8)*3,height-((height/32)*2),(width/8)*2,height/32);

        tutorialRect = new Rect(0,0,(int)width,(int)height);
        tutorial=BitmapFactory.decodeResource(resources,R.drawable.pongtutorial);
    }

    public void gameOver(){
        if(ballHit >=8){
            game.finishMiniGame(2);
        }
        else if(ballHit >4){
            game.finishMiniGame(1);
        }
        else if(ballHit >0){
            game.finishMiniGame(0);
        }
    }

    public void gameOver(int i){
        game.finishMiniGame(i);
    }

    public void tutorialFinished(){

    }

    @Override
    public void reset() {
        pongBall = new PongBall(this,width/2,height/2,height/64,15,-15,0.1f,false);
        ghostPongBall = new PongBall(this,width/2,height/2,30,30,-30,0.1f,true);
        enemyPaddle=new EnemyPaddle(this,(width/8)*3,(height/32),width/8*2,height/32,pongBall,ghostPongBall);
        playerPaddle=new PlayerPaddle(this,(width/8)*3,height-((height/32)*2),(width/8)*2,height/32);
        ballHit = 0;
        tickCounter=0;
        tutorialFinished=false;
    }

    public void resetGhostBall(){
        ghostPongBall.setX(pongBall.getX());
        ghostPongBall.setxVel((pongBall.getxVel()*2));
        ghostPongBall.setY(pongBall.getY());
        ghostPongBall.setyVel((pongBall.getyVel()*2));
        tickCounter=0;
    }

    public boolean isPortrait(){
        return true;
    }

    @Override
    public void render(Canvas canvas) {

        canvas.drawColor(Color.WHITE);
        pongBall.render(canvas);
        enemyPaddle.render(canvas);
        playerPaddle.render(canvas);
        if(!tutorialFinished){
            canvas.drawBitmap(tutorial,null,tutorialRect,tutorialPaint);
        }
    }

    @Override
    public void tick() {
        if(!tutorialFinished)tickCounter++;
        if(tutorialFinished){

            enemyPaddle.tick();
            playerPaddle.tick();
            ghostPongBall.tick();
            pongBall.tick();
        }

    }

    @Override
    public void touched(MotionEvent motionEvent) {
        if(tutorialFinished){
            playerPaddle.touched(motionEvent);
        }
       if(!tutorialFinished && tickCounter>50){
           tutorialFinished=true;
        }
    }



    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void ballHit(){
        ballHit++;
    }



    public EnemyPaddle getEnemyPaddle() {
        return enemyPaddle;
    }

    public PlayerPaddle getPlayerPaddle() {
        return playerPaddle;
    }

}
