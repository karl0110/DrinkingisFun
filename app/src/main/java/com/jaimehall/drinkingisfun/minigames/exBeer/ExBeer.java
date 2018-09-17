package com.jaimehall.drinkingisfun.minigames.exBeer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.minigames.MiniGameInterface;
import com.jaimehall.drinkingisfun.minigames.MiniGameType;

public class ExBeer extends SurfaceView implements MiniGameInterface,Runnable{

    public static final float WIDTH = 1080;
    public static final float HEIGHT= 1920;

    private boolean running;
    private Thread thread;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;

    private int bottleFillStep= 0;
    private int tapCounter = 0;
    private Rect buttonRect;
    private Bitmap[] bottle;
    private Rect imageRect;
    private Bitmap button;

    public ExBeer(Context context){
        super(context);
        surfaceHolder = getHolder();
        imageRect = new Rect((int)(WIDTH/8),(int)(HEIGHT/16),(int)((WIDTH/8)*7),(int)((HEIGHT/16)*12));
        buttonRect = new Rect((int)((WIDTH/8)*3),(int)((HEIGHT/8)*6),(int)((WIDTH/8)*5),(int)((HEIGHT/8)*7));
        button = BitmapFactory.decodeResource(getResources(),R.drawable.tapbutton);
        bottle = new Bitmap[10];
        bottle[0]=BitmapFactory.decodeResource(getResources(),R.drawable.bottle0);
        bottle[1]=BitmapFactory.decodeResource(getResources(),R.drawable.bottle1);
        bottle[2]=BitmapFactory.decodeResource(getResources(),R.drawable.bottle2);
        bottle[3]=BitmapFactory.decodeResource(getResources(),R.drawable.bottle3);
        bottle[4]=BitmapFactory.decodeResource(getResources(),R.drawable.bottle4);
        bottle[5]=BitmapFactory.decodeResource(getResources(),R.drawable.bottle5);
        bottle[6]=BitmapFactory.decodeResource(getResources(),R.drawable.bottle6);
        bottle[7]=BitmapFactory.decodeResource(getResources(),R.drawable.bottle7);
        bottle[8]=BitmapFactory.decodeResource(getResources(),R.drawable.bottle8);
        bottle[9]=BitmapFactory.decodeResource(getResources(),R.drawable.bottle9);

    }

    public void touched(MotionEvent mE){
        Rect touchPoint= new Rect((int)mE.getX()-1, (int)mE.getY()-1,(int)mE.getX()+1,(int)mE.getY()+1);
        if(buttonRect.contains(touchPoint)){
            tapCounter++;
            if(tapCounter>19){
                tapCounter= 0;
                bottleFillStep++;
            }
        }
    }

    public boolean isSurfaceView() {
        return true;
    }

    public void run() {
        long lastTime = System.nanoTime();//Wird für einen Timer benötigt.
        final double amountOfTicks =30.0;//Wie oft die Methode tick() in einer Sekunde aufgerufen werden soll.
        double ns = 1000000000 / amountOfTicks;//Berechnet wie viel Zeit vergeht bis die Methode tick() aufgerufen wird.
        double delta = 0;//Variable welche Berechnet, wann die tick() Methode aufgerufen werden soll.
        int updates = 0;//Wie oft das Progam die tick() Methode in einer Sekunde aufrufen hat.
        int frames = 0;//Wie oft das Program die Methode render() in einer Sekunde aufgerufen hat.
        long timer = System.currentTimeMillis();//Eine Variable um die Zeit zu Zählen. Ist für die Berechnung der "TicksPerSecond" und "FramesPerSecond" notwendig.

        while (running){
            if(!surfaceHolder.getSurface().isValid()){
                continue;
            }
            long now = System.nanoTime();//Timer Variable für die aktuelle Zeit.
            delta += (now - lastTime) / ns;//Berechnet mithilfe der Timer und ns Variablen, wann die tick() Methode aufgerufen werden soll.
            lastTime = now;//Stellt den Timer wieder zurück.
            if (delta >= 1) {//Guckt ob die Methode tick() jetzt aufgerufen werden soll.
                tick();//ruft die Methode tick() auf.
                updates++;//Addiert zum Update-Zähler 1 dazu.
                delta--;//Setzt den tick() Aufruf-Timer zurück.
                render();//ruft die render() Methode auf.
                frames++;////Addiert zum Frame-Zähler 1 dazu.
            }
            if (System.currentTimeMillis() - timer > 1000) {//Wenn eine Sekunde vergangen ist.
                timer += 1000;//addiert zum Timer eine Sekunde dazu.
                System.out.println("Ticks: " + updates + " FPS: " + frames);//Druckt die "TicksPerSecond" und "FramesPerSecond" aus.
                updates = 0;//setzt den Tick-Zähler zurück.
                frames = 0;//setzt den Frame-Zähler zurück.
            }
        }
    }
    private void tick(){

    }

    private void render(){
        canvas = surfaceHolder.lockCanvas();

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//Drawing a transparent Background to clear away old draws from the Tiles.
        canvas.drawBitmap(bottle[bottleFillStep],null,imageRect,null);
        canvas.drawBitmap(button,null,buttonRect,null);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public MiniGameType getMiniGameType() {
        return MiniGameType.EXBEER;
    }

    public void onPause() {
        running = false;

        try {
            thread.join();//Der Thread wird beendet.
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.exit(1);
    }


    public void onResume() {
        running = true;
        thread = new Thread(this);
        thread.start();//Der erstellte Thread wird gestartet.(Die Methode run() wird ausgeführt).
    }
}
