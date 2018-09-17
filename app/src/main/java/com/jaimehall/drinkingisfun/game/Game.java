package com.jaimehall.drinkingisfun.game;

import android.content.Context;
import android.content.Intent;
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
import com.jaimehall.drinkingisfun.activities.MiniGameHandlerActivity;

import java.util.ArrayList;

public class Game extends SurfaceView implements Runnable {

    public static final float WIDTH = 1920;
    public static final float HEIGHT=1080;
    private static final double maxZoom = 0.4;
    private static final int zoomSpeed = 30;

    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private Thread thread;
    private boolean running;

    private int touchTimer=0;
    private Rect zoomButtonRect;
    private Rect zoomButtonRenderingRect;
    private Bitmap zoomButtonZoomedOut;
    private Bitmap zoomButtonZoomedIn;

    private Map map;
    private PlayerHandler playerHandler;

    private boolean zoomEvent = false;
    private boolean zoomingIn = false;
    private boolean zoomingOut = false;
    private boolean zoomedOut = false;
    private boolean playerAction = false;
    private boolean focusChange = false;
    private boolean firstHalfFocusChange = false;
    private boolean secondHalfFocusChange = false;
    private double zoomFactorXFocusChange = 1.0;
    private double zoomFactorXStepFocusChange;
    private double zoomFactorYFocusChange = 1.0;
    private double zoomFactorYStepFocusChange;
    private double actualXFocusChange = 0;
    private double actualXStepFocusChange;
    private double actualYFocusChange = 0;
    private double actualYStepFocusChange;
    private double xZoomTranslateVariable;
    private double yZoomTranslateVariable;
    private int frameFocusChange = 0;
    private int frameZoomEvent = 0;

    private float scaleX = 0;
    private float scaleY = 0;
    private float translateX = 0;
    private float translateY = 0;

    private float deltaX1,deltaX2;

    public Game(Context context, ArrayList<String> playerNames,boolean[] playerSexes){
        super(context);
        surfaceHolder = getHolder();
        map = new Map(this);
        playerHandler = new PlayerHandler();
        for(int i =0;i<playerNames.size();i++){
            playerHandler.addPlayer(new Player(map.getTileFromTileMap(0, 3), playerNames.get(i),playerSexes[i]));
        }
        zoomButtonRect = new Rect(0,0,(int)(WIDTH/8),((int)(HEIGHT/8)));
        zoomButtonRenderingRect = new Rect();
        zoomButtonZoomedIn = BitmapFactory.decodeResource(getResources(),R.drawable.minuslupe);
        zoomButtonZoomedOut = BitmapFactory.decodeResource(getResources(),R.drawable.pluslupe);


        Tile currentFocusedTile = playerHandler.getCurrentPlayer().getLocation();

        float focusedTileWidth = currentFocusedTile.getCoordinates().width();
        float focusedTileHeight = currentFocusedTile.getCoordinates().height();

        float focusedScaleX = WIDTH / focusedTileWidth;
        float focusedScaleY = HEIGHT / focusedTileHeight;

        zoomFactorXStepFocusChange = (focusedScaleX - maxZoom) / zoomSpeed;
        zoomFactorYStepFocusChange = (focusedScaleY - maxZoom) / zoomSpeed;

        xZoomTranslateVariable = ((focusedTileWidth/2)*focusedScaleX);
        yZoomTranslateVariable = ((focusedTileHeight/2)*focusedScaleY);

    }

    public void startMiniGame(){
        Intent intent = new Intent(getContext(), MiniGameHandlerActivity.class);
        getContext().startActivity(intent);
    }

    public void progress(MotionEvent motionEvent){
        System.out.println("touched");
        Rect touchPoint = new Rect((int)motionEvent.getX()-5,(int)motionEvent.getY()-5,(int)motionEvent.getX()+5,(int)motionEvent.getY()+5);
        if(zoomButtonRect.contains(touchPoint)){
            zoomEvent=true;
        }
        else if(zoomedOut){
            switch(motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    deltaX1 = motionEvent.getX();
                break;
                case MotionEvent.ACTION_UP:
                    deltaX2= motionEvent.getX();
                    float deltaX = deltaX2-deltaX1;
                    if(Math.abs(deltaX)>50){
                        if(deltaX2>deltaX1){
                            if(translateX!=0)translateX-=400;
                        }
                        else{
                            translateX+=400;
                        }
                    }
                break;
            }
        }
        else if(touchTimer==0){
            if(!focusChange && !firstHalfFocusChange && !secondHalfFocusChange && !zoomEvent && !zoomingOut && !zoomingIn && !zoomedOut) {
                focusChange = true;
                touchTimer = 5;
            }
        }
    }

