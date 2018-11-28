package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.jaimehall.drinkingisfun.helpers.BitmapLoader;


public class BackgroundHandler {

    private Bitmap[][] backgroundTiles;
    private Rect[][] backgroundTileRects;

    private Camera camera;


    BackgroundHandler(BitmapLoader bitmapLoader,float colWidth,float colHeight){
//
//
//        backgroundTiles = new Bitmap[26][9];
//        backgroundTileRects = new Rect[26][9];
//
//        for(int x = 0; x<26;x++){
//            for(int y = 0;y<9;y++) {
//                backgroundTiles[x][y] = bitmapLoader.getBackgroundBitmap(x,y, colWidth, colHeight);
//                float xx = x * colWidth;
//                float yy = y* colHeight;
//                backgroundTileRects[x][y] = new Rect((int) xx, (int)yy, (int) (xx + colWidth), (int) (yy+colHeight));
//            }
//        }
    }


    public void render(Canvas canvas,Rect spaceToDraw){
        for(int x = 0;x<backgroundTiles.length;x++){
            for(int y = 0;y<backgroundTiles[0].length;y++) {
                if (Rect.intersects(spaceToDraw, backgroundTileRects[x][y])) {

                    canvas.drawBitmap(backgroundTiles[x][y], null, backgroundTileRects[x][y], null);
                }
            }
        }

    }




    void setCamera(Camera camera) {
        this.camera = camera;
    }




}
