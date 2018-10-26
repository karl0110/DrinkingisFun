package com.jaimehall.drinkingisfun.game;

import android.graphics.Canvas;
import android.graphics.Rect;


public class BackgroundHandler {

    private BackgroundTile[][] backgroundTiles;

    private Camera camera;

    BackgroundHandler(){
        backgroundTiles = new BackgroundTile[26][9];
    }


    public void render(Canvas canvas){
        Rect cameraRect = camera.getCameraRect();
        for(int x = 0;x<backgroundTiles.length;x++){
            for(int y = 0;y<backgroundTiles[x].length;y++){
                backgroundTiles[x][y].render(canvas,cameraRect);
            }
        }

    }

    void renderFocusedTile(Canvas canvas,int xPos,int yPos){
        backgroundTiles[xPos][yPos].render(canvas);
    }


    void setCamera(Camera camera) {
        this.camera = camera;
    }

    void setBackgroundTile(BackgroundTile backgroundTile, int xPos, int yPos){
        backgroundTiles[xPos][yPos] = backgroundTile;
    }


}