    private void tick(){
        Tile currentFocusedTile = playerHandler.getCurrentPlayer().getLocation();
        Tile nextFocusedTile = playerHandler.getNextPlayer().getLocation();

        float focusedTileWidth = currentFocusedTile.getCoordinates().width();
        float focusedTileHeight = currentFocusedTile.getCoordinates().height();

        float focusedScaleX = WIDTH / focusedTileWidth;
        float focusedScaleY = HEIGHT / focusedTileHeight;


        if(zoomEvent){

            frameZoomEvent = 0;


            float startXFocusChange;
            float startYFocusChange;
            float targetXFocusChange;
            float targetYFocusChange;

            if(zoomedOut){
                startXFocusChange = 0;
                startYFocusChange = -400;
                targetXFocusChange = currentFocusedTile.getX();
                targetYFocusChange = currentFocusedTile.getY();
                zoomFactorXFocusChange = maxZoom;
                zoomFactorYFocusChange = maxZoom;

                zoomEvent = false;
                zoomingIn = true;
                System.out.println("Preparing zoom in");
            }
            else{
                startXFocusChange = currentFocusedTile.getX();
                startYFocusChange = currentFocusedTile.getY();
                targetXFocusChange = 0;
                targetYFocusChange = -400;
                zoomFactorXFocusChange = focusedScaleX;
                zoomFactorYFocusChange = focusedScaleY;

                zoomEvent = false;
                zoomingOut = true;
                System.out.println("Preparing zoom out");
            }
            actualXFocusChange = startXFocusChange;
            actualYFocusChange = startYFocusChange;

            actualXStepFocusChange = (targetXFocusChange - startXFocusChange) / (zoomSpeed);
            actualYStepFocusChange = (targetYFocusChange - startYFocusChange) / (zoomSpeed);
        }
        else if(zoomingOut){
            if(frameZoomEvent<zoomSpeed){

                actualXFocusChange += actualXStepFocusChange;
                actualYFocusChange += actualYStepFocusChange;

                zoomFactorXFocusChange -= zoomFactorXStepFocusChange;
                zoomFactorYFocusChange -= zoomFactorYStepFocusChange;

                scaleX = (float) zoomFactorXFocusChange;
                scaleY = (float) zoomFactorYFocusChange;
                translateX = (float)actualXFocusChange;
                translateY = (float)actualYFocusChange;

                frameZoomEvent ++;
            }
            else{
                zoomingOut=false;
                zoomedOut = true;
                frameZoomEvent = 0;
                System.out.println("Finished zoom out");
            }
        }
        else if(zoomingIn){
            if(frameZoomEvent<zoomSpeed){
                actualXFocusChange += actualXStepFocusChange;
                actualYFocusChange += actualYStepFocusChange;

                zoomFactorXFocusChange += zoomFactorXStepFocusChange;
                zoomFactorYFocusChange += zoomFactorYStepFocusChange;

                scaleX = (float) zoomFactorXFocusChange;
                scaleY = (float) zoomFactorYFocusChange;
                translateX = (float)actualXFocusChange;
                translateY = (float)actualYFocusChange;

                frameZoomEvent ++;
            }
            else{
                zoomingIn = false;
                zoomedOut = false;
                frameZoomEvent = 0;
                System.out.println("Finished zoom in");
            }
        }
//        else if(zoomedOut){
//            scaleX=(float)maxZoom;
//            scaleY =(float)maxZoom;
//            translateX=0;
//            translateY=-400;
//        }
        else if (focusChange) {

            frameFocusChange = 0;

            float startXFocusChange = currentFocusedTile.getCoordinates().centerX();
            float startYFocusChange = currentFocusedTile.getCoordinates().centerY();
            float targetXFocusChange = nextFocusedTile.getCoordinates().centerX();
            float targetYFocusChange = nextFocusedTile.getCoordinates().centerY();


            if (startXFocusChange == targetXFocusChange && startYFocusChange == targetYFocusChange) {
                playerAction =true;


                focusChange = false;
                firstHalfFocusChange = false;
                playerHandler.getCurrentPlayer().setLocation(currentFocusedTile.getNextTile());
                playerHandler.nextPlayer();
            } else {
                playerAction = false;


                actualXFocusChange = currentFocusedTile.getCoordinates().centerX();
                actualYFocusChange = currentFocusedTile.getCoordinates().centerY();
                zoomFactorXFocusChange = focusedScaleX;
                zoomFactorYFocusChange = focusedScaleY;

                actualXStepFocusChange = (targetXFocusChange - startXFocusChange) / (zoomSpeed * 2);
                actualYStepFocusChange = (targetYFocusChange - startYFocusChange) / (zoomSpeed * 2);

                focusChange = false;
                firstHalfFocusChange = true;
            }
        }
        else if (firstHalfFocusChange) {

            if (frameFocusChange < zoomSpeed) {
                zoomFactorXFocusChange -= zoomFactorXStepFocusChange;
                zoomFactorYFocusChange -= zoomFactorYStepFocusChange;

                actualXFocusChange += actualXStepFocusChange;
                actualYFocusChange += actualYStepFocusChange;

                scaleX = (float) zoomFactorXFocusChange;
                scaleY = (float) zoomFactorYFocusChange;
                translateX = (float) (actualXFocusChange - ( xZoomTranslateVariable/ zoomFactorXFocusChange));
                translateY = (float) (actualYFocusChange - ( yZoomTranslateVariable/ zoomFactorYFocusChange));
                frameFocusChange++;
            } else {
                firstHalfFocusChange = false;
                secondHalfFocusChange = true;
                frameFocusChange = 0;

            }

        } else if (secondHalfFocusChange) {
            if (frameFocusChange < zoomSpeed) {

                zoomFactorXFocusChange += zoomFactorXStepFocusChange;
                zoomFactorYFocusChange += zoomFactorYStepFocusChange;

                actualXFocusChange += actualXStepFocusChange;
                actualYFocusChange += actualYStepFocusChange;

                scaleX = (float) zoomFactorXFocusChange;
                scaleY = (float) zoomFactorYFocusChange;
                translateX = (float) (actualXFocusChange - (xZoomTranslateVariable / zoomFactorXFocusChange));
                translateY = (float) (actualYFocusChange - (yZoomTranslateVariable / zoomFactorYFocusChange));
                frameFocusChange++;
            } else {
                secondHalfFocusChange = false;
                playerHandler.getCurrentPlayer().setLocation(currentFocusedTile.getNextTile());
                playerHandler.nextPlayer();

                playerAction = true;
            }
        } else if(!zoomedOut){

            scaleX = focusedScaleX;
            scaleY = focusedScaleY;
            translateX = currentFocusedTile.getX();
            translateY = currentFocusedTile.getY();
        }

        if(zoomedOut){
            zoomButtonRenderingRect.set((int)(0+translateX),-400,(int)(500+translateX),100);
        }
        else{

            zoomButtonRenderingRect.set((int)(currentFocusedTile.getX()),(int)(currentFocusedTile.getY()),(int)(currentFocusedTile.getX()+(focusedTileWidth/16)),(int)((focusedTileHeight/8)+currentFocusedTile.getY()));
        }
        if(touchTimer>0)touchTimer--;
    }

