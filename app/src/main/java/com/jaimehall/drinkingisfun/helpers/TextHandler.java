package com.jaimehall.drinkingisfun.helpers;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;

import com.jaimehall.drinkingisfun.activities.menu.SettingsActivity;
import com.jaimehall.drinkingisfun.game.Game;
import com.jaimehall.drinkingisfun.game.NormalTile;
import com.jaimehall.drinkingisfun.game.Player;
import com.jaimehall.drinkingisfun.game.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class TextHandler {

    private Random generator;
    private String[][] easyTasks;
    private String[][] mediumTasks;
    private String[][] hardTasks;
    private String[][] tasksForAllPlayers;

    private double drinkMultiplier;

    public TextHandler(Game game){

        ArrayList<String> unSplitStringsEasyTasks= new ArrayList<>();
        ArrayList<String> unSplitStringsMediumTasks= new ArrayList<>();
        ArrayList<String> unSplitStringsHardTasks= new ArrayList<>();
        ArrayList<String> unSplitStringsAllPlayersTasks= new ArrayList<>();

        AssetManager am = game.getContext().getAssets();

        BufferedReader brEasy = null;
        BufferedReader brMedium = null;
        BufferedReader brHard = null;
        BufferedReader brAllPlayers = null;
        try  {

            brEasy = new BufferedReader(new InputStreamReader(am.open("einfacheAufgaben.txt")));
            brMedium = new BufferedReader(new InputStreamReader(am.open("mittlereAufgaben.txt")));
            brHard = new BufferedReader(new InputStreamReader(am.open("schwereAufgaben.txt")));
            brAllPlayers = new BufferedReader(new InputStreamReader(am.open("anAlleAufgaben.txt")));
            String lineEasy;
            while ((lineEasy=brEasy.readLine()) != null) {
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
            String lineAll;
            while ((lineAll = brAllPlayers.readLine()) != null) {
                unSplitStringsAllPlayersTasks.add(lineAll);
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
                    brMedium.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(brHard!=null){
                try{
                    brHard.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(brAllPlayers!=null){
                try{
                    brAllPlayers.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

        easyTasks = new String[unSplitStringsEasyTasks.size()][];
        mediumTasks= new String[unSplitStringsMediumTasks.size()][];
        hardTasks= new String[unSplitStringsHardTasks.size()][];
        tasksForAllPlayers = new String[unSplitStringsAllPlayersTasks.size()][];

        for(int i = 0; i<unSplitStringsEasyTasks.size() ; i++){
            StringTokenizer tokens = new StringTokenizer(unSplitStringsEasyTasks.get(i),":");
            easyTasks[i] = new String[tokens.countTokens()];
            int r = 0;
            while(tokens.hasMoreTokens()){
                easyTasks[i][r]=tokens.nextToken();
                r++;
            }
        }
        for(int i = 0; i<unSplitStringsMediumTasks.size() ; i++){
            StringTokenizer tokens = new StringTokenizer(unSplitStringsMediumTasks.get(i),":");
            mediumTasks[i] = new String[tokens.countTokens()];
            int r = 0;
            while(tokens.hasMoreTokens()){
                mediumTasks[i][r]=tokens.nextToken();
                r++;
            }
        }
        for(int i = 0; i<unSplitStringsHardTasks.size() ; i++){
            StringTokenizer tokens = new StringTokenizer(unSplitStringsHardTasks.get(i),":");
            hardTasks[i] = new String[tokens.countTokens()];
            int r = 0;
            while(tokens.hasMoreTokens()){
                hardTasks[i][r]=tokens.nextToken();
                r++;
            }
        }
        for(int i = 0; i<unSplitStringsAllPlayersTasks.size() ; i++){
            StringTokenizer tokens = new StringTokenizer(unSplitStringsAllPlayersTasks.get(i),":");
            tasksForAllPlayers[i] = new String[tokens.countTokens()];
            int r = 0;
            while(tokens.hasMoreTokens()){
                tasksForAllPlayers[i][r]=tokens.nextToken();
                r++;
            }
        }

        generator = new Random(Math.round(Math.random()*23469823));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(game.getContext());
        drinkMultiplier = Double.parseDouble(sharedPreferences.getString(SettingsActivity.KEY_PREF_DRINK_FACTOR,"" ));
        System.out.println("Multi:      " +drinkMultiplier);
    }

    public String getCompleteTask(Player currentPlayer){
        Tile location  = currentPlayer.getLocation();
        int randomIndex;

        String[] unprocessedTask;

        if(location.isGoal()){
            unprocessedTask = new String[1];
            unprocessedTask[0] = "Ihr seid am Ziel angekommen!";
        }
        else if(location.isMiniGame()){
            unprocessedTask = new String[2];
            unprocessedTask[0] = "PlayerName";
            unprocessedTask[1] = ", mach dich bereit ein Minispiel zu spielen!";
        }
        else{
            if(((NormalTile)location).isBlueTile()){
                randomIndex = generator.nextInt(tasksForAllPlayers.length);
                unprocessedTask = tasksForAllPlayers[randomIndex];
            }
            else{
                if(location.getTileDifficulty() == 0){
                    randomIndex = generator.nextInt(easyTasks.length);
                    unprocessedTask = easyTasks[randomIndex];
                }
                else if(location.getTileDifficulty() == 1){
                    randomIndex = generator.nextInt(mediumTasks.length);
                    unprocessedTask = mediumTasks[randomIndex];
                }
                else{
                    randomIndex = generator.nextInt(hardTasks.length);
                    unprocessedTask = hardTasks[randomIndex];
                }
            }
        }


        StringBuilder completeBufferedInformation = new StringBuilder();

        for(int i = 0;i<unprocessedTask.length;i++) {
            if (unprocessedTask[i].matches("PlayerName")) {
                completeBufferedInformation.append(currentPlayer.getPlayerName());

            } else if (unprocessedTask[i].matches("EinzahlGeschlecht")) {

                switch(currentPlayer.getSex()){
                    case "male":
                        completeBufferedInformation.append("er");
                        break;
                    case "female":
                        completeBufferedInformation.append("sie");
                        break;
                    case "unknown":
                        completeBufferedInformation.append("es");
                        break;
                }
            } else if (unprocessedTask[i].matches("MehrzahlGeschlecht")) {

                switch(currentPlayer.getSex()){
                    case "male":
                        completeBufferedInformation.append("seiner");
                        break;
                    case "female":
                        completeBufferedInformation.append("ihrer");
                        break;
                    case "unknown":
                        completeBufferedInformation.append("seiner");
                        break;
                }
            } else if (unprocessedTask[i].matches("AnzahlSchlucke")) {
                long anzahlSchlucke = Math.round( (Math.random() * (currentPlayer.getLocation().getTileDifficulty()+1*drinkMultiplier)));
                while (anzahlSchlucke == 0) {
                    anzahlSchlucke = Math.round( (Math.random() * (currentPlayer.getLocation().getTileDifficulty() +1* drinkMultiplier)));
                }
                if (anzahlSchlucke != 1) {
                    completeBufferedInformation.append(anzahlSchlucke + " Schlucke");
                } else {
                    completeBufferedInformation.append(anzahlSchlucke + " Schluck");
                }

            } else {
                completeBufferedInformation.append(unprocessedTask[i]);
            }
        }

        return completeBufferedInformation.toString();

    }

}
