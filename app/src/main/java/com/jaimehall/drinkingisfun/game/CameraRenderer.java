package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class CameraRenderer implements Runnable {

    private float width,height;
    private Renderer renderer;
    private Camera camera;

    private Thread thread;
    private boolean running;

    private Bitmap cameraBitmap;

    private Rect cameraRect;

    public CameraRenderer(Renderer renderer,Camera camera,float width,float height) {
        this.width=width;
        this.height=height;
        this.renderer=renderer;
        this.camera=camera;
        cameraRect = new Rect();
    }



    private void render(){

        float scaleX = camera.getScaleX();
        float scaleY =camera.getScaleY();
        float translateX = camera.getTranslateX();
        float translateY = camera.getTranslateY();

        float x = width/scaleX;
        float y = height/scaleX;

        if((width/scaleX) > 0 &&(width/scaleX) < 6500) {

            cameraRect.set((int) translateX, (int) translateY, (int) (translateX + x), (int) (translateY + y));

            Matrix m = new Matrix();

            m.setTranslate(-translateX, -translateY);
            m.setScale(scaleX, scaleY);

            cameraBitmap= Bitmap.createBitmap(renderer.getRenderedBitmap(), cameraRect.left, cameraRect.top, cameraRect.width(), cameraRect.height(),m,false);

            renderer.setRenderBitmap(cameraBitmap);
        }

    }

    public void run(){


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
                delta--;//Setzt den tick() Aufruf-Timer zurück.
                frames++;////Addiert zum Frame-Zähler 1 dazu.
            }

            if (System.currentTimeMillis() - timer > 1000) {//Wenn eine Sekunde vergangen ist.
                timer += 1000;//addiert zum Timer eine Sekunde dazu.
                System.out.println(" CameraRendererFPS: " + frames);//Druckt die "TicksPerSecond" und "FramesPerSecond" aus.
                frames = 0;//setzt den Frame-Zähler zurück.
            }
        }
    }

    void pause(){
        running = false;

        try {
            thread.join();//Der Thread wird beendet.
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    void resume(){
        running = true;
        thread = new Thread(this);
        thread.start();//Der erstellte Thread wird gestartet.(Die Methode run() wird ausgeführt).

    }


    public Rect getCameraRect() {
        return cameraRect;
    }
}
