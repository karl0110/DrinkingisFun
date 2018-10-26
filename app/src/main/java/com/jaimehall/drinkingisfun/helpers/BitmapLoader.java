package com.jaimehall.drinkingisfun.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jaimehall.drinkingisfun.R;


public class BitmapLoader implements Runnable{

    private Bitmap bitmap;
    private Bitmap background;
    private int id,width,height;

    private Thread thread;
    private Resources resources;

    private boolean loadedBackground = false;

    public BitmapLoader(Resources resources){
        this.resources = resources;
        thread = new Thread(this);

    }

    public Bitmap getBitmap(int id,int width,int height){
        this.id=id;
        bitmap =null;
        this.width=width;
        this.height=height;
        thread.run();

        return bitmap;
    }

    public Bitmap getBitmapFromPath(String path){

        return BitmapFactory.decodeFile(path);
    }

    public Bitmap getBackgroundBitmap(int xPos,int yPos,float width,float height){
        float x = xPos*width;
        float y =yPos* height;
        return Bitmap.createBitmap(background,(int)x,(int)y,(int)width,(int)height);
    }


    @Override
    public void run() {
        if(!loadedBackground){
            background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,R.drawable.maphintergrund),13000,2529,false);
            loadedBackground=true;
        }
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,id),width,height,false);
    }
}
