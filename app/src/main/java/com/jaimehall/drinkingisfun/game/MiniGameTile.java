package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class MiniGameTile extends Tile {

    private int[][] coordinatesOfNextTiles;
    private Tile[] nextPossibleTiles;
    private Tile nextEasierTile;
    private Tile nextTile;
    private Tile nextHarderTile;

    public MiniGameTile(float x, float y, float width , float height, Bitmap image, Map map, int[][] coordinatesOfNextTiles,int tileDifficulty){
        super(x,y,width,height,image,map,tileDifficulty);
        this.coordinatesOfNextTiles=coordinatesOfNextTiles;
        nextPossibleTiles = new Tile[coordinatesOfNextTiles.length];
        isMiniGame = true;
    }


    @Override
    public void findNextTile() {
        nextEasierTile = map.getTileFromTileMap(coordinatesOfNextTiles[0][0],coordinatesOfNextTiles[0][1]);
        nextTile = map.getTileFromTileMap(coordinatesOfNextTiles[1][0],coordinatesOfNextTiles[1][1]);
        nextHarderTile = map.getTileFromTileMap(coordinatesOfNextTiles[2][0],coordinatesOfNextTiles[2][1]);
    }

    public Tile getNextEasierTile() {
        return nextEasierTile;
    }

    public Tile getNextTile() {
        return nextTile;
    }

    public Tile getNextHarderTile() {
        return nextHarderTile;
    }

    public int getTileDifficulty(){
        return tileDifficulty;
    }
}
