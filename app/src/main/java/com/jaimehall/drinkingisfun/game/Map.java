package com.jaimehall.drinkingisfun.game;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.jaimehall.drinkingisfun.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Map {

    private Tile[][] tileMap;

    public Map(Game game){
        tileMap= new Tile[20][7];
        createTileMap(game);
    }

    private void createTileMap(Game game){

        ArrayList<String> unSplitStringsEasyTasks= new ArrayList<String>();
        ArrayList<String> unSplitStringsMediumTasks= new ArrayList<String>();
        ArrayList<String> unSplitStringsHardTasks= new ArrayList<String>();

        AssetManager am = game.getContext().getAssets();

        BufferedReader brEasy = null;
        BufferedReader brMedium = null;
        BufferedReader brHard = null;
        try  {

            brEasy = new BufferedReader(new InputStreamReader(am.open("einfacheAufgaben.txt")));
            brMedium = new BufferedReader(new InputStreamReader(am.open("mittlereAufgaben.txt")));
            brHard = new BufferedReader(new InputStreamReader(am.open("schwereAufgaben.txt")));
            String lineEasy;
            while ((lineEasy = brEasy.readLine()) != null) {
                unSplitStringsEasyTasks.add(lineEasy);
            }
            String lineMedium;
            while ((lineMedium = brMedium.readLine()) != null) {
                unSplitStringsMediumTasks.add(lineMedium);
            }
            String lineHard;
            while ((lineHard = brHard.readLine()) != null) {
                unSplitStringsHardTasks.add(lineHard);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            if(brEasy!=null){
                try{
                    brEasy.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(brMedium!=null){
                try{
                    brEasy.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(brHard!=null){
                try{
                    brEasy.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

        Bitmap greenTile = BitmapFactory.decodeResource(game.getResources(), R.drawable.greentile);
        Bitmap blueTile = BitmapFactory.decodeResource(game.getResources(),R.drawable.bluetile);
        Bitmap redTile = BitmapFactory.decodeResource(game.getResources(),R.drawable.redtile);

        float tileWidth = 500;
        float tileHeight = 281;
        // (float)((xx*1117)+558.5)
        // (float)((yy*763)+381.5)


        tileMap[0][3] = new MiniGameTile(0*tileWidth, 3*tileHeight, tileWidth, tileHeight, redTile,this, new int[][]{{1,2},{1,3},{1,4}});


        tileMap[1][2] = new NormalTile(1 * tileWidth, 2*tileHeight, tileWidth, tileHeight, greenTile,this, 2,1,unSplitStringsHardTasks,6);
        tileMap[2][1] = new NormalTile(2*tileWidth, 1*tileHeight, tileWidth, tileHeight, greenTile,this, 3,0,unSplitStringsHardTasks,6);
        tileMap[3][0] = new NormalTile(3*tileWidth, 0*tileHeight, tileWidth, tileHeight, greenTile,this, 4,0,unSplitStringsHardTasks,6);
        tileMap[4][0] = new NormalTile(4*tileWidth, 0*tileHeight, tileWidth, tileHeight, greenTile,this, 5,0,unSplitStringsHardTasks,6);
        tileMap[5][0] = new NormalTile(5*tileWidth, 0*tileHeight, tileWidth, tileHeight, greenTile,this, 6,0,unSplitStringsHardTasks,6);
        tileMap[6][0] = new MiniGameTile(6*tileWidth, 0*tileHeight, tileWidth, tileHeight, redTile,this, new int[][]{{7,0},{7,1}});
        tileMap[7][0] = new NormalTile(7*tileWidth, 0*tileHeight, tileWidth, tileHeight, greenTile,this, 8,0,unSplitStringsHardTasks,6);
        tileMap[7][2] = new NormalTile(7*tileWidth, 2*tileHeight, tileWidth, tileHeight, greenTile,this, 8,1,unSplitStringsHardTasks,6);
        tileMap[8][0] = new NormalTile(8*tileWidth, 0*tileHeight, tileWidth, tileHeight, greenTile,this, 9,0,unSplitStringsHardTasks,6);
        tileMap[8][1] = new NormalTile(8*tileWidth, 1*tileHeight, tileWidth, tileHeight, greenTile,this, 9,0,unSplitStringsHardTasks,6);
        tileMap[9][0] = new NormalTile(9*tileWidth, 0*tileHeight, tileWidth, tileHeight, greenTile,this, 0,3,unSplitStringsHardTasks,6);


        tileMap[1][3] = new NormalTile(1 * tileWidth, 3*tileHeight, tileWidth, tileHeight, greenTile,this, 2,3,unSplitStringsMediumTasks,4);
        tileMap[2][3] = new NormalTile(2*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenTile,this, 3,3,unSplitStringsMediumTasks,4);
        tileMap[3][3] = new NormalTile(3*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenTile,this, 4,3,unSplitStringsMediumTasks,4);
        tileMap[4][3] = new NormalTile(4*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenTile,this, 5,3,unSplitStringsMediumTasks,4);
        tileMap[5][3] = new NormalTile(5*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenTile,this, 6,3,unSplitStringsMediumTasks,4);
        tileMap[6][3] = new MiniGameTile(6*tileWidth, 3*tileHeight, tileWidth, tileHeight, redTile,this, new int[][]{{7,2},{7,3},{7,4}});
        tileMap[7][1] = new NormalTile(7*tileWidth, 1*tileHeight, tileWidth, tileHeight, greenTile,this, 8,2,unSplitStringsMediumTasks,4);
        tileMap[7][3] = new NormalTile(7*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenTile,this, 8,3,unSplitStringsMediumTasks,4);
        tileMap[7][5] = new NormalTile(7*tileWidth, 5*tileHeight, tileWidth, tileHeight, greenTile,this, 8,4,unSplitStringsMediumTasks,4);
        tileMap[8][2] = new NormalTile(8*tileWidth, 2*tileHeight, tileWidth, tileHeight, greenTile,this, 9,3,unSplitStringsMediumTasks,4);
        tileMap[8][3] = new NormalTile(8*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenTile,this, 9,3,unSplitStringsMediumTasks,4);
        tileMap[8][4] = new NormalTile(8*tileWidth, 4*tileHeight, tileWidth, tileHeight, greenTile,this, 9,3,unSplitStringsMediumTasks,4);
        tileMap[9][3] = new NormalTile(9*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenTile,this, 0,3,unSplitStringsMediumTasks,4);


        tileMap[1][4] = new NormalTile(1 * tileWidth, 4*tileHeight, tileWidth, tileHeight, greenTile,this, 2,5,unSplitStringsEasyTasks,2);
        tileMap[2][5] = new NormalTile(2*tileWidth, 5*tileHeight, tileWidth, tileHeight, greenTile,this, 3,6,unSplitStringsEasyTasks,2);
        tileMap[3][6] = new NormalTile(3*tileWidth, 6*tileHeight, tileWidth, tileHeight, greenTile,this, 4,6,unSplitStringsEasyTasks,2);
        tileMap[4][6] = new NormalTile(4*tileWidth, 6*tileHeight, tileWidth, tileHeight, greenTile,this, 5,6,unSplitStringsEasyTasks,2);
        tileMap[5][6] = new NormalTile(5*tileWidth, 6*tileHeight, tileWidth, tileHeight, greenTile,this, 6,6,unSplitStringsEasyTasks,2);
        tileMap[6][6] = new MiniGameTile(6*tileWidth, 6*tileHeight, tileWidth, tileHeight, redTile,this, new int[][]{{7,5},{7,6}});
        tileMap[7][4] = new NormalTile(7*tileWidth, 4*tileHeight, tileWidth, tileHeight, greenTile,this, 8,5,unSplitStringsEasyTasks,2);
        tileMap[7][6] = new NormalTile(7*tileWidth, 6*tileHeight, tileWidth, tileHeight, greenTile,this, 8,6,unSplitStringsEasyTasks,2);
        tileMap[8][5] = new NormalTile(8*tileWidth, 5*tileHeight, tileWidth, tileHeight, greenTile,this, 9,6,unSplitStringsEasyTasks,2);
        tileMap[8][6] = new NormalTile(8*tileWidth, 6*tileHeight, tileWidth, tileHeight, greenTile,this, 9,6,unSplitStringsEasyTasks,2);
        tileMap[9][6] = new NormalTile(9*tileWidth, 6*tileHeight, tileWidth, tileHeight, greenTile,this, 0,3,unSplitStringsEasyTasks,2);


        for(int xxx=0; xxx<tileMap.length;xxx++){
            for(int yyy =0; yyy<tileMap[0].length;yyy++){

                if(tileMap[xxx][yyy]!=null)tileMap[xxx][yyy].findNextTile();

            }
        }
    }

    public void render(Canvas canvas){
        for(int xx=0; xx<20;xx++){
            for(int yy =0; yy<7;yy++){

                if(tileMap[xx][yy]!= null)tileMap[xx][yy].render(canvas);



            }
        }
    }

    public void render(Canvas canvas,Tile currentFocusedTile){
        for(int xx=0; xx<20;xx++){
            for(int yy =0; yy<7;yy++){

                if(tileMap[xx][yy]!=null){
                    if(tileMap[xx][yy].equals(currentFocusedTile))tileMap[xx][yy].render(canvas);
                }



            }
        }
    }

    public Tile getTileFromTileMap(int indexA, int indexB){
        if(tileMap[indexA][indexB]==null){
            System.out.println(indexA);
            System.out.println(indexB);
        }

        return tileMap[indexA][indexB];
    }
}
