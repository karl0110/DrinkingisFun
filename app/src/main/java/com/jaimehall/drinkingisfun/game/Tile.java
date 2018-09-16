package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;


public abstract class Tile {

    protected RectF coordinates;
    protected float x,y;
    protected Bitmap image;
    protected Map map;
    protected boolean taskFinished = false;
    protected boolean isMiniGame;

    public Tile(RectF coordinates,float x,float y,Bitmap image,Map map){
        this.coordinates=coordinates;
        this.x=x;
        this.y=y;
        this.image=image;
        this.map=map;
    }

    public void render(Canvas canvas){
        canvas.drawBitmap(image,null,coordinates,null);
    }

    public abstract Tile getNextTile();

    public abstract void findNextTile();

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
