package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.jaimehall.drinkingisfun.helpers.BitmapLoader;


public class BackgroundHandler {

    private Bitmap[] backgroundColumns;
    private Rect[] backgroundColRects;

    private Camera camera;


    BackgroundHandler(BitmapLoader bitmapLoader,float colWidth,float colHeight){


        backgroundColumns = new Bitmap[26];
        backgroundColRects = new Rect[26];

        for(int i = 0; i<26;i++){
            backgroundColumns[i] = bitmapLoader.getBackgroundBitmap(i,colWidth,colHeight);
            float x = i*colWidth;
            backgroundColRects[i] = new Rect((int)x,0,(int)(x+colWidth),(int)colHeight);
        }
    }


    public void render(Canvas canvas){
        for(int x = 0;x<backgroundColumns.length;x++){
            //if(Rect.intersects(camera.getCameraRect(),backgroundColRects[x])) {
                canvas.drawBitmap(backgroundColumns[x], null,backgroundColRects[x], null);
           // }
        }

    }

    void renderFocusedTile(Canvas canvas,int xPos){
        canvas.drawBitmap(backgroundColumns[xPos],null,backgroundColRects[xPos],null);
    }


    void setCamera(Camera camera) {
        this.camera = camera;
    }




}
