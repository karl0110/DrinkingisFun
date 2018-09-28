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


    public ExBeerMiniGame(Game game, float width, float height){
       super(game);

       this.width=width;
       this.height = height;

        imageRect = new Rect((int)(width/8),(int)(height/16)*3,(int)((width/8)*7),(int)((height/16)*14));
        buttonRect = new Rect((int)((width/8)*3),(int)((height/16)*14)+10,(int)((width/8)*5),(int)(height-10));
        button = BitmapFactory.decodeResource(resources,R.drawable.tapbutton);
        bottle = new Bitmap[10];
        bottle[0]=BitmapFactory.decodeResource(resources,R.drawable.bottle0);
        bottle[1]=BitmapFactory.decodeResource(resources,R.drawable.bottle1);
        bottle[2]=BitmapFactory.decodeResource(resources,R.drawable.bottle2);
        bottle[3]=BitmapFactory.decodeResource(resources,R.drawable.bottle3);
        bottle[4]=BitmapFactory.decodeResource(resources,R.drawable.bottle4);
        bottle[5]=BitmapFactory.decodeResource(resources,R.drawable.bottle5);
        bottle[6]=BitmapFactory.decodeResource(resources,R.drawable.bottle6);
        bottle[7]=BitmapFactory.decodeResource(resources,R.drawable.bottle7);
        bottle[8]=BitmapFactory.decodeResource(resources,R.drawable.bottle8);
        bottle[9]=BitmapFactory.decodeResource(resources,R.drawable.bottle9);

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
        Rect touchPoint= new Rect((int)mE.getX()-1, (int)mE.getY()-1,(int)mE.getX()+1,(int)mE.getY()+1);
        if(tutorialFinished == false && tickCounter>=60){
            tutorialFinished();
        }
        else {
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
        if(!tutorialFinished)tickCounter++;
        if(tutorialFinished){
            timer = System.currentTimeMillis() - startTime;
            double displayTimer = Math.round((amountOfMilliSeconds-timer)*10);
            timerString = "Time left: "+displayTimer/10000+" s";
        }
        if(timer>=amountOfMilliSeconds || bottleFillStep == 0){
            if(bottleFillStep > 5){
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

        if(tutorialFinished==false){
            canvas.drawBitmap(tutorial,null,tutorialRect,tutorialPaint);
        }

    }


}
