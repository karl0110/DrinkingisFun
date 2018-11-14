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

    private BitmapFactory.Options options;

    public BitmapLoader(Resources resources){
        this.resources = resources;
        options = new BitmapFactory.Options();
        options.inPreferredConfig=Bitmap.Config.RGB_565;
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

        return BitmapFactory.decodeFile(path,options);
    }

    public Bitmap getBackgroundBitmap(int xPos,float width,float height){
        float x = xPos*width;
        return Bitmap.createBitmap(background,(int)x,0,(int)width,(int)height);
    }


    @Override
    public void run() {
        if(!loadedBackground){
            background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,R.drawable.maphintergrund,options),6500,1260,false);
            loadedBackground=true;
        }
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,id,options),width,height,false);
    }
}
