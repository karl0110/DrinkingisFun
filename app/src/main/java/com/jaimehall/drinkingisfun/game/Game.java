package com.jaimehall.drinkingisfun.game;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jaimehall.drinkingisfun.activities.GameActivity;
import com.jaimehall.drinkingisfun.helpers.BitmapLoader;
import com.jaimehall.drinkingisfun.helpers.TextHandler;
import com.jaimehall.drinkingisfun.minigames.MiniGameHandler;

import java.util.LinkedList;

public class Game extends SurfaceView implements SurfaceHolder.Callback{

    private float width,height;

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

    private static final int INVALID_POINTER_ID = 259;
    private int activePointerId = INVALID_POINTER_ID;
    private float lastTouchX = 0;
    private float lastTouchY = 0;

    public enum State {
        MAINGAME, MINIGAME, PLAYERMENU
    }



    public Game(Context context, String[] playerNames,String[] playerIcons, String[] playerSexes, float width, float height, LoadingScreen loadingScreen) {
        super(context);
        this.width=width;
        this.height=height;
        this.context = context;
        this.playerNames = playerNames;
        this.playerIcons = playerIcons;
        this.playerSexes=playerSexes;

        this.loadingScreen = loadingScreen;
        renderer = new Renderer(this,width,height);


        getHolder().addCallback(this);

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
            loadingScreen.progressOne();

                    BitmapLoader bitmapLoader = new BitmapLoader(getResources());

            loadingScreen.progressOne();


                    gameState = State.MAINGAME;

                    playerHandler = new PlayerHandler( width, height, bitmapLoader,this);
            loadingScreen.progressOne();
            loadingScreen.progressOne();

                    BackgroundHandler backgroundHandler = new BackgroundHandler(bitmapLoader,250,140);
            loadingScreen.progressOne();
            loadingScreen.progressOne();
            loadingScreen.progressOne();
            loadingScreen.progressOne();

                    Map map = new Map(bitmapLoader);
            loadingScreen.progressOne();
            loadingScreen.progressOne();
            loadingScreen.progressOne();

                    TextHandler textHandler = new TextHandler(this);
            loadingScreen.progressOne();

                    for (int i = 0; i < playerNames.length; i++) {
                         playerHandler.addPlayer(new Player(map.getTileFromTileMap(2, 4), playerNames[i], playerSexes[i], playerIcons[i], bitmapLoader, textHandler));
                    }

            loadingScreen.progressOne();

                    miniGameHandler = new MiniGameHandler(this, height, width, bitmapLoader);
            loadingScreen.progressOne();
            loadingScreen.progressOne();
            loadingScreen.progressOne();

                    playerIconTouchRect = new Rect((int) (width-(width / 8) ), 0, (int) width, ((int) (height / 8)));

                    zoomButtonRect = new Rect(0, 0, (int) (width / 8), ((int) (height / 8)));
            loadingScreen.progressOne();


                    camera = new Camera(this, playerHandler, width, height);
            loadingScreen.progressOne();
            loadingScreen.progressOne();

                    renderer.init( bitmapLoader,  playerHandler, camera, miniGameHandler, backgroundHandler,map);

            loadingScreen.progressOne();

                    cameraRenderer = new CameraRenderer(renderer,camera,width,height,this);

                    playerHandler.currentPlayerChanged();
            loadingScreen.progressOne();

                    camera.init();

            loadingScreen.progressOne();
            loadingScreen.progressOne();

                    ((GameActivity)context).initComplete();

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
                if(tempPlayer.getPlayerName().matches("Jaime")){
                    tempPlayer.addToScore((Math.random()*100)+500);
                }
                else {
                    tempPlayer.addToScore(Math.random() * 100);
                }
                if (score == 0) {
                    tempPlayer.setLocation(tempTile.getNextTile());
                } else {
                    tempPlayer.setLocation(tempTile.getNextHarderTile());
                }
                break;
            case 1:
                if(tempPlayer.getPlayerName().matches("Jaime")){
                    tempPlayer.addToScore((Math.random()*200)+500);
                }
                else {
                    tempPlayer.addToScore(Math.random() * 200);
                }
                if (score == 0) {
                    tempPlayer.setLocation(tempTile.getNextEasierTile());
                } else if (score == 2) {
                    tempPlayer.setLocation(tempTile.getNextHarderTile());
                } else {
                    tempPlayer.setLocation(tempTile.getNextTile());
                }
                break;
            case 2:
                if(tempPlayer.getPlayerName().matches("Jaime")){
                    tempPlayer.addToScore((Math.random()*300)+700);
                }
                else {
                    tempPlayer.addToScore(Math.random() * 300+200);
                }
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

    public void scale(float scaleFactor){
        camera.setScale(scaleFactor);
    }

    public void touch(MotionEvent e){

        if (gameState == State.MINIGAME) {
            miniGameHandler.touched(e);
            camera.setTouchTimer(60);
        }
        else{

            if (camera.getTouchTimer() == 0) {

                Rect touchPoint = new Rect((int) e.getX() - 1, (int) e.getY() - 1, (int) e.getX() + 1, (int) e.getY() + 1);
                if (gameState == State.MAINGAME) {
                    if (Rect.intersects(touchPoint, zoomButtonRect)) {
                        camera.prepareZoomEvent();
                    } else if (Rect.intersects(touchPoint, playerIconTouchRect)) {
                        gameState = State.PLAYERMENU;

                    } else if (camera.getCameraState() == Camera.CameraState.FOCUSED) {

                        if (playerHandler.getCurrentPlayer().getLocation().isMiniGame()) {
                            startMiniGame();
                        } else if (playerHandler.getCurrentPlayer().getLocation().isGoal()) {

                            finishGame();
                        } else {
                            camera.prepareFocusChange();
                        }
                    }
                } else if (gameState == State.PLAYERMENU) {
                    if (Rect.intersects(touchPoint, playerIconTouchRect)) {
                        gameState = State.MAINGAME;
                    } else {
                        playerHandler.touched(touchPoint);
                    }
                }


                camera.setTouchTimer(60);
            }

        }
        if(camera.getCameraState() == Camera.CameraState.ZOOMEDOUT) {

            final int action = e.getActionMasked();

            switch (action) {
                case MotionEvent.ACTION_DOWN: {

                    final int pointerIndex = e.getActionIndex();
                    lastTouchX = e.getX(pointerIndex);
                    lastTouchY = e.getY(pointerIndex);

                    activePointerId = e.getPointerId(pointerIndex);
                    break;
                }
                case MotionEvent.ACTION_MOVE: {

                    final int pointerIndex = e.findPointerIndex(activePointerId);

                    final float x = e.getX(pointerIndex);
                    final float y = e.getY(pointerIndex);

                    camera.addToTranslateX(lastTouchX-x);
                    camera.addToTranslateY(lastTouchY-y);

                    lastTouchX = x;
                    lastTouchY = y;

                    break;
                }

                case MotionEvent.ACTION_UP: {
                    activePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_CANCEL: {
                    activePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_POINTER_UP: {

                    final int pointerIndex = e.getActionIndex();
                    final int pointerId = e.getPointerId(pointerIndex);

                    if (pointerId == activePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        activePointerId = e.getPointerId(newPointerIndex);
                    }
                    break;
                }

            }

        }


    }

    public void nextPlayer(){

        playerHandler.nextPlayer();

        renderer.updateCurrentTask();

        cameraRenderer.updateCanvasBitmap();

    }



    public void pause() {

        camera.pause();
        renderer.pause();
        miniGameHandler.pause();
        cameraRenderer.pause();
    }


    public void resume() {
            camera.resume();
            renderer.resume();
            miniGameHandler.resume();
            cameraRenderer.resume();

    }

    public State getGameState() {
        return gameState;
    }


}
