package com.jaimehall.drinkingisfun.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jaimehall.drinkingisfun.R;


public class BitmapLoader implements Runnable{

    private Bitmap bitmap;
    private int id,width,height;

    private Thread thread;
    private Resources resources;

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
        Bitmap bitmap =BitmapFactory.decodeFile(path);

        return bitmap;
    }


    @Override
    public void run() {
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,id),width,height,false);
    }
}
