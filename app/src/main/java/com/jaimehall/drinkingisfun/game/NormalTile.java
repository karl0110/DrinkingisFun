package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.RectF;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NormalTile extends Tile {

    private int xPosOfNextTile,yPosOfNextTile;
    private Tile nextTile;
    private ArrayList<String> information;

    public NormalTile(float x,float y, float width, float height,Bitmap image, Map map,int xPosOfNextTile,int yPosOfNextTile, ArrayList<String> information,int tileDifficulty){
        super(new RectF(x,y,x+width,y+height),x,y,image,map,tileDifficulty);
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

    public String getRandomInformation() {
        double index = Math.random()*(information.size()-1);
        return information.get((int) Math.round(index));
    }

    public int getTileDifficulty() {

        return (tileDifficulty+1)*2;
    }

}
