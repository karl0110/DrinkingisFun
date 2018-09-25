package com.jaimehall.drinkingisfun.game;

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
import com.jaimehall.drinkingisfun.activities.GameActivity;
import com.jaimehall.drinkingisfun.minigames.MiniGameHandler;

import java.util.ArrayList;

public class Game extends SurfaceView implements Runnable {

    private float width ;
    private float height;
    private double maxZoom;
    private static final int zoomSpeed = 30;

    private SurfaceHolder surfaceHolder;

    private Thread thread;
    private boolean running;
    private Context context;

    private int touchTimer=0;
    private Rect zoomButtonRect;
    private Rect zoomButtonRenderingRect;
    private Bitmap zoomButtonZoomedOut;
    private Bitmap zoomButtonZoomedIn;

    private Map map;
    private PlayerHandler playerHandler;
    private MiniGameHandler miniGameHandler;

    private boolean zoomEvent = false;
    private boolean zoomingIn = false;
    private boolean zoomingOut = false;
    private boolean zoomedOut = false;
    private boolean playerAction = true;
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

    private float deltaX1, deltaX2;
    private State gameState;
    private CameraState cameraState;

    private enum State {
        MAINGAME, MINIGAME
    }

    private enum CameraState {
        ZOOMING, FOCUSCHANGE, FOCUSED, ZOOMEDOUT
    }

    public Game(Context context, ArrayList<String> playerNames, boolean[] playerSexes, float width, float height) {
        super(context);

        this.width = height;
        this.height = width;

        this.context = context;
        surfaceHolder = getHolder();
        map = new Map(this);
        gameState = State.MAINGAME;
        cameraState = CameraState.FOCUSED;
        playerHandler = new PlayerHandler();
        miniGameHandler = new MiniGameHandler(this, width, height);

        for (int i = 0; i < playerNames.size(); i++) {
            playerHandler.addPlayer(new Player(map.getTileFromTileMap(0, 3), playerNames.get(i), playerSexes[i]));
        }
        zoomButtonRect = new Rect(0, 0, (int) (width / 8), ((int) (height / 8)));
        zoomButtonRenderingRect = new Rect();
        zoomButtonZoomedIn = BitmapFactory.decodeResource(getResources(), R.drawable.minuslupe);
        zoomButtonZoomedOut = BitmapFactory.decodeResource(getResources(), R.drawable.pluslupe);


        currentFocusedTile = playerHandler.getCurrentPlayer().getLocation();

        focusedTileWidth = currentFocusedTile.getCoordinates().width();
        focusedTileHeight = currentFocusedTile.getCoordinates().height();

        focusedScaleX = this.width / focusedTileWidth;
        focusedScaleY = this.height / focusedTileHeight;

        maxZoom = this.height / (focusedTileHeight * 7);

        zoomFactorXStepFocusChange = (focusedScaleX - maxZoom) / zoomSpeed;
        zoomFactorYStepFocusChange = (focusedScaleY - maxZoom) / zoomSpeed;

        xZoomTranslateVariable = ((focusedTileWidth / 2) * focusedScaleX);
        yZoomTranslateVariable = ((focusedTileHeight / 2) * focusedScaleY);


    }

    public void startMiniGame() {
        miniGameHandler.selectNewMiniGame();
        gameState = State.MINIGAME;
        ((GameActivity) context).setScreenOrientationPortrait();
        playerAction = false;
    }

    public void finishMiniGame(int score) {
        Player tempPlayer = playerHandler.getCurrentPlayer();
        MiniGameTile tempTile = (MiniGameTile) playerHandler.getCurrentPlayer().getLocation();

        ((GameActivity) context).setScreenOrientationLandscape();
        gameState = State.MAINGAME;
        touchTimer = 15;

        playerAction = true;
        switch (tempTile.getTileDifficulty()) {
            case 0:
                if (score == 0) {

                    tempPlayer.setLocation(tempTile.getNextTile());
                } else {
                    tempPlayer.setLocation(tempTile.getNextHarderTile());
                }
                break;
            case 1:
                if (score == 0) {
                    tempPlayer.setLocation(tempTile.getNextEasierTile());
                } else if (score == 2) {
                    tempPlayer.setLocation(tempTile.getNextHarderTile());
                } else {
                    tempPlayer.setLocation(tempTile.getNextTile());
                }
                break;
            case 2:
                if (score == 2) {
                    tempPlayer.setLocation(tempTile.getNextTile());
                } else {
                    tempPlayer.setLocation(tempTile.getNextEasierTile());
                }
                break;
        }

        playerHandler.nextPlayer();
    }

