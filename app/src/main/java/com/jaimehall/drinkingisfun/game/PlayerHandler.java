package com.jaimehall.drinkingisfun.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.LinkedList;




public class PlayerHandler {

	private LinkedList<Player> players = new LinkedList<Player>();
	private Player currentPlayer;
	private Player nextPlayer;
	private LinkedList<Player> playersOnCurrentTile = new LinkedList<>();
	private boolean detailedPlayerInfo =false;
	private int indexOfDetailedPlayer=0;
	private Rect detailRight,detailLeft;
	
	public PlayerHandler(float width,float height){
	    detailRight = new Rect((int)(width/2),0,(int)width,(int)height);
	    detailLeft = new Rect(0,0,(int)(width/2),(int)height);
	}

	public void touched(Rect touchPoint){
	    if(Rect.intersects(touchPoint,detailLeft)){
	        if(indexOfDetailedPlayer==0){
	            indexOfDetailedPlayer=playersOnCurrentTile.size()-1;
            }
            else{
	            indexOfDetailedPlayer--;
            }
        }
        else if(Rect.intersects(touchPoint,detailRight)){
            if(indexOfDetailedPlayer == playersOnCurrentTile.size()-1){
                indexOfDetailedPlayer=0;
            }
            else{
                indexOfDetailedPlayer++;
            }
        }
        else{
            detailedPlayerInfo = false;
        }
	}
	
	public void nextPlayer() {
	    if(!currentPlayer.getLocation().isMiniGame){
	        currentPlayer.addToScore((currentPlayer.getLocation().getTileDifficulty()+1)*11.1);
        }

        int indexOfNextPlayer = players.indexOf(nextPlayer);

        if (indexOfNextPlayer == players.size() - 1) {
            nextPlayer = players.get(0);

        } else {
            currentPlayer = nextPlayer;
            nextPlayer = players.get(indexOfNextPlayer + 1);
        }
        currentPlayer = players.get(indexOfNextPlayer);

        currentPlayerChanged();
        indexOfDetailedPlayer=0;
    }


	public void currentPlayerChanged(){
	    playersOnCurrentTile.clear();

		Tile currentTile = currentPlayer.getLocation();
		for(int i =0;i<players.size();i++){
			Player tempPlayer = players.get(i);
			if(currentTile == tempPlayer.getLocation()){
				playersOnCurrentTile.add(tempPlayer);
			}
		}

		float baseX = currentTile.getX()+((currentTile.getWidth()/32)*3);
		float baseY = currentTile.getY()+((currentTile.getHeight()/32)*23);

		for(int i=0;i<playersOnCurrentTile.size();i++){
			float x =baseX+(i*((float)((currentTile.getWidth()/16)*1.6875)+currentTile.getWidth()/128));
			Rect rect = new Rect((int)x,(int)baseY,(int)(x+((currentTile.getWidth()/16)*1.6875)),(int)(baseY+((currentTile.getHeight()/16)*3)));
			playersOnCurrentTile.get(i).setCoordinates(rect);
		}

	}

	public void render(Canvas canvas) {
		for(int i =0;i<playersOnCurrentTile.size();i++){
			playersOnCurrentTile.get(i).render(canvas);
		}
		if(detailedPlayerInfo){
		    playersOnCurrentTile.get(indexOfDetailedPlayer).renderDetails(canvas);

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

    public boolean isDetailedPlayerInfo() {
        return detailedPlayerInfo;
    }

    public void setDetailedPlayerInfo(boolean detailedPlayerInfo) {
        this.detailedPlayerInfo = detailedPlayerInfo;
    }


}
