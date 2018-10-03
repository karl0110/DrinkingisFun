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
    private Paint iconPaint;
	private Rect detailCoordinates;
	private Paint detailedPaint;
	private Paint backgroundPaint;

	
	public Player(Tile location,String playerName, Boolean isFemale){
		this.location=location;
		this.playerName=playerName;
		this.isFemale=isFemale;
		this.image=image;

		detailCoordinates=new Rect();

		int r = (int)(Math.random()*255);
        int g = (int)(Math.random()*255);
        int b = (int)(Math.random()*255);

        iconPaint = new Paint();
        iconPaint.setStyle(Paint.Style.FILL);
        iconPaint.setARGB(255,r,g,b);

        detailedPaint = new Paint();
        detailedPaint.setColor(Color.BLACK);
        detailedPaint.setFakeBoldText(true);
        detailedPaint.setTextAlign(Paint.Align.LEFT);
        detailedPaint.setTextSize(40);

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setAlpha(150);
	}


	public void renderDetails(Canvas canvas){
	    canvas.drawRect(detailCoordinates,backgroundPaint);
        canvas.drawRect(detailCoordinates.left,detailCoordinates.top,detailCoordinates.left+detailCoordinates.width()/2,detailCoordinates.bottom,iconPaint);
        canvas.drawText(playerName,detailCoordinates.left+detailCoordinates.width()/2,detailCoordinates.top+detailCoordinates.height()/8,detailedPaint);
        canvas.drawText("Score:",detailCoordinates.left+detailCoordinates.width()/2,detailCoordinates.top+detailCoordinates.height()/2,detailedPaint);
        canvas.drawText(""+Math.round(score),detailCoordinates.left+detailCoordinates.width()/2,detailCoordinates.top+((detailCoordinates.height()/4)*3),detailedPaint);
    }

	public void render(Canvas canvas){
		canvas.drawRect(coordinates,iconPaint);

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

	public String getCompleteInformation() {
		return completeInformation;
	}

	public void setCoordinates(Rect coordinates) {
		this.coordinates = coordinates;
		detailCoordinates.set(new Rect((int)(location.getX()+((location.getWidth()/32)*3)),(int)(location.getY()+((location.getHeight()/32)*3)),(int)(location.getX()+((location.getWidth()/32)*29)),(int)(location.getY()+(location.getHeight()/32)*29)));
	}

    public Rect getCoordinates() {
        return coordinates;
    }

    public void addToScore(double scoreAdd){
	    score+=scoreAdd;
    }

}
