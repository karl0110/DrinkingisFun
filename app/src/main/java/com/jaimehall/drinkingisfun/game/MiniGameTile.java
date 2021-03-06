package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class MiniGameTile extends Tile {

    private int[][] coordinatesOfNextTiles;
    private Tile nextEasierTile;
    private Tile nextTile;
    private Tile nextHarderTile;
    private Bitmap arrowRight,arrowRightUp,arrowRightDown;
    private Rect arrowRightRect,arrowUpRect,arrowDownRect;

    MiniGameTile(float x, float y, float width , float height, Map map, int[][] coordinatesOfNextTiles,int tileDifficulty){
        super(x,y,width,height,map,tileDifficulty);
        this.coordinatesOfNextTiles=coordinatesOfNextTiles;
    }

    public void renderMap(Canvas canvas,Rect spaceToDraw){
        if(Rect.intersects(spaceToDraw,getCoordinates())) {
            if (arrowRightUp != null) {
                canvas.drawBitmap(arrowRightUp, null, arrowUpRect, null);
            }
            if (arrowRight != null) {
                canvas.drawBitmap(arrowRight, null, arrowRightRect, null);
            }
            if (arrowRightDown != null) {
                canvas.drawBitmap(arrowRightDown, null, arrowDownRect, null);
            }
        }
    }

    @Override
    public boolean isMiniGame() {
        return true;
    }

    @Override
    public boolean isGoal() {
        return false;
    }

    @Override
    public void findNextTile(Bitmap arrowRight,Bitmap arrowRightUp,Bitmap arrowRightDown) {

        nextEasierTile = map.getTileFromTileMap(coordinatesOfNextTiles[0][0],coordinatesOfNextTiles[0][1]);
        nextTile = map.getTileFromTileMap(coordinatesOfNextTiles[1][0],coordinatesOfNextTiles[1][1]);
        nextHarderTile = map.getTileFromTileMap(coordinatesOfNextTiles[2][0],coordinatesOfNextTiles[2][1]);

        if(nextEasierTile != null){
            this.arrowRightUp = arrowRightUp;
            arrowUpRect = new Rect((int)(x+width-(arrowRightUp.getWidth()/2)),(int)(y-(arrowRightUp.getHeight()/2)),(int)(x+width+(arrowRightUp.getWidth()/2)),(int)(y+(arrowRightUp.getHeight()/2)));
        }
        if(nextHarderTile != null){
            this.arrowRightDown = arrowRightDown;
            arrowDownRect = new Rect((int) (x+width-(arrowRightUp.getWidth()/2)), (int) (y + height-(arrowRightUp.getHeight()/2)), (int) (x+width+(arrowRightUp.getWidth()/2)), (int) (y + height + (arrowRightUp.getHeight()/2)));
        }
        if(nextTile != null){
            this.arrowRight = arrowRight;
            arrowRightRect = new Rect((int)(x+width-(arrowRightUp.getWidth()/2)),(int)(y+((height/2)-(arrowRight.getHeight()/2))),(int)(x+width+(arrowRightUp.getWidth()/2)),(int)(y+((height/2)+(arrowRight.getHeight()/2))));
        }
    }

    Tile getNextEasierTile() {
        return nextEasierTile;
    }

    public Tile getNextTile() {
        return nextTile;
    }

    Tile getNextHarderTile() {
        return nextHarderTile;
    }

}
