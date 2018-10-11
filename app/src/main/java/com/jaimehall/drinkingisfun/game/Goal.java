package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Goal extends Tile {

    public Goal(float x, float y,float width, float height,Bitmap border,Bitmap background,Map map,int tileDifficulty ){
        super(x,y,width,height,border,background,map,tileDifficulty);
    }

    public void renderMap(Canvas canvas) {
        canvas.drawBitmap(image,null,getCoordinates(),null);
    }

    @Override
    public boolean isMiniGame() {
        return false;
    }

    @Override
    public boolean isGoal() {
        return true;
    }

    @Override
    public Tile getNextTile() { return null; }

    @Override
    public void findNextTile(Bitmap arrowRight, Bitmap arrowRightUp, Bitmap arrowRightDown) { }

    @Override
    public int getTileDifficulty() {
        return tileDifficulty;
    }
}
