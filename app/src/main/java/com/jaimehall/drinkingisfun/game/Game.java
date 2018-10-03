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
import com.jaimehall.drinkingisfun.helpers.Camera;
import com.jaimehall.drinkingisfun.helpers.Renderer;
import com.jaimehall.drinkingisfun.minigames.MiniGameHandler;

import java.util.ArrayList;

public class Game extends SurfaceView {

    private Camera camera;
    private Renderer renderer;

    private Context context;


    private Rect playerIconTouchRect;
    private Rect zoomButtonRect;

    private PlayerHandler playerHandler;
    private MiniGameHandler miniGameHandler;

    private float deltaX1, deltaX2;
    private State gameState;

    public enum State {
        MAINGAME, MINIGAME
    }

    public Game(Context context,  ArrayList<String> playerNames, boolean[] playerSexes, float width, float height) {
        super(context);

        this.context = context;
        Map map = new Map(this);
        gameState = State.MAINGAME;
        playerHandler = new PlayerHandler(width,height);
        for (int i = 0; i < playerNames.size(); i++) {
            playerHandler.addPlayer(new Player(map.getTileFromTileMap(0, 3), playerNames.get(i), playerSexes[i]));
        }
        playerHandler.currentPlayerChanged();

        miniGameHandler = new MiniGameHandler(this, height, width);

        playerIconTouchRect = new  Rect((int) ((width / 8)*7), 0, (int) width, ((int) (height / 8)));

        zoomButtonRect = new Rect(0, 0, (int) (width / 8), ((int) (height / 8)));

        camera = new Camera(this,playerHandler,width,height);
        renderer = new Renderer(this,getHolder(),map,playerHandler,camera,miniGameHandler);



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
                tempPlayer.addToScore(134);
                if (score == 0) {
                    tempPlayer.setLocation(tempTile.getNextTile());
                } else {
                    tempPlayer.setLocation(tempTile.getNextHarderTile());
                }
                break;
            case 1:
                tempPlayer.addToScore(309);
                if (score == 0) {
                    tempPlayer.setLocation(tempTile.getNextEasierTile());
                } else if (score == 2) {
                    tempPlayer.setLocation(tempTile.getNextHarderTile());
                } else {
                    tempPlayer.setLocation(tempTile.getNextTile());
                }
                break;
            case 2:
                tempPlayer.addToScore(489);
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



    public void progress(MotionEvent motionEvent) {
        if (gameState == State.MAINGAME) {
            Rect touchPoint = new Rect((int) motionEvent.getX() - 1, (int) motionEvent.getY() - 1, (int) motionEvent.getX() + 1, (int) motionEvent.getY() + 1);
            if (Rect.intersects(touchPoint,zoomButtonRect)) {
                camera.prepareZoomEvent();
            }
            else if (Rect.intersects(touchPoint, playerIconTouchRect)) {
                if(playerHandler.isDetailedPlayerInfo()){
                    playerHandler.setDetailedPlayerInfo(false);
                }
                else{
                    playerHandler.setDetailedPlayerInfo(true);
                }

            }
            else if (camera.getCameraState() == Camera.CameraState.ZOOMEDOUT) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        deltaX1 = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        deltaX2 = motionEvent.getX();
                        float deltaX = deltaX2 - deltaX1;
                        if (Math.abs(deltaX) > 50) {
                            if (deltaX2 > deltaX1) {
                                if (camera.getTranslateX() != 0){
                                    camera.addToTranslateX(-400);
                                }
                            } else {
                                camera.addToTranslateX(400);
                            }
                        }
                        break;
                }
            } else if (camera.getCameraState() == Camera.CameraState.FOCUSED) {
                if (camera.getTouchTimer() == 0) {
                    if(!playerHandler.isDetailedPlayerInfo()) {

                            if (playerHandler.getCurrentPlayer().getLocation().isMiniGame) {
                                startMiniGame();
                            } else {
                                camera.prepareFocusChange();
                            }

                    }
                    else{
                        playerHandler.touched(touchPoint);
                    }
                    camera.setTouchTimer(30);
                }
            }
        }
        else if (gameState == State.MINIGAME) {
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

    public void setGameState(State gameState) {
        this.gameState = gameState;
    }

}
