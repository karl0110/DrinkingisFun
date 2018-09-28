package com.jaimehall.drinkingisfun.game;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.jaimehall.drinkingisfun.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Map {

    private Tile[][] tileMap;
    private float tileWidth,tileHeight;

    public Map(Game game){
        tileMap= new Tile[20][7];
        tileWidth = 500;
        tileHeight = 281;
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

        String[][] tokenedStringEasyTasks = new String[unSplitStringsEasyTasks.size()][];
        String[][] tokenedStringMediumTasks= new String[unSplitStringsMediumTasks.size()][];
        String[][] tokenedStringHardTasks= new String[unSplitStringsHardTasks.size()][];

        for(int i = 0; i<unSplitStringsEasyTasks.size() ; i++){
            StringTokenizer tokens = new StringTokenizer(unSplitStringsEasyTasks.get(i),":");
            tokenedStringEasyTasks[i] = new String[tokens.countTokens()];
            int r = 0;
            while(tokens.hasMoreTokens()){
                tokenedStringEasyTasks[i][r]=tokens.nextToken();
                r++;
            }
        }
        for(int i = 0; i<unSplitStringsMediumTasks.size() ; i++){
            StringTokenizer tokens = new StringTokenizer(unSplitStringsMediumTasks.get(i),":");
            tokenedStringMediumTasks[i] = new String[tokens.countTokens()];
            int r = 0;
            while(tokens.hasMoreTokens()){
                tokenedStringMediumTasks[i][r]=tokens.nextToken();
                r++;
            }
        }
        for(int i = 0; i<unSplitStringsHardTasks.size() ; i++){
            StringTokenizer tokens = new StringTokenizer(unSplitStringsHardTasks.get(i),":");
            tokenedStringHardTasks[i] = new String[tokens.countTokens()];
            int r = 0;
            while(tokens.hasMoreTokens()){
                tokenedStringHardTasks[i][r]=tokens.nextToken();
                r++;
            }
        }

        Resources resources = game.getResources();

        Bitmap miniGameBorder = BitmapFactory.decodeResource(resources,R.drawable.minigameumrandung);
        Bitmap greenBorder = BitmapFactory.decodeResource(resources,R.drawable.grueneumrandung);
        Bitmap blueBorder = BitmapFactory.decodeResource(resources,R.drawable.blaueumrandung);

        Bitmap background = BitmapFactory.decodeResource(resources,R.drawable.uebergehenderhintergrund);


        tileMap[0][3] = new MiniGameTile(0*tileWidth, 3*tileHeight, tileWidth, tileHeight, miniGameBorder,background,this, new int[][]{{1,4},{1,3},{1,2}},1);


        tileMap[1][2] = new NormalTile(1 * tileWidth, 2*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 2,1,tokenedStringHardTasks,2);
        tileMap[2][1] = new NormalTile(2*tileWidth, 1*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 3,0,tokenedStringHardTasks,2);
        tileMap[3][0] = new NormalTile(3*tileWidth, 0*tileHeight, tileWidth, tileHeight, blueBorder,background,this, 4,0,tokenedStringHardTasks,2);
        tileMap[4][0] = new NormalTile(4*tileWidth, 0*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 5,0,tokenedStringHardTasks,2);
        tileMap[5][0] = new NormalTile(5*tileWidth, 0*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 6,0,tokenedStringHardTasks,2);
        tileMap[6][0] = new MiniGameTile(6*tileWidth, 0*tileHeight, tileWidth, tileHeight, miniGameBorder,background,this, new int[][]{{7,1},{7,0},{0,0}},2);
        tileMap[7][0] = new NormalTile(7*tileWidth, 0*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 8,0,tokenedStringHardTasks,2);
        tileMap[7][2] = new NormalTile(7*tileWidth, 2*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 8,1,tokenedStringHardTasks,2);
        tileMap[8][0] = new NormalTile(8*tileWidth, 0*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 9,0,tokenedStringHardTasks,2);
        tileMap[8][1] = new NormalTile(8*tileWidth, 1*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 9,0,tokenedStringHardTasks,2);
        tileMap[9][0] = new NormalTile(9*tileWidth, 0*tileHeight, tileWidth, tileHeight, blueBorder,background,this, 0,3,tokenedStringHardTasks,2);


        tileMap[1][3] = new NormalTile(1 * tileWidth, 3*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 2,3,tokenedStringMediumTasks,1);
        tileMap[2][3] = new NormalTile(2*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 3,3,tokenedStringMediumTasks,1);
        tileMap[3][3] = new NormalTile(3*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 4,3,tokenedStringMediumTasks,1);
        tileMap[4][3] = new NormalTile(4*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 5,3,tokenedStringMediumTasks,1);
        tileMap[5][3] = new NormalTile(5*tileWidth, 3*tileHeight, tileWidth, tileHeight, blueBorder,background,this, 6,3,tokenedStringMediumTasks,1);
        tileMap[6][3] = new MiniGameTile(6*tileWidth, 3*tileHeight, tileWidth, tileHeight, miniGameBorder,background,this, new int[][]{{7,4},{7,3},{7,2}},1);
        tileMap[7][1] = new NormalTile(7*tileWidth, 1*tileHeight, tileWidth, tileHeight, blueBorder,background,this, 8,2,tokenedStringMediumTasks,1);
        tileMap[7][3] = new NormalTile(7*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 8,3,tokenedStringMediumTasks,1);
        tileMap[7][5] = new NormalTile(7*tileWidth, 5*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 8,4,tokenedStringMediumTasks,1);
        tileMap[8][2] = new NormalTile(8*tileWidth, 2*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 9,3,tokenedStringMediumTasks,1);
        tileMap[8][3] = new NormalTile(8*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 9,3,tokenedStringMediumTasks,1);
        tileMap[8][4] = new NormalTile(8*tileWidth, 4*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 9,3,tokenedStringMediumTasks,1);
        tileMap[9][3] = new NormalTile(9*tileWidth, 3*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 0,3,tokenedStringMediumTasks,1);


        tileMap[1][4] = new NormalTile(1 * tileWidth, 4*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 2,5,tokenedStringEasyTasks,0);
        tileMap[2][5] = new NormalTile(2*tileWidth, 5*tileHeight, tileWidth, tileHeight, blueBorder,background,this, 3,6,tokenedStringEasyTasks,0);
        tileMap[3][6] = new NormalTile(3*tileWidth, 6*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 4,6,tokenedStringEasyTasks,0);
        tileMap[4][6] = new NormalTile(4*tileWidth, 6*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 5,6,tokenedStringEasyTasks,0);
        tileMap[5][6] = new NormalTile(5*tileWidth, 6*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 6,6,tokenedStringEasyTasks,0);
        tileMap[6][6] = new MiniGameTile(6*tileWidth, 6*tileHeight, tileWidth, tileHeight, miniGameBorder,background,this, new int[][]{{0,0},{7,6},{7,5}},0);
        tileMap[7][4] = new NormalTile(7*tileWidth, 4*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 8,5,tokenedStringEasyTasks,0);
        tileMap[7][6] = new NormalTile(7*tileWidth, 6*tileHeight, tileWidth, tileHeight, blueBorder,background,this, 8,6,tokenedStringEasyTasks,0);
        tileMap[8][5] = new NormalTile(8*tileWidth, 5*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 9,6,tokenedStringEasyTasks,0);
        tileMap[8][6] = new NormalTile(8*tileWidth, 6*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 9,6,tokenedStringEasyTasks,0);
        tileMap[9][6] = new NormalTile(9*tileWidth, 6*tileHeight, tileWidth, tileHeight, greenBorder,background,this, 0,3,tokenedStringEasyTasks,0);


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
