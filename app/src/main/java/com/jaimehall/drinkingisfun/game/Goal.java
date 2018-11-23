package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Goal extends Tile {

    Goal(float x, float y,float width, float height,Map map,int tileDifficulty ){
        super(x,y,width,height,map,tileDifficulty);
    }

    public void renderMap(Canvas canvas,Rect spaceToDraw) {
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
