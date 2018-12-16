package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.helpers.BitmapLoader;
import com.jaimehall.drinkingisfun.helpers.TextHandler;


public class Player {

	private Tile location;
	private String playerName;
	private String sex;
	private long score = 0;
	private Bitmap playerIcon;
	private Rect coordinates;
	private TextHandler textHandler;

	
	Player(Tile location, String playerName, String sex, String playerIconPath, BitmapLoader bitmapLoader, TextHandler textHandler){
		this.location=location;
		this.playerName=playerName;
		this.sex=sex;
		this.textHandler=textHandler;

        if(playerIconPath.matches("Color")) {
            Bitmap playerIconBackground = Bitmap.createScaledBitmap(bitmapLoader.getBitmap(R.drawable.spieler,300,300),300,300,false);

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

	public void renderAtSpecificCoordinates(Canvas canvas, Rect specificCoordinates){

			canvas.drawBitmap(playerIcon,null,specificCoordinates,null);
	}


    private Bitmap combineTwoBitmaps(Bitmap bmp1,Bitmap bmp2){
        Bitmap bitmapOverlay = Bitmap.createBitmap(bmp1.getWidth(),bmp1.getHeight(),bmp1.getConfig());
        Canvas canvas = new Canvas(bitmapOverlay);
        canvas.drawBitmap(bmp1,new Matrix(),null);
        canvas.drawBitmap(bmp2,0,0,null);
        return bitmapOverlay;
    }

    String getTask(){
	    return textHandler.getCompleteTask(this);
    }



	public Tile getLocation() {
		return location;
	}

	void setLocation(Tile location) {
		this.location = location;
	}


	void setCoordinates(Rect coordinates) {
		this.coordinates = coordinates;

	}

	public String getSex() {
		return sex;
	}

    void addToScore(double scoreAdd){
	    score+=scoreAdd;
    }

	public String getPlayerName() {
		return playerName;
	}

	long getScore() {
		return score;
	}

	Bitmap getImage() {
		return playerIcon;
	}
}
