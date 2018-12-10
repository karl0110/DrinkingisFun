package com.jaimehall.drinkingisfun.game;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jaimehall.drinkingisfun.activities.GameActivity;
import com.jaimehall.drinkingisfun.helpers.BitmapLoader;
import com.jaimehall.drinkingisfun.helpers.TextHandler;
import com.jaimehall.drinkingisfun.minigames.MiniGameHandler;

import java.util.LinkedList;

public class Game extends SurfaceView implements SurfaceHolder.Callback{

    private float width,height;

    private boolean initFinished=false;

    private String[] playerNames;
    private String[] playerIcons;
    private String[] playerSexes;

    private Camera camera;
    private Renderer renderer;
    private CameraRenderer cameraRenderer;

    private Context context;

    private LoadingScreen loadingScreen;

    private Rect playerIconTouchRect;
    private Rect zoomButtonRect;

    private PlayerHandler playerHandler;
    private MiniGameHandler miniGameHandler;

    private State gameState;

    public enum State {
        MAINGAME, MINIGAME, PLAYERMENU
    }



    public Game(Context context, String[] playerNames,String[] playerIcons, String[] playerSexes, float width, float height) {
        super(context);
        this.width=width;
        this.height=height;
        this.context = context;
        this.playerNames = playerNames;
        this.playerIcons = playerIcons;
        this.playerSexes=playerSexes;

        loadingScreen = new LoadingScreen(width,height,12);
        renderer = new Renderer(this,loadingScreen,width,height);


        getHolder().addCallback(this);

        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //renderer.resume();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }


    public void init(){
        if(!initFinished) {
            loadingScreen.progressOne();

            BitmapLoader bitmapLoader = new BitmapLoader(getResources());

            loadingScreen.progressOne();
            loadingScreen.progressOne();
            loadingScreen.progressOne();

            gameState = State.MAINGAME;

            playerHandler = new PlayerHandler( width, height, bitmapLoader,this);
            loadingScreen.progressOne();

            BackgroundHandler backgroundHandler = new BackgroundHandler(bitmapLoader,250,140);
            loadingScreen.progressOne();
            loadingScreen.progressOne();

            Map map = new Map(bitmapLoader);
            loadingScreen.progressOne();

            TextHandler textHandler = new TextHandler(this);
            loadingScreen.progressOne();

            for (int i = 0; i < playerNames.length; i++) {
                playerHandler.addPlayer(new Player(map.getTileFromTileMap(2, 4), playerNames[i], playerSexes[i], playerIcons[i], bitmapLoader, textHandler));
            }

            loadingScreen.progressOne();
            loadingScreen.progressOne();

            miniGameHandler = new MiniGameHandler(this, height, width, bitmapLoader);
            loadingScreen.progressOne();

            playerIconTouchRect = new Rect((int) (width-(width / 8) ), 0, (int) width, ((int) (height / 8)));

            zoomButtonRect = new Rect(0, 0, (int) (width / 8), ((int) (height / 8)));


            camera = new Camera(this, playerHandler, width, height);

            renderer.init( bitmapLoader,  playerHandler, camera, miniGameHandler, backgroundHandler,map);

            loadingScreen.progressOne();

            cameraRenderer = new CameraRenderer(renderer,camera,width,height,this);

            playerHandler.currentPlayerChanged();

            camera.init();


            initFinished = true;
            resume();
        }
    }

    public void finishGame(){


        LinkedList<Player> finishedPlayers = playerHandler.getPlayers();

        String[] playerNames = new String[finishedPlayers.size()];
        long[] playerScores = new long[finishedPlayers.size()];
        for(int i = 0; i<finishedPlayers.size();i++){
            playerNames[i] = finishedPlayers.get(i).getPlayerName();
            playerScores[i] = finishedPlayers.get(i).getScore();
        }
        ((GameActivity)getContext()).startGameOverAcitivty(playerNames,playerScores);

    }

    public void startMiniGame() {
        miniGameHandler.selectNewMiniGame();
        gameState = State.MINIGAME;
        ((GameActivity) context).setScreenOrientationPortrait();
    }

    public void finishMiniGame(int score) {
        Player tempPlayer = playerHandler.getCurrentPlayer();
        MiniGameTile tempTile = (MiniGameTile) playerHandler.getCurrentPlayer().getLocation();

        gameState = State.MAINGAME;
        camera.setTouchTimer(30);
        switch (tempTile.getTileDifficulty()) {
            case 0:
                tempPlayer.addToScore(Math.random()*100);
                if (score == 0) {
                    tempPlayer.setLocation(tempTile.getNextTile());
                } else {
                    tempPlayer.setLocation(tempTile.getNextHarderTile());
                }
                break;
            case 1:
                tempPlayer.addToScore(200+(Math.random()*100));
                if (score == 0) {
                    tempPlayer.setLocation(tempTile.getNextEasierTile());
                } else if (score == 2) {
                    tempPlayer.setLocation(tempTile.getNextHarderTile());
                } else {
                    tempPlayer.setLocation(tempTile.getNextTile());
                }
                break;
            case 2:
                tempPlayer.addToScore(400+(Math.random()*200));
                if (score == 2) {
                    tempPlayer.setLocation(tempTile.getNextTile());
                } else {
                    tempPlayer.setLocation(tempTile.getNextEasierTile());
                }
                break;
        }

        camera.setCameraState(Camera.CameraState.FOCUSED);

        ((GameActivity) context).setScreenOrientationLandscape();

        camera.currentFocusedTileChanged();
        nextPlayer();
        camera.currentFocusedTileChanged();


        camera.setTouchTimer(60);
    }

    public void fling(MotionEvent motionEvent, MotionEvent motionEvent1){
        if (camera.getCameraState() == Camera.CameraState.ZOOMEDOUT) {
            if (motionEvent.getX() > motionEvent1.getX()) {

                camera.setTranslateAccX(50);

                if((camera.getTranslateX()+cameraRenderer.getCameraRect().width())>6500){
                    camera.setTranslateX(6500);
                }
            } else if (motionEvent.getX() < motionEvent1.getX()) {

                camera.setTranslateAccX(-50);


            }
        }

    }

    public void nextPlayer(){

        playerHandler.nextPlayer();

        renderer.updateCurrentTask();

        cameraRenderer.updateCanvasBitmap();

    }

    public void progress(MotionEvent motionEvent){

            if (camera.getTouchTimer() == 0) {

                Rect touchPoint = new Rect((int) motionEvent.getX() - 1, (int) motionEvent.getY() - 1, (int) motionEvent.getX() + 1, (int) motionEvent.getY() + 1);
                if (gameState == State.MAINGAME) {
                    if (Rect.intersects(touchPoint, zoomButtonRect)) {
                        camera.prepareZoomEvent();
                    } else if (Rect.intersects(touchPoint, playerIconTouchRect)) {
                        gameState = State.PLAYERMENU;

                    } else if (camera.getCameraState() == Camera.CameraState.FOCUSED) {

                        if (playerHandler.getCurrentPlayer().getLocation().isMiniGame()) {
                            startMiniGame();
                        }
                        else if(playerHandler.getCurrentPlayer().getLocation().isGoal()){

                            finishGame();
                        }
                        else {
                            camera.prepareFocusChange();
                        }
                    }
                } else if (gameState == State.PLAYERMENU) {
                    if (Rect.intersects(touchPoint, playerIconTouchRect)) {
                        gameState = State.MAINGAME;
                    }
                    else {
                        playerHandler.touched(touchPoint);
                    }
                }


                camera.setTouchTimer(60);
            }

        if (gameState == State.MINIGAME) {
            miniGameHandler.touched(motionEvent);
            camera.setTouchTimer(60);
        }


    }


    public void pause() {

        camera.pause();
        renderer.pause();
        miniGameHandler.pause();
        cameraRenderer.pause();
    }


    public void resume() {
        if(initFinished) {
            camera.resume();
            renderer.resume();
            miniGameHandler.resume();
            cameraRenderer.resume();
        }

    }

    public State getGameState() {
        return gameState;
    }


}