    public void progress(MotionEvent motionEvent) {
        if (gameState == State.MAINGAME) {
            Rect touchPoint = new Rect((int) motionEvent.getX() - 5, (int) motionEvent.getY() - 5, (int) motionEvent.getX() + 5, (int) motionEvent.getY() + 5);
            if (zoomButtonRect.contains(touchPoint)) {
                zoomEvent = true;
                cameraState = CameraState.ZOOMING;
            } else if (zoomedOut) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        deltaX1 = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        deltaX2 = motionEvent.getX();
                        float deltaX = deltaX2 - deltaX1;
                        if (Math.abs(deltaX) > 50) {
                            if (deltaX2 > deltaX1) {
                                if (translateX != 0) translateX -= 400;
                            } else {
                                translateX += 400;
                            }
                        }
                        break;
                }
            } else if (touchTimer == 0) {
                if (!focusChange && !firstHalfFocusChange && !secondHalfFocusChange && !zoomEvent && !zoomingOut && !zoomingIn && !zoomedOut) {
                    if (playerHandler.getCurrentPlayer().getLocation().isMiniGame) {
                        startMiniGame();
                    } else {
                        focusChange = true;
                        cameraState = CameraState.FOCUSCHANGE;
                    }

                    touchTimer = 5;
                }
            }
        } else if (gameState == State.MINIGAME) {
            miniGameHandler.touched(motionEvent);
        }
    }

    private void tick() {
        if (gameState == State.MAINGAME) {




            if (currentFocusedTile != playerHandler.getCurrentPlayer().getLocation() || currentFocusedTile ==null) {

                currentFocusedTile = playerHandler.getCurrentPlayer().getLocation();
                nextFocusedTile = playerHandler.getNextPlayer().getLocation();

                focusedTileWidth = currentFocusedTile.getCoordinates().width();
                focusedTileHeight = currentFocusedTile.getCoordinates().height();

                focusedScaleX = width / focusedTileWidth;
                focusedScaleY = height / focusedTileHeight;


            }

            if (cameraState == CameraState.FOCUSED) {
                if(translateX != currentFocusedTile.getX() || translateY != currentFocusedTile.getY()){
                    scaleX = focusedScaleX;
                    scaleY = focusedScaleY;
                    translateX = currentFocusedTile.getX();
                    translateY = currentFocusedTile.getY();

                    zoomButtonRenderingRect.set((int) (currentFocusedTile.getX()), (int) (currentFocusedTile.getY()), (int) (currentFocusedTile.getX() + (focusedTileWidth / 16)), (int) ((focusedTileHeight / 8) + currentFocusedTile.getY()));
                }

            }
            else if (cameraState == CameraState.ZOOMING) {
                if (zoomEvent) {

                    frameZoomEvent = 0;


                    float startXFocusChange;
                    float startYFocusChange;
                    float targetXFocusChange;
                    float targetYFocusChange;

                    if (zoomedOut) {
                        startXFocusChange = 0;
                        startYFocusChange = 0;
                        targetXFocusChange = currentFocusedTile.getX();
                        targetYFocusChange = currentFocusedTile.getY();
                        zoomFactorXFocusChange = maxZoom;
                        zoomFactorYFocusChange = maxZoom;

                        zoomEvent = false;
                        zoomingIn = true;
                        System.out.println("Preparing zoom in");
                    } else {
                        startXFocusChange = currentFocusedTile.getX();
                        startYFocusChange = currentFocusedTile.getY();
                        targetXFocusChange = 0;
                        targetYFocusChange = 0;
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
                } else if (zoomingOut) {
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
                        zoomingOut = false;
                        zoomedOut = true;
                        cameraState = CameraState.ZOOMEDOUT;
                        frameZoomEvent = 0;
                        System.out.println("Finished zoom out");
                    }
                } else if (zoomingIn) {
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
                        zoomingIn = false;
                        zoomedOut = false;
                        cameraState = CameraState.FOCUSED;
                        frameZoomEvent = 0;
                        System.out.println("Finished zoom in");
                    }
                }
            } else if (cameraState == CameraState.FOCUSCHANGE) {
                if (focusChange) {

                    frameFocusChange = 0;

                    float startXFocusChange = currentFocusedTile.getCoordinates().centerX();
                    float startYFocusChange = currentFocusedTile.getCoordinates().centerY();
                    float targetXFocusChange = nextFocusedTile.getCoordinates().centerX();
                    float targetYFocusChange = nextFocusedTile.getCoordinates().centerY();


                    if (startXFocusChange == targetXFocusChange && startYFocusChange == targetYFocusChange) {
                        playerAction = true;
                        focusChange = false;
                        firstHalfFocusChange = false;
                        cameraState = CameraState.FOCUSED;
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
                } else if (firstHalfFocusChange) {

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
                        cameraState = CameraState.FOCUSED;
                        playerHandler.getCurrentPlayer().setLocation(currentFocusedTile.getNextTile());
                        playerHandler.nextPlayer();


                        playerAction = true;
                    }
                }
            }
            else if (cameraState == CameraState.ZOOMEDOUT) {
                zoomButtonRenderingRect.set((int) (0 + translateX), 0, (int) (500 + translateX), 500);
            }

            if (touchTimer > 0) touchTimer--;

        } else if (gameState == State.MINIGAME) {
            miniGameHandler.tick();

        }
    }


    private void render() {

        Canvas canvas = surfaceHolder.lockCanvas();


        if (gameState == State.MAINGAME) {
            /////Start of scaling and translating
            canvas.scale(scaleX, scaleY);
            canvas.translate(-translateX, -translateY);
            /////End of scaling and translating


            /////////Start of scaled and translated rendering
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//Drawing a transparent Background to clear away old draws from the Tiles.

            if(cameraState == CameraState.FOCUSED){
                map.render(canvas, currentFocusedTile);

                canvas.drawBitmap(zoomButtonZoomedIn, null, zoomButtonRenderingRect, null);
            }
            else {
                map.render(canvas);
            }

            if (playerAction) {
                playerHandler.render(canvas, this);
            }
            if (cameraState == CameraState.ZOOMEDOUT) {
                canvas.drawBitmap(zoomButtonZoomedOut, null, zoomButtonRenderingRect, null);
            }
        } else if (gameState == State.MINIGAME) {

            miniGameHandler.render(canvas);
        }


        //////////End of Rendering
        //////Start of unscaled and unstranslated Rendering

        //////End
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void run() {
        long lastTime = System.nanoTime();//Wird für einen Timer benötigt.
        final double amountOfTicks = 30.0;//Wie oft die Methode tick() in einer Sekunde aufgerufen werden soll.
        double ns = 1000000000 / amountOfTicks;//Berechnet wie viel Zeit vergeht bis die Methode tick() aufgerufen wird.
        double delta = 0;//Variable welche Berechnet, wann die tick() Methode aufgerufen werden soll.
        int updates = 0;//Wie oft das Progam die tick() Methode in einer Sekunde aufrufen hat.
        int frames = 0;//Wie oft das Program die Methode render() in einer Sekunde aufgerufen hat.
        long timer = System.currentTimeMillis();//Eine Variable um die Zeit zu Zählen. Ist für die Berechnung der "TicksPerSecond" und "FramesPerSecond" notwendig.

        while (running) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }
            long now = System.nanoTime();//Timer Variable für die aktuelle Zeit.
            delta += (now - lastTime) / ns;//Berechnet mithilfe der Timer und ns Variablen, wann die tick() Methode aufgerufen werden soll.
            lastTime = now;//Stellt den Timer wieder zurück.
            if (delta >= 1) {//Guckt ob die Methode tick() jetzt aufgerufen werden soll.
                tick();//ruft die Methode tick() auf.
                updates++;//Addiert zum Update-Zähler 1 dazu.
                delta--;//Setzt den tick() Aufruf-Timer zurück.

                frames++;////Addiert zum Frame-Zähler 1 dazu.
            }
            render();//ruft die render() Methode auf.
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
    }

    public void resume() {
        running = true;
        thread = new Thread(this);
        thread.start();//Der erstellte Thread wird gestartet.(Die Methode run() wird ausgeführt).
    }

}