    private void render(){

        canvas = surfaceHolder.lockCanvas();



        /////Start of scaling and translating
        canvas.scale(scaleX,scaleY);
        canvas.translate(-translateX,-translateY);
        /////End of scaling and translating


        /////////Start of scaled and translated rendering
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//Drawing a transparent Background to clear away old draws from the Tiles.

        if (firstHalfFocusChange || secondHalfFocusChange || zoomingIn || zoomingOut || zoomedOut) {
            map.render(canvas);
        } else {
            Tile currentFocusedTile = playerHandler.getCurrentPlayer().getLocation();
            map.render(canvas, currentFocusedTile);

            canvas.drawBitmap(zoomButtonZoomedIn,null,zoomButtonRenderingRect,null);
        }

        if(playerAction){
            playerHandler.render(canvas,this);
        }
        if(zoomedOut){
            canvas.drawBitmap(zoomButtonZoomedOut,null,zoomButtonRenderingRect,null);
        }



        //////////End of Rendering
        //////Start of unscaled and unstranslated Rendering

        //////End
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void run() {
        long lastTime = System.nanoTime();//Wird für einen Timer benötigt.
        final double amountOfTicks =20.0;//Wie oft die Methode tick() in einer Sekunde aufgerufen werden soll.
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

    public void pause() {
        running = false;

        try {
            thread.join();//Der Thread wird beendet.
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.exit(1);
    }

    public void resume() {
        running = true;
        thread = new Thread(this);
        thread.start();//Der erstellte Thread wird gestartet.(Die Methode run() wird ausgeführt).
    }
}
