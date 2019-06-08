package com.jaimehall.drinkingisfun.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.activities.GameActivity;

public class LoadingScreen extends SurfaceView implements Runnable{

    private Rect strokeLoadingBarRect;
    private Rect loadingBarRect;
    private Paint strokeLoadingBar;
    private Paint fillLoadingBar;

    private Rect backgroundRect;
    private Paint backgroundPaint;

    private Paint textPaint;

    private float progress;
    private float maxProgress;
    private double progressPercent;

    private float width,height;

    private Thread thread;
    private boolean running;

    private SurfaceHolder surfaceHolder;

    private boolean firstRun = true;

    private GameActivity gameActivity;



    public LoadingScreen(Context context, float width, float height, float maxProgress){
        super(context);

        gameActivity = (GameActivity)context;

        surfaceHolder = getHolder();
        surfaceHolder.setFormat(0x00000004);

        this.maxProgress = maxProgress;

        strokeLoadingBarRect = new Rect((int)((width/16)*2),(int)((height/8)*6),(int)((width/16)*14),(int)((height/8))*7);
        loadingBarRect = new Rect();

        backgroundRect = new Rect(0,0,(int)width,(int)height);

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(getResources().getColor(R.color.colorPrimary));

        strokeLoadingBar = new Paint();
        strokeLoadingBar.setStyle(Paint.Style.STROKE);
        strokeLoadingBar.setStrokeWidth(3);
        strokeLoadingBar.setColor(getResources().getColor(R.color.lightRed));

        fillLoadingBar = new Paint();
        fillLoadingBar.setStyle(Paint.Style.FILL);
        fillLoadingBar.setColor(getResources().getColor(R.color.darkRed));

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(100);

    }


    private void tick(){
        progressPercent = (progress/maxProgress);
        loadingBarRect.set(strokeLoadingBarRect.left,strokeLoadingBarRect.top,(int)(strokeLoadingBarRect.left+(strokeLoadingBarRect.width()*progressPercent)),strokeLoadingBarRect.bottom);
    }

    public void render(){

        Canvas canvas = surfaceHolder.lockCanvas();

        if(canvas != null) {

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//Drawing a transparent Background to clear away old draws from the Tiles.

            canvas.drawRect(backgroundRect,backgroundPaint);
            canvas.drawRect(loadingBarRect, fillLoadingBar);

            canvas.drawText(((Math.round(progressPercent*1000))/10)+"%",strokeLoadingBarRect.centerX(),strokeLoadingBarRect.bottom,textPaint);

            canvas.drawRect(strokeLoadingBarRect, strokeLoadingBar);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    public void run(){

        if(firstRun){
            gameActivity.initGame();
            firstRun = false;
        }

        long lastTime = System.nanoTime();//Wird für einen Timer benötigt.
        final double amountOfTicks = 30.0;//Wie oft die Methode tick() in einer Sekunde aufgerufen werden soll.
        double ns = 1000000000 / amountOfTicks;//Berechnet wie viel Zeit vergeht bis die Methode tick() aufgerufen wird.
        double delta = 0;//Variable welche Berechnet, wann die tick() Methode aufgerufen werden soll.
        int frames = 0;//Wie oft das Program die Methode render() in einer Sekunde aufgerufen hat.
        long timer = System.currentTimeMillis();//Eine Variable um die Zeit zu Zählen. Ist für die Berechnung der "TicksPerSecond" und "FramesPerSecond" notwendig.

        while (running) {

            render();

            long now = System.nanoTime();//Timer Variable für die aktuelle Zeit.
            delta += (now - lastTime) / ns;//Berechnet mithilfe der Timer und ns Variablen, wann die tick() Methode aufgerufen werden soll.
            lastTime = now;//Stellt den Timer wieder zurück.
            if (delta >= 1) {//Guckt ob die Methode tick() jetzt aufgerufen werden soll.
                tick();
                delta--;//Setzt den tick() Aufruf-Timer zurück.
                frames++;////Addiert zum Frame-Zähler 1 dazu.
            }

            if (System.currentTimeMillis() - timer > 1000) {//Wenn eine Sekunde vergangen ist.
                timer += 1000;//addiert zum Timer eine Sekunde dazu.
                System.out.println("Looading Screen FPS: " + frames);//Druckt die "TicksPerSecond" und "FramesPerSecond" aus.
                frames = 0;//setzt den Frame-Zähler zurück.
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
    }

    public void resume(){
        running = true;
        thread = new Thread(this);
        thread.start();//Der erstellte Thread wird gestartet.(Die Methode run() wird ausgeführt).

    }

    void progressOne(){
        progress++;
    }





}
