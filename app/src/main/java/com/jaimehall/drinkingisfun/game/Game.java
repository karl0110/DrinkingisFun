package com.jaimehall.drinkingisfun.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.activities.GameActivity;
import com.jaimehall.drinkingisfun.helpers.BitmapLoader;
import com.jaimehall.drinkingisfun.helpers.Camera;
import com.jaimehall.drinkingisfun.helpers.Renderer;
import com.jaimehall.drinkingisfun.minigames.MiniGameHandler;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game extends SurfaceView {

    private float width,height;

    private Camera camera;
    private Renderer renderer;
    private BitmapLoader bitmapLoader;

    private Context context;


    private Rect playerIconTouchRect;
    private Rect zoomButtonRect;

    private PlayerHandler playerHandler;
    private MiniGameHandler miniGameHandler;

    private State gameState;

    public enum State {
        MAINGAME, MINIGAME, PLAYERMENU
    }



    public Game(Context context, String[] playerNames,String[] playerIcons, boolean[] playerSexes, float width, float height) {
        super(context);
        this.width=width;
        this.height=height;

        this.context = context;

        bitmapLoader = new BitmapLoader(getResources());

        Map map = new Map(this,bitmapLoader);
        gameState = State.MAINGAME;

        playerHandler = new PlayerHandler(this,width,height, bitmapLoader);


        for (int i = 0; i < playerNames.length; i++) {
            playerHandler.addPlayer(new Player(map.getTileFromTileMap(0, 3), playerNames[i], playerSexes[i],playerIcons[i],bitmapLoader));
        }
        playerHandler.currentPlayerChanged();

        miniGameHandler = new MiniGameHandler(this, height, width,bitmapLoader);

        playerIconTouchRect = new  Rect((int) ((width / 8)*7), 0, (int) width, ((int) (height / 8)));

        zoomButtonRect = new Rect(0, 0, (int) (width / 8), ((int) (height / 8)));

        camera = new Camera(this,playerHandler,width,height);
        renderer = new Renderer(this, bitmapLoader,getHolder(),map,playerHandler,camera,miniGameHandler);



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

        playerHandler.nextPlayer();
        camera.currentFocusedTileChanged();
        camera.setCameraState(Camera.CameraState.FOCUSED);

        ((GameActivity) context).setScreenOrientationLandscape();
    }

    public void fling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1){
        if (camera.getCameraState() == Camera.CameraState.ZOOMEDOUT) {
            if (motionEvent.getX() > motionEvent1.getX()) {
                camera.addToTranslateX(400);
            } else if (motionEvent.getX() < motionEvent1.getX()) {

                if (camera.getTranslateX() != 0) {
                    camera.addToTranslateX(-400);
                }
            }
        }

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

                camera.setTouchTimer(20);
            }

            if (gameState == State.MINIGAME) {
                miniGameHandler.touched(motionEvent);
            }
        }


    public void pause() {

        camera.pause();
        renderer.pause();
        miniGameHandler.pause();
    }

    public void resume() {
        renderer.resume();
        camera.resume();
        miniGameHandler.resume();


    }

    public State getGameState() {
        return gameState;
    }

    public float getScreenWidth() {
        return width;
    }

    public float getScreenHeight() {
        return height;
    }

}
