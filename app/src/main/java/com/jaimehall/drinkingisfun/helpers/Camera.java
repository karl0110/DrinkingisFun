package com.jaimehall.drinkingisfun.helpers;

import android.graphics.Rect;

import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.game.PlayerHandler;
import com.jaimehall.drinkingisfun.game.Tile;

public class Camera implements Runnable {

    private float width,height;

    private Thread thread;
    private boolean running;

    private Game game;
    private PlayerHandler playerHandler;

    private double maxZoom;
    private static final int zoomSpeed = 40;

    private int touchTimer;

    private Rect zoomButtonRenderingRect;
    private Rect playerIconRenderingRect;

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

    private Tile currentFocusedTile;
    private Tile nextFocusedTile;
    private float focusedTileWidth;
    private float focusedTileHeight;
    private float focusedScaleX;
    private float focusedScaleY;

    private float scaleX = 0;
    private float scaleY = 0;
    private float translateX = 0;
    private float translateY = 0;

    private CameraState cameraState;
    public enum CameraState{
        FOCUSED,FIRSTHALFFOCUSCHANGE,SECONDHALFFOCUSCHANGE,ZOOMINGIN,ZOOMINGOUT,ZOOMEDOUT
    }


    public Camera(Game game, PlayerHandler playerHandler,float width, float height){
        this.game=game;
        this.playerHandler=playerHandler;
        this.width=width;
        this.height=height;

        cameraState = CameraState.FOCUSED;

        playerIconRenderingRect = new Rect();
        zoomButtonRenderingRect= new Rect();
        currentFocusedTileChanged();

        maxZoom =height/(focusedTileHeight *5);


        zoomFactorXStepFocusChange = (focusedScaleX - maxZoom) / zoomSpeed;
        zoomFactorYStepFocusChange = (focusedScaleY - maxZoom) / zoomSpeed;

        xZoomTranslateVariable = ((focusedTileWidth / 2) * focusedScaleX);
        yZoomTranslateVariable = ((focusedTileHeight / 2) * focusedScaleY);

    }

