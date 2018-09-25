package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.jaimehall.drinkingisfun.helpers.TextRect;


public abstract class Tile {

    protected RectF coordinates;
    protected float x,y;
    protected Bitmap image;
    protected Map map;
    protected boolean taskFinished = false;
    protected boolean isMiniGame;
    protected int tileDifficulty;
    protected TextRect textRect;
    protected int textRectPrep;
    protected float baseX,baseY;

    public Tile(RectF coordinates,float x,float y,Bitmap image,Map map,int tileDifficulty){
        this.coordinates=coordinates;
        this.x=x;
        this.y=y;
        this.image=image;
        this.map=map;
        this.tileDifficulty=tileDifficulty;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.getFontMetrics();
        paint.setTextSize(30);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        textRect = new TextRect(paint);
    }

    public void render(Canvas canvas){
        canvas.drawBitmap(image,null,coordinates,null);
    }

    public void renderText(Canvas canvas,String completeInformation){
        if(textRectPrep !=textRect.prepare(completeInformation,(int)(coordinates.width()-((coordinates.width()/32)*6)),(int)(coordinates.height()-((coordinates.height()/32)*12)))){
            baseX = x+(coordinates.width()/2);
            baseY = y+((coordinates.height()/32)*3);
            textRectPrep = textRect.prepare(completeInformation,(int)(coordinates.width()-((coordinates.width()/32)*6)),(int)(coordinates.height()-((coordinates.height()/32)*12)));
        }

        textRect.draw(canvas,(int)baseX,(int)baseY);
    }

    public abstract Tile getNextTile();

    public abstract void findNextTile();

    public abstract int getTileDifficulty() ;

    public Rect getRectCoordinates(){
        return new Rect((int)x,(int)y,(int)coordinates.width(),(int)coordinates.height());
    }

    public RectF getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(RectF coordinates) {
        this.coordinates = coordinates;
    }


    public boolean isTaskFinished() {
        return taskFinished;
    }

    public void setTaskFinished(boolean taskFinished) {
        this.taskFinished = taskFinished;
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

    public boolean isMiniGame() {
        return isMiniGame;
    }

    public void setMiniGame(boolean miniGame) {
        isMiniGame = miniGame;
    }


}
