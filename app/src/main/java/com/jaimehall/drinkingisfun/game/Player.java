package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

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
	private Bitmap image;
	private Rect coordinates;
	private Paint tempPaint;
	
	public Player(Tile location,String playerName, Boolean isFemale){
		this.location=location;
		this.playerName=playerName;
		this.isFemale=isFemale;
		this.image=image;

		tempPaint = new Paint();
		tempPaint.setStyle(Paint.Style.FILL);
		tempPaint.setColor(Color.BLACK);
	}



	public void render(Canvas canvas){
		canvas.drawRect(coordinates,tempPaint);

	}

	public void tick(){
		if(!location.isMiniGame){
			if(prevLocation!=location){
				NormalTile normalLocation = (NormalTile)location;
				String[] tokenedInformation = normalLocation.getRandomInformation();
				StringBuilder completeBufferedInformation = new StringBuilder();

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
						if(anzahlSchlucke != 1){
                            completeBufferedInformation.append(anzahlSchlucke+ " Schlucke");
                        }
                        else{
                            completeBufferedInformation.append(anzahlSchlucke+ " Schluck");
                        }

					}
					else{
						completeBufferedInformation.append(tokenedInformation[i]);
					}
				}
				completeInformation=completeBufferedInformation.toString();
				prevLocation=location;
			}
		}
		else{

			completeInformation = playerName+", mach dich bereit ein Minispiel zu spielen!";

		}
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

	public String getCompleteInformation() {
		return completeInformation;
	}

	public void setCoordinates(Rect coordinates) {
	    int amount = (coordinates.width()/16)*7;
	    Rect rect = new Rect(coordinates.centerX()-amount,coordinates.centerY()-amount,coordinates.centerX()+amount,coordinates.centerY()+amount);
		this.coordinates = rect;
	}
}
