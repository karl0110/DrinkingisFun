package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.RectF;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NormalTile extends Tile {

    private int xPosOfNextTile,yPosOfNextTile;
    private Tile nextTile;
    private String[][] information;

    public NormalTile(float x,float y, float width, float height,Bitmap border,Bitmap background, Map map,int xPosOfNextTile,int yPosOfNextTile, String[][] information,int tileDifficulty){
        super(x,y,width,height,border,background,map,tileDifficulty);
        this.xPosOfNextTile=xPosOfNextTile;
        this.yPosOfNextTile=yPosOfNextTile;
        isMiniGame=false;
        this.information=information;
    }



    @Override
    public Tile getNextTile() {
        return nextTile;
    }

    @Override
    public void findNextTile() {
        nextTile=map.getTileFromTileMap(xPosOfNextTile, yPosOfNextTile);
    }

    public String[] getRandomInformation() {
        double index = Math.random()*(information.length-1);
        return information[(int) Math.round(index)];
    }

    public int getTileDifficulty() {

        return (tileDifficulty+1)*2;
    }

}
