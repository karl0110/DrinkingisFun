package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jaimehall.drinkingisfun.helpers.TextRect;


public abstract class Tile {

    protected float width,height;
    protected float x,y;
    Map map;
    int tileDifficulty;

    private TextRect textRect;
    private int textRectPrep;
    private float baseX,baseY;
    private Player prevPlayer = null;
    private String currentTask;

    public Tile(float x, float y, float width, float height, Map map, int tileDifficulty){
        this.width=width;
        this.height=height;
        this.x=x;
        this.y=y;
        this.map=map;
        this.tileDifficulty=tileDifficulty;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setFakeBoldText(true);
        paint.setTextSize(15);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        textRect = new TextRect(paint);
    }

    public abstract void renderMap(Canvas canvas);

    void renderText(Canvas canvas,Player currentFocusedPlayer){
        if(prevPlayer != currentFocusedPlayer){
            currentTask = currentFocusedPlayer.getTask();
            prevPlayer = currentFocusedPlayer;
        }

        if(textRectPrep != textRect.prepare(currentTask, (int) (width - ((width / 32) * 6)), (int) (height - ((height / 32) * 12)))){
            System.out.println("updating textrect");

            baseX = x + (width / 2);
            baseY = y + ((height / 32) * 3);
            textRectPrep = textRect.prepare(currentTask, (int) (width - ((width / 32) * 6)), (int) (height - ((height / 32) * 12)));
        }

        textRect.draw(canvas, (int) baseX, (int) baseY);


    }

    public abstract Tile getNextTile();

    public abstract void findNextTile(Bitmap arrowRight,Bitmap arrowRightUp,Bitmap arrowRightDown);

    public int getTileDifficulty(){
        return  tileDifficulty;
    }

    public abstract boolean isMiniGame();
    public abstract boolean isGoal();

    Rect getCoordinates(){
        return new Rect((int)x,(int)y,(int)(x+width),(int)(y+height));
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
