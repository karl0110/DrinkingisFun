package com.jaimehall.drinkingisfun.game;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.preference.PreferenceManager;

import com.jaimehall.drinkingisfun.activities.menu.SettingsActivity;

public class Camera implements Runnable{

    private float width,height;

    private Thread thread;
    private boolean running;

    private Game game;
    private PlayerHandler playerHandler;

    private double maxZoom;
    private float maxWidth = 6500;
    private float maxHeight = 1250;
    private static int zoomSpeed;

    private int touchTimer;



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


    Camera(Game game, PlayerHandler playerHandler,float width, float height){
        this.game=game;
        this.playerHandler=playerHandler;
        this.width=width;
        this.height=height;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(game.getContext());

        double zoomSeconds = Double.parseDouble(sharedPreferences.getString(SettingsActivity.KEY_PREF_ZOOM_SPEED,"" ));
        zoomSpeed = (int)(30*zoomSeconds);


        cameraState = CameraState.FOCUSED;

    }

    public void init(){
        currentFocusedTileChanged();

        maxZoom =height/(focusedTileHeight *9);

        zoomFactorXStepFocusChange = (focusedScaleX - maxZoom) / zoomSpeed;
        zoomFactorYStepFocusChange = (focusedScaleY - maxZoom) / zoomSpeed;

        xZoomTranslateVariable = ((focusedTileWidth / 2) * focusedScaleX);
        yZoomTranslateVariable = ((focusedTileHeight / 2) * focusedScaleY);

        scaleX = focusedScaleX;
        scaleY = focusedScaleY;
        translateX = currentFocusedTile.getX();
        translateY = currentFocusedTile.getY();
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
                    tickMainGame();//ruft die Methode tick() auf.
                }
                generalTick();

