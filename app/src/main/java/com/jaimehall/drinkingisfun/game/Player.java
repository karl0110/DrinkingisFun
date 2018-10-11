package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.activities.GameActivity;
import com.jaimehall.drinkingisfun.helpers.BitmapLoader;
import com.jaimehall.drinkingisfun.helpers.TextRect;

import java.util.StringTokenizer;


public class Player {

	private Tile location;
	private Tile prevLocation;
	private String completeInformation;
	private String playerName;
	private boolean isFemale;
	private long score = 0;
	private Bitmap playerIcon;
	private Rect coordinates;

	
	public Player(Tile location,String playerName, Boolean isFemale,String playerIconPath,BitmapLoader bitmapLoader){
		this.location=location;
		this.playerName=playerName;
		this.isFemale=isFemale;

        if(playerIconPath.matches("Color")) {
            Bitmap playerIconBackground = Bitmap.createScaledBitmap(bitmapLoader.getBitmap(R.drawable.spieler,300,300),200,200,false);

            int r = (int) (Math.random() * 255);
            int g = (int) (Math.random() * 255);
            int b = (int) (Math.random() * 255);

            int playerColor = Color.argb(255, r, g, b);

            Bitmap playerBackground = Bitmap.createBitmap(playerIconBackground.getWidth(), playerIconBackground.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(playerBackground);
            Paint paint = new Paint();
            paint.setColor(playerColor);
            canvas.drawRect(0, 0, playerIconBackground.getWidth(), playerIconBackground.getHeight(), paint);

            playerIcon = combineTwoBitmaps(playerBackground, playerIconBackground);
        }
        else{
            playerIcon = bitmapLoader.getBitmapFromPath(playerIconPath);
        }

	}


	public void render(Canvas canvas){
		canvas.drawBitmap(playerIcon,null,coordinates,null);

	}

	public void tick(){

		if(!location.isMiniGame() && !location.isGoal()){
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
		else if(location.isMiniGame()){

			completeInformation = playerName+", mach dich bereit ein Minispiel zu spielen!";

		}
		else {
			completeInformation = "Ihr seid am Ziel angekommen!";
		}
	}

    private Bitmap combineTwoBitmaps(Bitmap bmp1,Bitmap bmp2){
        Bitmap bitmapOverlay = Bitmap.createBitmap(bmp1.getWidth(),bmp1.getHeight(),bmp1.getConfig());
        Canvas canvas = new Canvas(bitmapOverlay);
        canvas.drawBitmap(bmp1,new Matrix(),null);
        canvas.drawBitmap(bmp2,0,0,null);
        return bitmapOverlay;
    }



	public Tile getLocation() {
		return location;
	}

	public void setLocation(Tile location) {
		this.location = location;
	}

	public String getCompleteInformation() {
		return completeInformation;
	}

	public void setCoordinates(Rect coordinates) {
		this.coordinates = coordinates;

	}

    public Rect getCoordinates() {
        return coordinates;
    }

    public void addToScore(double scoreAdd){
	    score+=scoreAdd;
    }

	public String getPlayerName() {
		return playerName;
	}

	public long getScore() {
		return score;
	}

	public Bitmap getImage() {
		return playerIcon;
	}
}
