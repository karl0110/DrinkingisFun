package com.jaimehall.drinkingisfun.minigames.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.minigames.MiniGame;

public class PongMiniGame extends MiniGame {

    private EnemyPaddle enemyPaddle;
    private PlayerPaddle playerPaddle;


    public PongMiniGame(Game game) {
        super(game);
        enemyPaddle=new EnemyPaddle(0,0,10,10);
        playerPaddle=new PlayerPaddle(0,1800,10,10);
    }

    public boolean isPortrait(){
        return true;
    }

    @Override
    public void render(Canvas canvas) {

        canvas.drawColor(Color.WHITE);

        enemyPaddle.render(canvas);
        playerPaddle.render(canvas);
    }

    @Override
    public void tick() {
        enemyPaddle.tick();
        playerPaddle.tick();
    }

    @Override
    public void touched(MotionEvent motionEvent) {
        playerPaddle.touched(motionEvent);
    }

    @Override
    public void reset() {

    }
}
