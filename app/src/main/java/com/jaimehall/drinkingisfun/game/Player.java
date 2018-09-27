package com.jaimehall.drinkingisfun.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jaimehall.drinkingisfun.activities.GameActivity;
import com.jaimehall.drinkingisfun.helpers.TextRect;

import java.util.StringTokenizer;


public class Player {

	private Tile location;
	private Tile prevLocation;
	private String completeInformation;
	private String playerName;
	private boolean isFemale;
	private long score = 0;
	
	public Player(Tile location,String playerName, Boolean isFemale){
		this.location=location;
		this.playerName=playerName;
		this.isFemale=isFemale;
	}



	public void render(Canvas canvas){
		if(location.isMiniGame){
			completeInformation = playerName+", mach dich bereit ein Minispiel zu spielen!";
		}
		else{

			if(prevLocation!=location){
				NormalTile normalLocation = (NormalTile)location;
				String[] tokenedInformation = normalLocation.getRandomInformation();
				StringBuffer completeBufferedInformation = new StringBuffer();

				for(int i = 0;i<tokenedInformation.length;i++){
					if(tokenedInformation[i].matches("PlayerName")){
						completeBufferedInformation.append(playerName);

					}
					else if(tokenedInformation[i].matches("EinzahlGeschlecht")){
						if(isFemale){
							completeBufferedInformation.append("sie");
						}
						else{
							completeBufferedInformation.append("er");
						}
					}
					else if(tokenedInformation[i].matches("MehrzahlGeschlecht")){
						if(isFemale){
							completeBufferedInformation.append("ihrer");
						}
						else{
							completeBufferedInformation.append("seiner");
						}
					}
					else if(tokenedInformation[i].matches("AnzahlSchlucke")){
						int anzahlSchlucke = Math.round((int)(Math.random()*((NormalTile) location).getTileDifficulty()));
						while(anzahlSchlucke==0){
							anzahlSchlucke = Math.round((int)(Math.random()*((NormalTile) location).getTileDifficulty()));
						}
						completeBufferedInformation.append(anzahlSchlucke);
					}
					else{
						completeBufferedInformation.append(tokenedInformation[i]);
					}
				}
				completeInformation=completeBufferedInformation.toString();
				prevLocation=location;
			}
		}
		location.renderText(canvas,completeInformation);

	}


	public Tile getLocation() {
		return location;
	}

	public void setLocation(Tile location) {
		this.location = location;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	
}
