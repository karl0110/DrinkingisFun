package com.jaimehall.drinkingisfun.minigames;


import android.graphics.Canvas;
import android.view.MotionEvent;

import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.minigames.exBeer.ExBeerMiniGame;
import com.jaimehall.drinkingisfun.minigames.pong.PongMiniGame;


import java.util.ArrayList;

public class MiniGameHandler implements Runnable {

    private Thread thread;
    private boolean running;

    private MiniGame miniGame;
    private ArrayList<MiniGame> miniGames;
    private Game game;
    private float width,height;


    public MiniGameHandler(Game game,float width,float height) {
        this.game=game;
        miniGames= new ArrayList<>();
        miniGames.add(new PongMiniGame(game,width,height));
        miniGames.add(new ExBeerMiniGame(game,width,height));


    }

    public void run(){
        long lastTime = System.nanoTime();//Wird für einen Timer benötigt.
        final double amountOfTicks = 60.0;//Wie oft die Methode tick() in einer Sekunde aufgerufen werden soll.
        double ns = 1000000000 / amountOfTicks;//Berechnet wie viel Zeit vergeht bis die Methode tick() aufgerufen wird.
        double delta = 0;//Variable welche Berechnet, wann die tick() Methode aufgerufen werden soll.
        int updates = 0;//Wie oft das Progam die tick() Methode in einer Sekunde aufrufen hat.
        long timer = System.currentTimeMillis();//Eine Variable um die Zeit zu Zählen. Ist für die Berechnung der "TicksPerSecond" und "FramesPerSecond" notwendig.

        while (running) {
            long now = System.nanoTime();//Timer Variable für die aktuelle Zeit.
            delta += (now - lastTime) / ns;//Berechnet mithilfe der Timer und ns Variablen, wann die tick() Methode aufgerufen werden soll.
            lastTime = now;//Stellt den Timer wieder zurück.
            if (delta >= 1) {//Guckt ob die Methode tick() jetzt aufgerufen werden soll.

                if(game.getGameState() == Game.State.MINIGAME) {
                    tick();//ruft die Methode tick() auf.
                }

                updates++;//Addiert zum Update-Zähler 1 dazu.
                delta--;//Setzt den tick() Aufruf-Timer zurück.

            }

            if (System.currentTimeMillis() - timer > 1000) {//Wenn eine Sekunde vergangen ist.
                timer += 1000;//addiert zum Timer eine Sekunde dazu.
                System.out.println("MiniGame Ticks: " + updates);//Druckt die "TicksPerSecond" und "FramesPerSecond" aus.
                updates = 0;//setzt den Tick-Zähler zurück.
            }
        }
    }

    public void selectNewMiniGame(){
        int index = (int)Math.round(Math.random()*(1));
        miniGame=miniGames.get(index);
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
