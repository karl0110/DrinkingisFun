package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;



public abstract class Tile {

    protected float width,height;
    protected float x,y;
    Map map;
    int tileDifficulty;

    public Tile(float x, float y, float width, float height, Map map, int tileDifficulty){
        this.width=width;
        this.height=height;
        this.x=x;
        this.y=y;
        this.map=map;
        this.tileDifficulty=tileDifficulty;

    }

    public abstract void renderMap(Canvas canvas,Rect spaceToDraw);



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
