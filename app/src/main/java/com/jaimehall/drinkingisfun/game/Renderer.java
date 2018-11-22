package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.helpers.BitmapLoader;
import com.jaimehall.drinkingisfun.helpers.TextRect;
import com.jaimehall.drinkingisfun.minigames.MiniGameHandler;

public class Renderer implements Runnable {

    private float width,height;

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

    private Bitmap renderBitmap;

    private boolean initPhase = true;
    private LoadingScreen loadingScreen;

    private Rect zoomButtonRenderingRect;
    private Rect playerIconRenderingRect;

    private TextRect textRect;
    private String currentTask = "";
    private int textRectPrep;


    Renderer(Game game,LoadingScreen loadingScreen,float width,float height){
        this.surfaceHolder = game.getHolder();
        surfaceHolder.setFormat(0x00000004);
        this.loadingScreen = loadingScreen;
        this.game = game;
        this.width=width;
        this.height=height;

        zoomButtonRenderingRect =new Rect(0,0,(int)(width/16),(int)(height/9));
        playerIconRenderingRect = new Rect((int)(width-(width/16)),0,(int)width,(int)(height/9));

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setFakeBoldText(true);
        paint.setTextSize(15);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        textRect = new TextRect(paint);


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

            updateCurrentTask();


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

                        if(renderBitmap != null) {
                            canvas.drawBitmap(renderBitmap, 0, 0, null);
                        }

                        if (camera.getCameraState() == Camera.CameraState.FOCUSED) {



                            canvas.drawBitmap(zoomButtonZoomedIn, null, zoomButtonRenderingRect, null);
                            canvas.drawBitmap(showPlayers, null, playerIconRenderingRect, null);

                            if(textRectPrep != textRect.prepare(currentTask, (int) (width - ((width / 32) * 6)), (int) (height - ((height / 32) * 12)))){
                                textRectPrep = textRect.prepare(currentTask, (int) (width - ((width / 32) * 6)), (int) (height - ((height / 32) * 12)));
                            }
                            textRect.draw(canvas,  0,  0);

                        }
                        else if(camera.getCameraState() == Camera.CameraState.ZOOMEDOUT){
                            canvas.drawBitmap(zoomButtonZoomedOut, null, zoomButtonRenderingRect, null);
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

    public Bitmap getRenderedBitmap(Rect spaceToDraw){

        Bitmap.Config conf = Bitmap.Config.RGB_565;
        Bitmap returnBitmap = Bitmap.createBitmap(spaceToDraw.width(),spaceToDraw.height(),conf);

        Canvas canvas = new Canvas(returnBitmap);

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//Drawing a transparent Background to clear away old draws from the Tiles.

        canvas.translate(-spaceToDraw.left,-spaceToDraw.top);

        backgroundHandler.render(canvas,spaceToDraw);

        if (camera.getCameraState() == Camera.CameraState.FOCUSED) {

            playerHandler.renderPlayerIconsOnCurrentTile(canvas);

        }
         else {
            playerHandler.renderPlayerIconsWholeMap(canvas);
        }

        return returnBitmap;
    }


    public void updateCurrentTask(){
        currentTask = playerHandler.getCurrentPlayer().getTask();

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

    public void setRenderBitmap(Bitmap renderBitmap) {
        this.renderBitmap = renderBitmap;
    }
}
