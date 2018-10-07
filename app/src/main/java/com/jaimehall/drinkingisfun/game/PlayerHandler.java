package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.helpers.BitmapLoader;

import java.util.LinkedList;




public class PlayerHandler {

	private LinkedList<Player> players = new LinkedList<Player>();
	private Player currentPlayer;
	private Player nextPlayer;
	private LinkedList<Player> playersOnCurrentTile = new LinkedList<>();
	private int indexOfDetailedPlayer=0;
	private Rect touchLeftRect,touchRightRect;
	private Rect menuBackgroundRect;
	private Rect playerMenuLeft,playerMenuRight,playerMenuName,playerMenuPic,playerMenuScore;
	private Paint textPaint;
	private Bitmap playerMenuBackground;

	private boolean playerChanged = true;
	
	public PlayerHandler(Game game, float width, float height, BitmapLoader bitmapLoader){ ;

	    touchRightRect = new Rect((int)(width/2),0,(int)width,(int)height);
	    touchLeftRect = new Rect(0,0,(int)(width/2),(int)height);
        menuBackgroundRect = new Rect(0,0,(int)width,(int)height);

        playerMenuLeft = new Rect((int)(width/10),(int)(height/7),(int)((width/25)*11),(int)((height/7)*6));
        playerMenuRight = new Rect((int)((width/25)*14),(int)(height/7),(int)((width/10)*9),(int)((height/7)*6));
        playerMenuName = new Rect(playerMenuLeft.left,playerMenuLeft.top,playerMenuLeft.left+playerMenuLeft.width(),playerMenuLeft.top+(playerMenuLeft.height()/4));
        playerMenuPic = new Rect(playerMenuLeft.left,playerMenuLeft.top+(playerMenuLeft.width()/4),playerMenuLeft.left+playerMenuLeft.width(),playerMenuLeft.top+playerMenuLeft.height());
        playerMenuScore = new Rect(playerMenuRight.left,playerMenuRight.top,playerMenuRight.left+playerMenuRight.width(),playerMenuRight.top+(playerMenuRight.height()/2));

        textPaint = new Paint();
        textPaint.setTextSize(120f);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);


        playerMenuBackground = bitmapLoader.getBitmap(R.drawable.spielermenu);
	}

    public void renderPlayerMenu(Canvas canvas){
        canvas.drawBitmap(playerMenuBackground,null,menuBackgroundRect,null);

        Player detailedPlayer = players.get(indexOfDetailedPlayer);

        canvas.drawText(detailedPlayer.getPlayerName(),playerMenuName.centerX(),playerMenuName.centerY(),textPaint);
        canvas.drawBitmap(detailedPlayer.getImage(),null,playerMenuPic,null);
        canvas.drawText("Score: "+detailedPlayer.getScore(),playerMenuScore.centerX(),playerMenuScore.centerY(),textPaint);

    }

	public void touched(Rect touchPoint){
	    if(Rect.intersects(touchPoint,touchRightRect)){
	        if(indexOfDetailedPlayer==0){
	            indexOfDetailedPlayer=players.size()-1;
            }
            else{
	            indexOfDetailedPlayer--;
            }
        }
        else if(Rect.intersects(touchPoint,touchLeftRect)){
            if(indexOfDetailedPlayer == players.size()-1){
                indexOfDetailedPlayer=0;
            }
            else{
                indexOfDetailedPlayer++;
            }
        }
	}
	
	public void nextPlayer() {
	    if(!currentPlayer.getLocation().isMiniGame){
	        currentPlayer.addToScore((currentPlayer.getLocation().getTileDifficulty()+1)*(20*Math.random()));
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
        playerChanged = true;
    }


	public void currentPlayerChanged(){
        Tile currentTile = currentPlayer.getLocation();

	    playersOnCurrentTile.clear();


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

	public void renderPlayerIcons(Canvas canvas) {
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

    public boolean isPlayerChanged() {
        return playerChanged;
    }

    public void setPlayerChanged(boolean playerChanged) {
        this.playerChanged = playerChanged;
    }


}
