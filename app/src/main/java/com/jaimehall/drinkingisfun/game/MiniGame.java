package com.jaimehall.drinkingisfun.game;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MiniGame extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean running;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    public MiniGame(Context context){
        super(context);
        surfaceHolder=getHolder();
    }


    public void run() {

        while(running){
            if(!surfaceHolder.getSurface().isValid()){
                continue;
            }
        }
    }

    public void pause(){
        running = false;

        try {
            thread.join();//Der Thread wird beendet.
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.exit(1);
    }

    public void resume(){
        running = true;
        thread = new Thread(this);
        thread.start();//Der erstellte Thread wird gestartet.(Die Methode run() wird ausgeführt).
    }
}
