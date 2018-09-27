package com.jaimehall.drinkingisfun.game;

import android.graphics.Canvas;

import java.util.LinkedList;




public class PlayerHandler {

	private LinkedList<Player> players = new LinkedList<Player>();
	private Player currentPlayer;
	private Player nextPlayer;
	
	public PlayerHandler(){
				
	}
	
	public void nextPlayer(){
		int indexOfNextPlayer = players.indexOf(nextPlayer);
		
		if(indexOfNextPlayer==players.size()-1){
			nextPlayer=players.get(0);
			
		}
		else{
			currentPlayer=nextPlayer;
			nextPlayer=players.get(indexOfNextPlayer+1);
		}
		currentPlayer=players.get(indexOfNextPlayer);
	}
	

	public void render(Canvas canvas) {
		currentPlayer.render(canvas);
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
