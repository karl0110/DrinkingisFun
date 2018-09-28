package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.jaimehall.drinkingisfun.helpers.TextRect;


public abstract class Tile {

    protected float width,height;
    protected float x,y;
    protected Bitmap image;
    protected Map map;
    protected boolean taskFinished = false;
    protected boolean isMiniGame;
    protected int tileDifficulty;
    protected TextRect textRect;
    protected int textRectPrep;
    protected float baseX,baseY;

    public Tile(float x,float y,float width,float height,Bitmap border,Bitmap background,Map map,int tileDifficulty){
        this.width=width;
        this.height=height;
        this.x=x;
        this.y=y;
        this.image=combineTwoBitmaps(background,border);
        this.map=map;
        this.tileDifficulty=tileDifficulty;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setFakeBoldText(true);
        paint.setTextSize(25);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        textRect = new TextRect(paint);
    }

    public void render(Canvas canvas){
        canvas.drawBitmap(image,null,getCoordinates(),null);
    }

    public void renderText(Canvas canvas,Player currentFocusedPlayer){
        if(currentFocusedPlayer.getCompleteInformation() != null) {
            if (textRectPrep != textRect.prepare(currentFocusedPlayer.getCompleteInformation(), (int) (width - ((width / 32) * 6)), (int) (height - ((height / 32) * 12)))) {
                baseX = x + (width / 2);
                baseY = y + ((height / 32) * 3);
                textRectPrep = textRect.prepare(currentFocusedPlayer.getCompleteInformation(), (int) (width - ((width / 32) * 6)), (int) (height - ((height / 32) * 12)));
            }

            textRect.draw(canvas, (int) baseX, (int) baseY);
        }
    }

    private Bitmap combineTwoBitmaps(Bitmap bmp1,Bitmap bmp2){
        Bitmap bitmapOverlay = Bitmap.createBitmap(bmp1.getWidth(),bmp1.getHeight(),bmp1.getConfig());
        Canvas canvas = new Canvas(bitmapOverlay);
        canvas.drawBitmap(bmp1,new Matrix(),null);
        canvas.drawBitmap(bmp2,0,0,null);
        return bitmapOverlay;
    }

    public abstract Tile getNextTile();

    public abstract void findNextTile();

    public abstract int getTileDifficulty() ;

    public Rect getCoordinates(){
        return new Rect((int)x,(int)y,(int)(x+width),(int)(y+height));
    }


    public boolean isTaskFinished() {
        return taskFinished;
    }

    public void setTaskFinished(boolean taskFinished) {
        this.taskFinished = taskFinished;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isMiniGame() {
        return isMiniGame;
    }

    public void setMiniGame(boolean miniGame) {
        isMiniGame = miniGame;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
