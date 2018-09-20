package com.jaimehall.drinkingisfun.minigames;


import android.graphics.Canvas;
import android.view.MotionEvent;

import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.minigames.exBeer.ExBeer;


import java.util.ArrayList;

public class MiniGameHandler {


    private MiniGame miniGame;
    private ArrayList<MiniGame> miniGames;
    private Game game;


    public MiniGameHandler(Game game) {
        this.game=game;
        miniGames= new ArrayList<>();
        miniGames.add(new ExBeer(game));

    }

    public void selectNewMiniGame(){
        miniGame=miniGames.get(0);
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
