package com.jaimehall.drinkingisfun.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jaimehall.drinkingisfun.helpers.TextRect;

import java.util.StringTokenizer;


public class Player {

	private Tile location;
	private Tile prevLocation;
	private String completeInformation;
	private String playerName;
	private boolean isFemale;
	
	public Player(Tile location,String playerName, Boolean isFemale){
		this.location=location;
		this.playerName=playerName;
		this.isFemale=isFemale;
	}
	
	public void render(Canvas canvas,Game game){
		if(location.isMiniGame){
            game.startMiniGame();
		}
		else{
			float baseX = location.getX()+56;
			float baseY = location.getY()+27;

			if(prevLocation!=location){
				NormalTile normalLocation = (NormalTile)location;
				String information = normalLocation.getRandomInformation();
				StringBuffer completeBufferedInformation = new StringBuffer();
				StringTokenizer tokens = new StringTokenizer(information,":");
				String[] tokenedString = new String[tokens.countTokens()];
				int r = 0;
				while(tokens.hasMoreTokens()){
					tokenedString[r]=tokens.nextToken();
					r++;
				}
				for(int i = 0;i<tokenedString.length;i++){
					if(tokenedString[i].matches("PlayerName")){
						completeBufferedInformation.append(playerName);

					}
					else if(tokenedString[i].matches("EinzahlGeschlecht")){
						if(isFemale){
							completeBufferedInformation.append("sie");
						}
						else{
							completeBufferedInformation.append("er");
						}
					}
					else if(tokenedString[i].matches("MehrzahlGeschlecht")){
						if(isFemale){
							completeBufferedInformation.append("ihrer");
						}
						else{
							completeBufferedInformation.append("seiner");
						}
					}
					else if(tokenedString[i].matches("AnzahlSchlucke")){
						int anzahlSchlucke = Math.round((int)(Math.random()*((NormalTile) location).getDifficulty()));
						while(anzahlSchlucke==0){
							anzahlSchlucke = Math.round((int)(Math.random()*((NormalTile) location).getDifficulty()));
						}
						completeBufferedInformation.append(anzahlSchlucke);
					}
					else{
						completeBufferedInformation.append(tokenedString[i]);
					}
				}
				completeInformation=completeBufferedInformation.toString();
				prevLocation=location;
			}

			TextRect textRect;

			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.BLACK);
			paint.getFontMetrics();
			paint.setTextSize(30);
			paint.setAntiAlias(true);

			textRect = new TextRect(paint);

			int h = textRect.prepare(completeInformation,(int)location.coordinates.width()-112,(int)location.coordinates.height()-62);

			textRect.draw(canvas,(int)baseX,(int)baseY);
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
	
	
}
