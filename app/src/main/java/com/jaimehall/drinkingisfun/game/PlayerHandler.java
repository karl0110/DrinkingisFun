package com.jaimehall.drinkingisfun.game;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.LinkedList;




public class PlayerHandler {

	private LinkedList<Player> players = new LinkedList<Player>();
	private Player currentPlayer;
	private Player nextPlayer;
	private LinkedList<Player> playersOnCurrentTile = new LinkedList<>();
	
	public PlayerHandler(){
	}
	
	public void nextPlayer() {
        int indexOfNextPlayer = players.indexOf(nextPlayer);

        if (indexOfNextPlayer == players.size() - 1) {
            nextPlayer = players.get(0);

        } else {
            currentPlayer = nextPlayer;
            nextPlayer = players.get(indexOfNextPlayer + 1);
        }
        currentPlayer = players.get(indexOfNextPlayer);

        currentPlayerChanged();
    }


	public void currentPlayerChanged(){
	    playersOnCurrentTile.clear();

		Tile currentTile = currentPlayer.getLocation();
		float baseX = currentTile.getX()+((currentTile.getWidth()/32)*3);
		float baseY = currentTile.getY()+((currentTile.getHeight()/16)*10);
		for(int i =0;i<players.size();i++){
			Player tempPlayer = players.get(i);
			if(currentTile == tempPlayer.getLocation()){
				playersOnCurrentTile.add(tempPlayer);
			}
		}
		for(int i=0;i<playersOnCurrentTile.size();i++){
			float x =baseX+(i*((currentTile.getWidth()/16)*3));
			Rect rect = new Rect((int)x,(int)baseY,(int)(x+((currentTile.getWidth()/16)*3)),(int)(baseY+((currentTile.getWidth()/16)*3)));
			playersOnCurrentTile.get(i).setCoordinates(rect);
		}

	}

	public void render(Canvas canvas) {
		for(int i =0;i<playersOnCurrentTile.size();i++){
			playersOnCurrentTile.get(i).render(canvas);
		}
	}

	public void tick() {
		currentPlayer.tick();
	}


	public void addPlayer(Player player) {
		if(players.size()==0){
            this.players.add(player);
            currentPlayer=player;
        }
        else if(players.size()==1){
            this.players.add(player);
            nextPlayer=player;
        }
        else{
            this.players.add(player);
        }

	}


	public void removePlayer(Player player) {
		this.players.remove(player);
		
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Player getNextPlayer() {
		return nextPlayer;
	}

	public void setNextPlayer(Player nextPlayer) {
		this.nextPlayer = nextPlayer;
	}
	
	
}
