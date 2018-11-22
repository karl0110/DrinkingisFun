package com.jaimehall.drinkingisfun.minigames.exBeer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.jaimehall.drinkingisfun.R;

import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.helpers.BitmapLoader;
import com.jaimehall.drinkingisfun.minigames.MiniGame;

public class ExBeerMiniGame extends MiniGame {

    private float width;
    private float height;


    private double amountOfMilliSeconds =5000;
    private int bottleFillStep= 9;
    private int tapCounter = 0;
    private long timer;
    private long startTime;

    private Rect buttonRect;
    private Bitmap button;

    private Bitmap[] bottle;
    private Rect imageRect;


    private String timerString= "";
    private Paint timerTextPaint;


    public ExBeerMiniGame(Game game, float width, float height, BitmapLoader bitmapLoader){
       super(game);

       this.width=width;
       this.height = height;

        imageRect = new Rect((int)(width/8),(int)(height/16)*3,(int)((width/8)*7),(int)((height/16)*14));
        buttonRect = new Rect((int)((width/8)*2),(int)((height/16)*14)+10,(int)((width/8)*6),(int)(height-10));
        button = bitmapLoader.getBitmap(R.drawable.tapbutton,200,200);
        bottle = new Bitmap[10];
        bottle[0]=bitmapLoader.getBitmap(R.drawable.bottle0,450,900);
        bottle[1]=bitmapLoader.getBitmap(R.drawable.bottle1,450,900);
        bottle[2]=bitmapLoader.getBitmap(R.drawable.bottle2,450,900);
        bottle[3]=bitmapLoader.getBitmap(R.drawable.bottle3,450,900);
        bottle[4]=bitmapLoader.getBitmap(R.drawable.bottle4,450,900);
        bottle[5]=bitmapLoader.getBitmap(R.drawable.bottle5,450,900);
        bottle[6]=bitmapLoader.getBitmap(R.drawable.bottle6,450,900);
        bottle[7]=bitmapLoader.getBitmap(R.drawable.bottle7,450,900);
        bottle[8]=bitmapLoader.getBitmap(R.drawable.bottle8,450,900);
        bottle[9]=bitmapLoader.getBitmap(R.drawable.bottle9,450,900);

        tutorialRect = new Rect(0,0,(int)width,(int)height);
        tutorial=BitmapFactory.decodeResource(resources,R.drawable.exbeertutorial);

        timerTextPaint = new Paint();
        timerTextPaint.setColor(Color.WHITE);
        timerTextPaint.setTextAlign(Paint.Align.CENTER);
        timerTextPaint.setTextSize(140);


    }

    public boolean isPortrait(){
        return true;
    }

    public void reset(){
          bottleFillStep= 9;
          tapCounter = 0;
          timer= 0;
          tutorialFinished=false;
          tickCounter=0;

    }

    public void touched(MotionEvent mE){
        super.universalMinigameTouch();

        Rect touchPoint= new Rect((int)mE.getX()-1, (int)mE.getY()-1,(int)mE.getX()+1,(int)mE.getY()+1);

        if(tutorialFinished){
            if (buttonRect.contains(touchPoint)) {

                tapCounter++;
                if (tapCounter >= 40) {
                    tapCounter = 0;
                    bottleFillStep--;
                }
            }
        }
    }

    public void tutorialFinished(){
        startTime = System.currentTimeMillis();
        tutorialFinished=true;

    }

    public void tick(){
        super.tickUniversalMinigame();
        if(tutorialFinished){
            timer = System.currentTimeMillis() - startTime;
            double displayTimer = Math.round((amountOfMilliSeconds-timer)*10);
            timerString = "Time left: "+displayTimer/10000+" s";
        }
        if(timer>=amountOfMilliSeconds || bottleFillStep == 0){
            if(bottleFillStep > 3){
                game.finishMiniGame(0);
            }
            else if(bottleFillStep >2){
                game.finishMiniGame(1);
            }
            else if(bottleFillStep >=0){
                game.finishMiniGame(2);
            }
        }
    }

    public void render(Canvas canvas){


        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//Drawing a transparent Background to clear away old draws from the Tiles.

        if(tutorialFinished){
            canvas.drawText(timerString,width/2,(height/16)*2,timerTextPaint);
        }

        canvas.drawBitmap(bottle[bottleFillStep],null,imageRect,null);
        canvas.drawBitmap(button,null,buttonRect,null);

        if(!tutorialFinished){
            super.renderTutorial(canvas);
        }

    }


}