    public void run(){
        long lastTime = System.nanoTime();//Wird für einen Timer benötigt.
        final double amountOfTicks = 30.0;//Wie oft die Methode tick() in einer Sekunde aufgerufen werden soll.
        double ns = 1000000000 / amountOfTicks;//Berechnet wie viel Zeit vergeht bis die Methode tick() aufgerufen wird.
        double delta = 0;//Variable welche Berechnet, wann die tick() Methode aufgerufen werden soll.
        int updates = 0;//Wie oft das Progam die tick() Methode in einer Sekunde aufrufen hat.
        long timer = System.currentTimeMillis();//Eine Variable um die Zeit zu Zählen. Ist für die Berechnung der "TicksPerSecond" und "FramesPerSecond" notwendig.

        while (running) {
            long now = System.nanoTime();//Timer Variable für die aktuelle Zeit.
            delta += (now - lastTime) / ns;//Berechnet mithilfe der Timer und ns Variablen, wann die tick() Methode aufgerufen werden soll.
            lastTime = now;//Stellt den Timer wieder zurück.
            if (delta >= 1) {//Guckt ob die Methode tick() jetzt aufgerufen werden soll.

                if(game.getGameState() == Game.State.MAINGAME) {
                    tick();//ruft die Methode tick() auf.
                }

                updates++;//Addiert zum Update-Zähler 1 dazu.
                delta--;//Setzt den tick() Aufruf-Timer zurück.

            }

            if (System.currentTimeMillis() - timer > 1000) {//Wenn eine Sekunde vergangen ist.
                timer += 1000;//addiert zum Timer eine Sekunde dazu.
                System.out.println("MainGame Ticks: " + updates);//Druckt die "TicksPerSecond" und "FramesPerSecond" aus.
                updates = 0;//setzt den Tick-Zähler zurück.
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

    private void tick(){
        playerHandler.tick();

        if (touchTimer >= 0) {
            touchTimer = (touchTimer-2);
        }

        if(cameraState == CameraState.FOCUSED){
            scaleX = focusedScaleX;
            scaleY = focusedScaleY;
            translateX = currentFocusedTile.getX();
            translateY = currentFocusedTile.getY();
        }
        else if (cameraState == CameraState.ZOOMEDOUT) {
            zoomButtonRenderingRect.set((int) (0 + translateX), 0, (int) (500 + translateX), 500);
        }
        else if(cameraState == CameraState.ZOOMINGOUT){
            if (frameZoomEvent < zoomSpeed) {

                actualXFocusChange += actualXStepFocusChange;
                actualYFocusChange += actualYStepFocusChange;

                zoomFactorXFocusChange -= zoomFactorXStepFocusChange;
                zoomFactorYFocusChange -= zoomFactorYStepFocusChange;

                scaleX = (float) zoomFactorXFocusChange;
                scaleY = (float) zoomFactorYFocusChange;
                translateX = (float) actualXFocusChange;
                translateY = (float) actualYFocusChange;

                frameZoomEvent++;
            } else {
                cameraState = CameraState.ZOOMEDOUT;
            }
        }
        else if(cameraState == CameraState.ZOOMINGIN){
            if (frameZoomEvent < zoomSpeed) {
                actualXFocusChange += actualXStepFocusChange;
                actualYFocusChange += actualYStepFocusChange;

                zoomFactorXFocusChange += zoomFactorXStepFocusChange;
                zoomFactorYFocusChange += zoomFactorYStepFocusChange;

                scaleX = (float) zoomFactorXFocusChange;
                scaleY = (float) zoomFactorYFocusChange;
                translateX = (float) actualXFocusChange;
                translateY = (float) actualYFocusChange;

                frameZoomEvent++;
            } else {
                currentFocusedTileChanged();
                cameraState = CameraState.FOCUSED;
            }
        }
        else if(cameraState == CameraState.FIRSTHALFFOCUSCHANGE){
            if (frameFocusChange < zoomSpeed) {
                zoomFactorXFocusChange -= zoomFactorXStepFocusChange;
                zoomFactorYFocusChange -= zoomFactorYStepFocusChange;

                actualXFocusChange += actualXStepFocusChange;
                actualYFocusChange += actualYStepFocusChange;

                scaleX = (float) zoomFactorXFocusChange;
                scaleY = (float) zoomFactorYFocusChange;
                translateX = (float) (actualXFocusChange - (xZoomTranslateVariable / zoomFactorXFocusChange));
                translateY = (float) (actualYFocusChange - (yZoomTranslateVariable / zoomFactorYFocusChange));
                frameFocusChange++;
            } else {

                cameraState = CameraState.SECONDHALFFOCUSCHANGE;
                frameFocusChange = 0;

            }
        }
        else if(cameraState == CameraState.SECONDHALFFOCUSCHANGE){
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
                cameraState = CameraState.FOCUSED;

                playerHandler.getCurrentPlayer().setLocation(currentFocusedTile.getNextTile());
                playerHandler.nextPlayer();
                currentFocusedTileChanged();
            }
        }
    }

    public void currentFocusedTileChanged(){
        currentFocusedTile = playerHandler.getCurrentPlayer().getLocation();
        nextFocusedTile = playerHandler.getNextPlayer().getLocation();

        focusedTileWidth = currentFocusedTile.getWidth();
        focusedTileHeight = currentFocusedTile.getHeight();

        focusedScaleX = width / focusedTileWidth;
        focusedScaleY = height / focusedTileHeight;

        float currentTileX = currentFocusedTile.getX();
        float currentTileY = currentFocusedTile.getY();

        zoomButtonRenderingRect.set((int) (currentTileX), (int) (currentTileY), (int) (currentTileX + (focusedTileWidth / 16)), (int) ((focusedTileHeight / 8) + currentTileY));
        playerIconRenderingRect.set((int) (currentTileX + ((focusedTileWidth / 16)*15)), (int) (currentTileY), (int)( currentTileX+focusedTileWidth), (int) ((focusedTileHeight / 8) + currentTileY));


    }

    public void prepareZoomEvent(){
        frameZoomEvent = 0;

        if(cameraState == CameraState.FOCUSED){
            float startXFocusChange = currentFocusedTile.getX();
            float startYFocusChange = currentFocusedTile.getY();
            float targetXFocusChange = currentFocusedTile.getX()/2;
            float targetYFocusChange = 0;
            zoomFactorXFocusChange = scaleX;
            zoomFactorYFocusChange = scaleY;

            actualXFocusChange = startXFocusChange;
            actualYFocusChange = startYFocusChange;

            actualXStepFocusChange = (targetXFocusChange - startXFocusChange) / (zoomSpeed);
            actualYStepFocusChange = (targetYFocusChange - startYFocusChange) / (zoomSpeed);

            cameraState = CameraState.ZOOMINGOUT;
        }
        if (cameraState == CameraState.ZOOMEDOUT){
            float startXFocusChange = translateX;
            float startYFocusChange = 0;
            float targetXFocusChange = currentFocusedTile.getX();
            float targetYFocusChange = currentFocusedTile.getY();
            zoomFactorXFocusChange = maxZoom;
            zoomFactorYFocusChange = maxZoom;

            actualXFocusChange = startXFocusChange;
            actualYFocusChange = startYFocusChange;

            actualXStepFocusChange = (targetXFocusChange - startXFocusChange) / (zoomSpeed);
            actualYStepFocusChange = (targetYFocusChange - startYFocusChange) / (zoomSpeed);

            cameraState = CameraState.ZOOMINGIN;
        }


    }


    public void prepareFocusChange(){
        frameFocusChange = 0;

        float startXFocusChange = currentFocusedTile.getCoordinates().centerX();
        float startYFocusChange = currentFocusedTile.getCoordinates().centerY();
        float targetXFocusChange = nextFocusedTile.getCoordinates().centerX();
        float targetYFocusChange = nextFocusedTile.getCoordinates().centerY();

        if (startXFocusChange == targetXFocusChange && startYFocusChange == targetYFocusChange) {

            cameraState = CameraState.FOCUSED;

            playerHandler.getCurrentPlayer().setLocation(currentFocusedTile.getNextTile());
            playerHandler.nextPlayer();

            currentFocusedTileChanged();


        } else {

            actualXFocusChange = currentFocusedTile.getCoordinates().centerX();
            actualYFocusChange = currentFocusedTile.getCoordinates().centerY();
            zoomFactorXFocusChange = focusedScaleX;
            zoomFactorYFocusChange = focusedScaleY;

            actualXStepFocusChange = (targetXFocusChange - startXFocusChange) / (zoomSpeed * 2);
            actualYStepFocusChange = (targetYFocusChange - startYFocusChange) / (zoomSpeed * 2);

            cameraState = CameraState.FIRSTHALFFOCUSCHANGE;
        }
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getTranslateX() {
        return translateX;
    }

    public float getTranslateY() {
        return translateY;
    }

    public void addToTranslateX(float amount) {
        this.translateX += amount;
    }

    public CameraState getCameraState() {
        return cameraState;
    }

    public Rect getZoomButtonRenderingRect() {
        return zoomButtonRenderingRect;
    }

    public Rect getPlayerIconRenderingRect() {
        return playerIconRenderingRect;
    }

    public void setCameraState(CameraState cameraState) {
        this.cameraState = cameraState;
    }

    public int getTouchTimer() {
        return touchTimer;
    }

    public void setTouchTimer(int touchTimer) {
        this.touchTimer = touchTimer;
    }

    public Tile getCurrentFocusedTile() {
        return currentFocusedTile;
    }

}
