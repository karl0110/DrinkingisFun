package com.jaimehall.drinkingisfun.minigames;


import android.graphics.Canvas;
import android.view.MotionEvent;

import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.minigames.exBeer.ExBeerMiniGame;
import com.jaimehall.drinkingisfun.minigames.pong.PongMiniGame;


import java.util.ArrayList;

public class MiniGameHandler {


    private MiniGame miniGame;
    private ArrayList<MiniGame> miniGames;
    private Game game;
    private float width,height;


    public MiniGameHandler(Game game,float width,float height) {
        this.game=game;
        miniGames= new ArrayList<>();
        miniGames.add(new ExBeerMiniGame(game,width,height));
        miniGames.add(new PongMiniGame(game,width,height));

    }

    public void selectNewMiniGame(){
        miniGame=miniGames.get(1);
        miniGame.reset();
    }

    public void render(Canvas canvas){
        miniGame.render(canvas);
    }

    public void tick(){
        miniGame.tick();
    }

    public void touched(MotionEvent motionEvent){
        miniGame.touched(motionEvent);
    }

}
