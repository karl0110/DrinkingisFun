package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class NormalTile extends Tile {

    private int xPosOfNextTile,yPosOfNextTile;
    private Tile nextTile;
    private Bitmap arrow;
    private Rect arrowRect;
    private String arrowDirection;
    private boolean isBlueTile;

    NormalTile(float x,float y, float width, float height, Map map,int xPosOfNextTile,int yPosOfNextTile, int tileDifficulty,boolean isBlueTile){
        super(x,y,width,height,map,tileDifficulty);
        this.xPosOfNextTile=xPosOfNextTile;
        this.yPosOfNextTile=yPosOfNextTile;
        this.isBlueTile=isBlueTile;

    }


    public void renderMap(Canvas canvas,Rect spaceToDraw){
        if(Rect.intersects(spaceToDraw,getCoordinates())) {
            switch (arrowDirection) {
                case ("UP"):
                    canvas.drawBitmap(arrow, null, arrowRect, null);
                    break;
                case ("RIGHT"):
                    canvas.drawBitmap(arrow, null, arrowRect, null);
                    break;
                case ("DOWN"):
                    canvas.drawBitmap(arrow, null, arrowRect, null);
                    break;
            }
        }

    }

    @Override
    public boolean isMiniGame() {
        return false;
    }

    @Override
    public boolean isGoal() {
        return false;
    }

    @Override
    public Tile getNextTile() {
        return nextTile;
    }

    @Override
    public void findNextTile(Bitmap arrowRight,Bitmap arrowRightUp,Bitmap arrowRightDown) {

        nextTile=map.getTileFromTileMap(xPosOfNextTile, yPosOfNextTile);

        if((y/height)>yPosOfNextTile){
            arrow = arrowRightUp;
            arrowDirection = "UP";
            arrowRect = new Rect((int)(x+width-(arrowRightUp.getWidth()/2)),(int)(y-(arrowRightUp.getHeight()/2)),(int)(x+width+(arrowRightUp.getWidth()/2)),(int)(y+(arrowRightUp.getHeight()/2)));
        }
        else if((y/height)<yPosOfNextTile) {
            arrow = arrowRightDown;
            arrowDirection = "DOWN";
            arrowRect = new Rect((int) (x+width-(arrowRightUp.getWidth()/2)), (int) (y + height-(arrowRightUp.getHeight()/2)), (int) (x+width+(arrowRightUp.getWidth()/2)), (int) (y + height + (arrowRightUp.getHeight()/2)));

        }
        else{
            arrow = arrowRight;
            arrowDirection = "RIGHT";
            arrowRect = new Rect((int)(x+width-(arrowRightUp.getWidth()/2)),(int)(y+((height/2)-(arrowRight.getHeight()/2))),(int)(x+width+(arrowRightUp.getWidth()/2)),(int)(y+((height/2)+(arrowRight.getHeight()/2))));
        }
    }


    public boolean isBlueTile() {
        return isBlueTile;
    }
}