                updates++;//Addiert zum Update-Zähler 1 dazu.
                delta--;//Setzt den tick() Aufruf-Timer zurück.

            }

            if (System.currentTimeMillis() - timer > 1000) {//Wenn eine Sekunde vergangen ist.
                timer += 1000;//addiert zum Timer eine Sekunde dazu.
                if(game.getGameState() == Game.State.MAINGAME) {
                    System.out.println("MainGame Ticks: " + updates);//Druckt die "TicksPerSecond" und "FramesPerSecond" aus.
                }
                updates = 0;//setzt den Tick-Zähler zurück.
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

    private void generalTick(){
        if (touchTimer > 0) {
            touchTimer--;
        }
    }

    private void tickMainGame(){

        float resultScaleX=scaleX;
        float resultScaleY=scaleY;
        float resultTranslateX=translateX;
        float resultTranslateY =translateY;

        if(cameraState == CameraState.FOCUSED){
            resultScaleX = focusedScaleX;
            resultScaleY = focusedScaleY;
            resultTranslateX = currentFocusedTile.getX();
            resultTranslateY = currentFocusedTile.getY();
        }
        else if(cameraState == CameraState.ZOOMINGOUT){
            if (frameZoomEvent < zoomSpeed) {

                actualXFocusChange += actualXStepFocusChange;
                actualYFocusChange += actualYStepFocusChange;

                zoomFactorXFocusChange -= zoomFactorXStepFocusChange;
                zoomFactorYFocusChange -= zoomFactorYStepFocusChange;

                resultScaleX = (float) zoomFactorXFocusChange;
                resultScaleY = (float) zoomFactorYFocusChange;
                resultTranslateX = (float) actualXFocusChange;
                resultTranslateY = (float) actualYFocusChange;

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

                resultScaleX = (float) zoomFactorXFocusChange;
                resultScaleY = (float) zoomFactorYFocusChange;
                resultTranslateX = (float) actualXFocusChange;
                resultTranslateY = (float) actualYFocusChange;

                frameZoomEvent++;
            } else {
                cameraState = CameraState.FOCUSED;
                currentFocusedTileChanged();
            }
        }
        else if(cameraState == CameraState.FIRSTHALFFOCUSCHANGE){
            if (frameFocusChange < zoomSpeed) {
                zoomFactorXFocusChange -= zoomFactorXStepFocusChange;
                zoomFactorYFocusChange -= zoomFactorYStepFocusChange;

                actualXFocusChange += actualXStepFocusChange;
                actualYFocusChange += actualYStepFocusChange;

                resultScaleX = (float) zoomFactorXFocusChange;
                resultScaleY = (float) zoomFactorYFocusChange;
                resultTranslateX = (float) (actualXFocusChange - (xZoomTranslateVariable / zoomFactorXFocusChange));
                resultTranslateY = (float) (actualYFocusChange - (yZoomTranslateVariable / zoomFactorYFocusChange));
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

                resultScaleX = (float) zoomFactorXFocusChange;
                resultScaleY = (float) zoomFactorYFocusChange;
                resultTranslateX = (float) (actualXFocusChange - (xZoomTranslateVariable / zoomFactorXFocusChange));
                resultTranslateY = (float) (actualYFocusChange - (yZoomTranslateVariable / zoomFactorYFocusChange));
                frameFocusChange++;
            } else {
                cameraState = CameraState.FOCUSED;
                playerHandler.getCurrentPlayer().setLocation(currentFocusedTile.getNextTile());
                currentFocusedTileChanged();
                game.nextPlayer();
                currentFocusedTileChanged();
            }
        }

        translateX=resultTranslateX;
        translateY=resultTranslateY;
        scaleX=resultScaleX;
        scaleY=resultScaleY;

    }

    void currentFocusedTileChanged(){
        currentFocusedTile = playerHandler.getCurrentPlayer().getLocation();
        nextFocusedTile = playerHandler.getNextPlayer().getLocation();

        focusedTileWidth = currentFocusedTile.getWidth();
        focusedTileHeight = currentFocusedTile.getHeight();


        focusedScaleX = width / focusedTileWidth;
        focusedScaleY = height / focusedTileHeight;


    }

    void prepareZoomEvent(){
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
            float startYFocusChange = translateY;
            float targetXFocusChange = currentFocusedTile.getX();
            float targetYFocusChange = currentFocusedTile.getY();
            zoomFactorXFocusChange = scaleX;
            zoomFactorYFocusChange = scaleY;

            actualXFocusChange = startXFocusChange;
            actualYFocusChange = startYFocusChange;

            actualXStepFocusChange = (targetXFocusChange - startXFocusChange) / (zoomSpeed);
            actualYStepFocusChange = (targetYFocusChange - startYFocusChange) / (zoomSpeed);

            cameraState = CameraState.ZOOMINGIN;
        }


    }


    void prepareFocusChange(){
        frameFocusChange = 0;

        float startXFocusChange = currentFocusedTile.getCoordinates().centerX();
        float startYFocusChange = currentFocusedTile.getCoordinates().centerY();
        float targetXFocusChange = nextFocusedTile.getCoordinates().centerX();
        float targetYFocusChange = nextFocusedTile.getCoordinates().centerY();


        if (startXFocusChange == targetXFocusChange && startYFocusChange == targetYFocusChange) {

            cameraState = CameraState.FOCUSED;

            playerHandler.getCurrentPlayer().setLocation(currentFocusedTile.getNextTile());
            currentFocusedTileChanged();
            game.nextPlayer();
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


    float getScaleX() {
        return scaleX;
    }

    float getScaleY() {
        return scaleY;
    }

    float getTranslateX() {
        return translateX;
    }

    float getTranslateY() {
        return translateY;
    }

    void addToTranslateX(float amount) {

        float tempTranslateX = translateX;

        tempTranslateX += (amount/scaleX);

        if(tempTranslateX > maxWidth){
            tempTranslateX = (maxWidth-1)- ((width/scaleX));
        }

        if(tempTranslateX <0) {
            tempTranslateX = 0;
        }

        this.translateX = tempTranslateX;
    }
    void addToTranslateY(float amount) {

        float tempTranslateY = translateY;

        tempTranslateY += (amount/scaleY);

        if(tempTranslateY > maxHeight){
            tempTranslateY = (maxHeight-1)- ((height/scaleY));
        }

        if(tempTranslateY <0){
            tempTranslateY =0;
        }

        this.translateY = tempTranslateY;
    }

    CameraState getCameraState() {
        return cameraState;
    }

    void setCameraState(CameraState cameraState) {
        this.cameraState = cameraState;
    }

    int getTouchTimer() {
        return touchTimer;
    }

    void setTouchTimer(int touchTimer) {
        this.touchTimer = touchTimer;
    }

    public void setScale(float scaleFactor){
        float tempScaleX = scaleX *= scaleFactor;

        tempScaleX = Math.max((float)maxZoom, Math.min(tempScaleX, focusedScaleX/2));

        scaleX = tempScaleX;



        float tempScaleY = scaleY *= scaleFactor;

        tempScaleY = Math.max((float)maxZoom, Math.min(tempScaleY, focusedScaleY/2));

        scaleY = tempScaleY;
    }
}
