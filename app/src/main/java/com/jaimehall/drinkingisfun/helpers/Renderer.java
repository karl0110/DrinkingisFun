package com.jaimehall.drinkingisfun.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.game.Map;
import com.jaimehall.drinkingisfun.game.PlayerHandler;
import com.jaimehall.drinkingisfun.minigames.MiniGameHandler;

public class Renderer implements Runnable {

    private Thread thread;
    private boolean running;

    private SurfaceHolder surfaceHolder;
    private Game game;
    private Map map;
    private PlayerHandler playerHandler;
    private Camera camera;
    private MiniGameHandler miniGameHandler;

    private Bitmap zoomButtonZoomedOut;
    private Bitmap zoomButtonZoomedIn;

    private Bitmap showPlayers;
    private Bitmap hidePlayers;


    public Renderer(Game game,SurfaceHolder surfaceHolder, Map map, PlayerHandler playerHandler,Camera camera,MiniGameHandler miniGameHandler){
        this.game=game;
        this.surfaceHolder = surfaceHolder;
        this.map=map;
        this.playerHandler=playerHandler;
        this.camera=camera;
        this.miniGameHandler = miniGameHandler;

        Resources resources = game.getResources();

        showPlayers = BitmapFactory.decodeResource(resources, R.drawable.spieleranzeigen);
        hidePlayers = BitmapFactory.decodeResource(resources, R.drawable.spielerverstecken);

        zoomButtonZoomedIn = BitmapFactory.decodeResource(resources, R.drawable.minuslupe);
        zoomButtonZoomedOut = BitmapFactory.decodeResource(resources, R.drawable.pluslupe);

    }

    private void render(){
        if(surfaceHolder.getSurface().isValid()) {
            Canvas canvas  = surfaceHolder.lockCanvas();

            if (game.getGameState() == Game.State.MAINGAME) {
                /////Start of scaling and translating
                canvas.scale(camera.getScaleX(), camera.getScaleY());
                canvas.translate(-camera.getTranslateX(), -camera.getTranslateY());
                /////End of scaling and translating


                /////////Start of scaled and translated rendering

                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//Drawing a transparent Background to clear away old draws from the Tiles.
                if (camera.getCameraState() == Camera.CameraState.FOCUSED) {

                    map.render(canvas, camera.getCurrentFocusedTile());

                    camera.getCurrentFocusedTile().renderText(canvas, playerHandler.getCurrentPlayer());

                    playerHandler.renderPlayerIcons(canvas);

                    canvas.drawBitmap(zoomButtonZoomedIn, null, camera.getZoomButtonRenderingRect(), null);
                    canvas.drawBitmap(showPlayers, null, camera.getPlayerIconRenderingRect(), null);

                } else if (camera.getCameraState() == Camera.CameraState.ZOOMEDOUT) {
                    map.render(canvas);
                    playerHandler.renderPlayerIcons(canvas);

                    canvas.drawBitmap(zoomButtonZoomedOut, null, camera.getZoomButtonRenderingRect(), null);
                } else {
                    map.render(canvas);
                    playerHandler.renderPlayerIcons(canvas);
                }

            } else if (game.getGameState() == Game.State.MINIGAME) {
                miniGameHandler.render(canvas);
            } else if (game.getGameState() == Game.State.PLAYERMENU) {
                playerHandler.renderPlayerMenu(canvas);

                canvas.drawBitmap(hidePlayers, null, camera.getPlayerIconRenderingRect(), null);
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

}
