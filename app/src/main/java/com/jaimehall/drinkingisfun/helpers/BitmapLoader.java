package com.jaimehall.drinkingisfun.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;




public class BitmapLoader implements Runnable{

    private Bitmap bitmap;
    private int id;

    private Thread thread;
    private Resources resources;

    public BitmapLoader(Resources resources){
        this.resources = resources;
        thread = new Thread(this);

    }

    public Bitmap getBitmap(int id){
        this.id=id;
        bitmap =null;
        thread.run();

        return bitmap;
    }


    @Override
    public void run() {
        bitmap = BitmapFactory.decodeResource(resources,id);
    }
}
