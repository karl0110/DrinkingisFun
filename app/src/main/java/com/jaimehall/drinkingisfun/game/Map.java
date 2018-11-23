package com.jaimehall.drinkingisfun.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.helpers.BitmapLoader;


public class Map {

    private Tile[][] tileMap;
    private float tileWidth,tileHeight;

    Map(BitmapLoader bitmapLoader){
        tileMap= new Tile[26][9];
        tileWidth = 250;
        tileHeight = 140;

        createTileMap(bitmapLoader);
    }

    private void createTileMap(BitmapLoader bitmapLoader){

        tileMap[2][4] = new MiniGameTile(2*tileWidth, 4*tileHeight, tileWidth, tileHeight,this, new int[][]{{3,3},{3,4},{3,5}},1);


        tileMap[3][3] = new NormalTile(3 * tileWidth, 3*tileHeight, tileWidth, tileHeight,this, 4,2,0,false);
        tileMap[4][2] = new NormalTile(4*tileWidth, 2*tileHeight, tileWidth, tileHeight,this, 5,1,0,true);
        tileMap[5][1] = new NormalTile(5*tileWidth, 1*tileHeight, tileWidth, tileHeight,this, 6,1,0,false);
        tileMap[6][1] = new NormalTile(6*tileWidth, 1*tileHeight, tileWidth, tileHeight,this, 7,1,0,false);
        tileMap[7][1] = new NormalTile(7*tileWidth, 1*tileHeight, tileWidth, tileHeight,this, 8,1,0,false);
        tileMap[8][1] = new MiniGameTile(8*tileWidth, 1*tileHeight, tileWidth, tileHeight,this, new int[][]{{0,0},{9,1},{9,2}},0);
        tileMap[9][1] = new NormalTile(9*tileWidth, 1*tileHeight, tileWidth, tileHeight, this, 10,1,0,false);
        tileMap[9][3] = new NormalTile(9*tileWidth, 3*tileHeight, tileWidth, tileHeight, this, 10,2,0,false);
        tileMap[10][1] = new NormalTile(10*tileWidth, 1*tileHeight, tileWidth, tileHeight, this, 11,1,0,false);
        tileMap[10][2] = new NormalTile(10*tileWidth, 2*tileHeight, tileWidth, tileHeight, this, 11,1,0,true);
        tileMap[11][1] = new NormalTile(11*tileWidth, 1*tileHeight, tileWidth, tileHeight, this, 12,1,0,false);
        tileMap[12][1] = new Goal(12*tileWidth, 1*tileHeight, tileWidth, tileHeight,this, 0);


        tileMap[3][4] = new NormalTile(3 * tileWidth, 4*tileHeight, tileWidth, tileHeight, this, 4,4,1,false);
        tileMap[4][4] = new NormalTile(4*tileWidth, 4*tileHeight, tileWidth, tileHeight, this, 5,4,1,false);
        tileMap[5][4] = new NormalTile(5*tileWidth, 4*tileHeight, tileWidth, tileHeight, this, 6,4,1,false);
        tileMap[6][4] = new NormalTile(6*tileWidth, 4*tileHeight, tileWidth, tileHeight, this, 7,4,1,true);
        tileMap[7][4] = new NormalTile(7*tileWidth, 4*tileHeight, tileWidth, tileHeight, this, 8,4,1,false);
        tileMap[8][4] = new MiniGameTile(8*tileWidth, 4*tileHeight, tileWidth, tileHeight, this, new int[][]{{9,3},{9,4},{9,5}},1);
        tileMap[9][2] = new NormalTile(9*tileWidth, 2*tileHeight, tileWidth, tileHeight, this, 10,3,1,false);
        tileMap[9][4] = new NormalTile(9*tileWidth, 4*tileHeight, tileWidth, tileHeight, this, 10,4,1,false);
        tileMap[9][6] = new NormalTile(9*tileWidth, 6*tileHeight, tileWidth, tileHeight, this, 10,5,1,false);
        tileMap[10][3] = new NormalTile(10*tileWidth, 3*tileHeight, tileWidth, tileHeight, this, 11,4,1,false);
        tileMap[10][4] = new NormalTile(10*tileWidth, 4*tileHeight, tileWidth, tileHeight, this, 11,4,1,false);
        tileMap[10][5] = new NormalTile(10*tileWidth, 5*tileHeight, tileWidth, tileHeight, this, 11,4,1,false);
        tileMap[11][4] = new NormalTile(11*tileWidth, 4*tileHeight, tileWidth, tileHeight, this, 12,4,1,false);
        tileMap[12][4] = new Goal(12*tileWidth, 4*tileHeight, tileWidth, tileHeight,this, 1);


        tileMap[3][5] = new NormalTile(3 * tileWidth, 5*tileHeight, tileWidth, tileHeight, this, 4,6,2,false);
        tileMap[4][6] = new NormalTile(4*tileWidth, 6*tileHeight, tileWidth, tileHeight, this, 5,7,2,false);
        tileMap[5][7] = new NormalTile(5*tileWidth, 7*tileHeight, tileWidth, tileHeight, this, 6,7,2,false);
        tileMap[6][7] = new NormalTile(6*tileWidth, 7*tileHeight, tileWidth, tileHeight, this, 7,7,2,false);
        tileMap[7][7] = new NormalTile(7*tileWidth, 7*tileHeight, tileWidth, tileHeight, this, 8,7,2,true);
        tileMap[8][7] = new MiniGameTile(8*tileWidth, 7*tileHeight, tileWidth, tileHeight, this, new int[][]{{9,6},{9,7},{0,0}},2);
        tileMap[9][5] = new NormalTile(9*tileWidth, 5*tileHeight, tileWidth, tileHeight, this, 10,6,2,false);
        tileMap[9][7] = new NormalTile(9*tileWidth, 7*tileHeight, tileWidth, tileHeight, this, 10,7,2,false);
        tileMap[10][6] = new NormalTile(10*tileWidth, 6*tileHeight, tileWidth, tileHeight, this, 11,7,2,false);
        tileMap[10][7] = new NormalTile(10*tileWidth, 7*tileHeight, tileWidth, tileHeight, this, 11,7,2,false);
        tileMap[11][7] = new NormalTile(11*tileWidth, 7*tileHeight, tileWidth, tileHeight, this, 12,7,2,false);
        tileMap[12][7] = new Goal(12*tileWidth, 7*tileHeight, tileWidth, tileHeight,this, 2);

        Bitmap arrowRight = bitmapLoader.getBitmap(R.drawable.pfeilrechts,50,50);
        Bitmap arrowRightUp = bitmapLoader.getBitmap(R.drawable.pfeilrechtsoben,50,50);
        Bitmap arrowRightDown = bitmapLoader.getBitmap(R.drawable.pfeilrechtsunten,50,50);

        for(int xxx=0; xxx<tileMap.length;xxx++){
            for(int yyy =0; yyy<tileMap[0].length;yyy++){

                if(tileMap[xxx][yyy]!=null)tileMap[xxx][yyy].findNextTile(arrowRight,arrowRightUp,arrowRightDown);

            }
        }

        System.out.println("finishedLoading");


    }

    public void render(Canvas canvas,Rect spaceToDraw){
        for(int xx=25; xx>=0;xx--){
            for(int yy =8; yy>=0;yy--){

                if(tileMap[xx][yy]!= null)tileMap[xx][yy].renderMap(canvas,spaceToDraw);



            }
        }
    }


    Tile getTileFromTileMap(int indexA, int indexB){

        return tileMap[indexA][indexB];
    }

}
