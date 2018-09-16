package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class MiniGameTile extends Tile {

    private int[][] coordinatesOfNextTiles;
    private Tile[] nextPossibleTiles;

    public MiniGameTile(float x, float y, float width , float height, Bitmap image, Map map, int[][] coordinatesOfNextTiles){
        super(new RectF(x,y,x+width,y+height),x,y,image,map);
        this.coordinatesOfNextTiles=coordinatesOfNextTiles;
        nextPossibleTiles = new Tile[coordinatesOfNextTiles.length];
        isMiniGame = true;
    }

    @Override
    public Tile getNextTile() {
        double index = Math.random()*(nextPossibleTiles.length-1);
        return nextPossibleTiles[(int) Math.round(index)];
    }

    @Override
    public void findNextTile() {
        for(int i=0; i < coordinatesOfNextTiles.length ;i++){
            nextPossibleTiles[i]=map.getTileFromTileMap(coordinatesOfNextTiles[i][0], coordinatesOfNextTiles[i][1]);
        }
    }
}
