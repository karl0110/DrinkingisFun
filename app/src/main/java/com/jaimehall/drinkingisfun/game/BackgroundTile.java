package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class BackgroundTile {

    private float x,y,width,height;
    private Bitmap image;


    BackgroundTile(float x,float y,float width, float height,Bitmap image){
        this.x=x;
        this.y=y;
        this.width =width;
        this.height=height;
        this.image = image;
    }

    public void render(Canvas canvas ,Rect cameraRect) {

        Rect coordinates = getCoordinates();
        if(Rect.intersects(cameraRect,coordinates)) {
            canvas.drawBitmap(image, null, coordinates, null);
        }

    }

    public void render(Canvas canvas ){

        canvas.drawBitmap(image,null,getCoordinates(),null);

    }

    private Rect getCoordinates(){
        return new Rect((int)x,(int)y,(int)(x+width),(int)(y+height));
    }
}
