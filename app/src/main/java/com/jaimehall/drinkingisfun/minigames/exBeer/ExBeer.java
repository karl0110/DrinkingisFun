package com.jaimehall.drinkingisfun.minigames.exBeer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.jaimehall.drinkingisfun.R;

import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.minigames.MiniGame;
import com.jaimehall.drinkingisfun.minigames.MiniGameType;

public class ExBeer extends MiniGame {

    public static final float WIDTH = 1080;
    public static final float HEIGHT= 1920;

    private int amountOfSeconds =6;
    private int bottleFillStep= 9;
    private int tapCounter = 0;
    private Rect buttonRect;
    private Bitmap[] bottle;
    private Rect imageRect;
    private Bitmap button;
    private int timer= 0;
    private int tickCounter =0;
    private boolean timeStarted = false;

    public ExBeer(Game game){
       super(game);

        imageRect = new Rect((int)(WIDTH/8),(int)(HEIGHT/16),(int)((WIDTH/8)*7),(int)((HEIGHT/16)*12));
        buttonRect = new Rect((int)((WIDTH/8)*3),(int)((HEIGHT/8)*6),(int)((WIDTH/8)*5),(int)((HEIGHT/8)*7));
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

    }

    public void reset(){
          bottleFillStep= 9;
          tapCounter = 0;
          timer= 0;
          tickCounter =0;
          timeStarted = false;

    }

    public void touched(MotionEvent mE){
        Rect touchPoint= new Rect((int)mE.getX()-1, (int)mE.getY()-1,(int)mE.getX()+1,(int)mE.getY()+1);
        if(buttonRect.contains(touchPoint)){
            if(timeStarted == false){
                timeStarted=true;
            }
            tapCounter++;
            if(tapCounter>19){
                tapCounter= 0;
                bottleFillStep--;
            }
        }
    }



    public void tick(){
        tickCounter++;
        if(timeStarted && tickCounter>=20){
            timer++;
            tickCounter=0;
        }
        if(timer==amountOfSeconds || bottleFillStep == 0){
            if(bottleFillStep > 5){
                game.finishMiniGame(0);
            }
            else if(bottleFillStep >3){
                game.finishMiniGame(1);
            }
            else if(bottleFillStep >=0){
                game.finishMiniGame(2);
            }
        }
    }

    public void render(Canvas canvas){
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//Drawing a transparent Background to clear away old draws from the Tiles.
        canvas.drawBitmap(bottle[bottleFillStep],null,imageRect,null);
        canvas.drawBitmap(button,null,buttonRect,null);

    }

    public MiniGameType getMiniGameType() {
        return MiniGameType.EXBEER;
    }

}
