package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.helpers.BitmapLoader;
import com.jaimehall.drinkingisfun.minigames.MiniGameHandler;

public class Renderer implements Runnable {

    private Thread thread;
    private boolean running;
    private BackgroundHandler backgroundHandler;

    private SurfaceHolder surfaceHolder;
    private Game game;
    private PlayerHandler playerHandler;
    private Camera camera;
    private MiniGameHandler miniGameHandler;

    private Bitmap zoomButtonZoomedOut;
    private Bitmap zoomButtonZoomedIn;

    private Bitmap showPlayers;

    private boolean initPhase = true;
    private LoadingScreen loadingScreen;


    Renderer(Game game,LoadingScreen loadingScreen){
        this.surfaceHolder = game.getHolder();
        surfaceHolder.setFormat(0x00000004);
        this.loadingScreen = loadingScreen;
        this.game = game;

    }

    void init( BitmapLoader bitmapLoader, PlayerHandler playerHandler, Camera camera, MiniGameHandler miniGameHandler, BackgroundHandler backgroundHandler){
        if(initPhase) {

            this.playerHandler = playerHandler;
            this.camera = camera;
            this.miniGameHandler = miniGameHandler;
            this.backgroundHandler = backgroundHandler;
            backgroundHandler.setCamera(camera);

            showPlayers = bitmapLoader.getBitmap(R.drawable.spieleranzeigen, 100, 100);

            zoomButtonZoomedIn = bitmapLoader.getBitmap(R.drawable.minuslupe, 100, 100);
            zoomButtonZoomedOut = bitmapLoader.getBitmap(R.drawable.pluslupe, 1000, 1000);

            initPhase = false;
        }
    }

    private void render(){

            Canvas canvas  = surfaceHolder.lockCanvas();

            if(canvas !=null) {

                if(initPhase){
                    loadingScreen.render(canvas);
                }
                else {

                    if (game.getGameState() == Game.State.MAINGAME) {
                        /////Start of scaling and translating
                        canvas.scale(camera.getScaleX(), camera.getScaleY());
                        canvas.translate(-camera.getTranslateX(), -camera.getTranslateY());
                        /////End of scaling and translating


                        /////////Start of scaled and translated rendering

                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//Drawing a transparent Background to clear away old draws from the Tiles.
                        if (camera.getCameraState() == Camera.CameraState.FOCUSED) {

                            Tile currentFocusedTile = camera.getCurrentFocusedTile();

                            int x = (int) (currentFocusedTile.getX() / currentFocusedTile.getWidth());
                            backgroundHandler.renderFocusedTile(canvas, x);

                            camera.getCurrentFocusedTile().renderText(canvas, playerHandler.getCurrentPlayer());

                            playerHandler.renderPlayerIconsOnCurrentTile(canvas);

                            canvas.drawBitmap(zoomButtonZoomedIn, null, camera.getZoomButtonRenderingRect(), null);
                            canvas.drawBitmap(showPlayers, null, camera.getPlayerIconRenderingRect(), null);

                        } else if (camera.getCameraState() == Camera.CameraState.ZOOMEDOUT) {
                            backgroundHandler.render(canvas);
                            playerHandler.renderPlayerIconsWholeMap(canvas);

                            canvas.drawBitmap(zoomButtonZoomedOut, null, camera.getZoomButtonRenderingRect(), null);
                        } else {
                            backgroundHandler.render(canvas);
                            playerHandler.renderPlayerIconsWholeMap(canvas);
                        }

                    } else if (game.getGameState() == Game.State.MINIGAME) {
                        miniGameHandler.render(canvas);
                    } else if (game.getGameState() == Game.State.PLAYERMENU) {
                        playerHandler.renderPlayerMenu(canvas);

                    }
                }



                //////////End of Rendering
                //////Start of unscaled and unstranslated Rendering

                //////End
                surfaceHolder.unlockCanvasAndPost(canvas);
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
                System.out.println(" FPS: " + frames);//Druckt die "TicksPerSecond" und "FramesPerSecond" aus.
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

}
