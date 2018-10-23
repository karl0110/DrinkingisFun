package com.jaimehall.drinkingisfun.helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class CharacterIO implements Serializable {

    private File characterDirectory;
    private File characterTextPath;

    private String[] characterNames;
    private String[] characterIcons;
    private boolean[] characterSexes;

    public CharacterIO(Context context){

        ContextWrapper cw = new ContextWrapper(context);
        characterDirectory = cw.getDir("characters",Context.MODE_PRIVATE);
        characterTextPath = new File(characterDirectory,"playerInformation");

        if(!characterDirectory.exists()){
            characterDirectory.mkdir();
        }

        if(!characterTextPath.exists()){
            try {
                characterTextPath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void deleteAllCharacters(){
        File[] filesInCharacterDirectory =characterDirectory.listFiles();

        for(int i = 0; i<filesInCharacterDirectory.length;i++){
            if(!filesInCharacterDirectory[i].equals(characterTextPath)){
                filesInCharacterDirectory[i].delete();
            }
        }

        try {
            characterTextPath.delete();
            characterTextPath.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteCharacter(String characterName){

        ArrayList<String> untokenedLines= new ArrayList<String>();
        BufferedReader nameReader;
        try {
            FileInputStream fis= new FileInputStream(characterTextPath);
            DataInputStream in = new DataInputStream(fis);
            nameReader = new BufferedReader(new InputStreamReader(in));
            String line;

            while((line = nameReader.readLine())!= null){
                untokenedLines.add(line);

            }
            fis.close();
            in.close();

        }catch(IOException e){
            e.printStackTrace();
        }

        int indexLineToBeDeleted =0;

        for(int i = 0; i<untokenedLines.size() ; i++) {
            StringTokenizer tokens = new StringTokenizer(untokenedLines.get(i), ":");
            if(tokens.hasMoreTokens()) {
                if (tokens.nextToken().matches(characterName)) {
                    indexLineToBeDeleted = i;
                }
            }

        }


        BufferedWriter nameWriter;

        try{
            characterTextPath.delete();
            characterTextPath.createNewFile();
            nameWriter = new BufferedWriter(new FileWriter(characterTextPath,true));


            for(int i=0;i<untokenedLines.size();i++){
                if(i != indexLineToBeDeleted){
                    nameWriter.write(untokenedLines.get(i));
                    nameWriter.newLine();
                }

            }
            nameWriter.close();


        } catch(Exception e){
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }


    }

    private void refreshPlayerList(){

        ArrayList<String> untokenedPlayerNames= new ArrayList<String>();
        BufferedReader nameReader;
        try {
            FileInputStream fis= new FileInputStream(characterTextPath);
            DataInputStream in = new DataInputStream(fis);
            nameReader = new BufferedReader(new InputStreamReader(in));
            String line;

            while((line = nameReader.readLine())!= null){
                if(!line.matches("")) {
                    untokenedPlayerNames.add(line);
                }

            }
            fis.close();
            in.close();

        }catch(IOException e){
            e.printStackTrace();
        }


        characterNames = new String[untokenedPlayerNames.size()];
        characterIcons = new String[untokenedPlayerNames.size()];
        characterSexes = new boolean[untokenedPlayerNames.size()];

        for(int i = 0; i<untokenedPlayerNames.size() ; i++) {
            StringTokenizer tokens = new StringTokenizer(untokenedPlayerNames.get(i), ":");
            if(tokens.hasMoreTokens()){
                characterNames[i] = tokens.nextToken();
                characterIcons[i] = tokens.nextToken();
                if(tokens.nextToken().matches("true")){
                    characterSexes[i] = true;
                }
                else{
                    characterSexes[i] = false;
                }
            }

        }

    }

    public void save(String characterIcon,Bitmap characterIconBitmap,String characterName,boolean characterSex){

        File myImagePath = new File(characterDirectory,characterIcon);
        if(!myImagePath.exists()){
            try {
                myImagePath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter nameWriter;
        FileOutputStream outputStreamImage;

        try{
            outputStreamImage = new FileOutputStream(myImagePath);
            characterIconBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStreamImage);
            outputStreamImage.close();

            String textToWrite = (":"+characterName+":"+characterIcon+":"+characterSex);

            nameWriter = new BufferedWriter(new FileWriter(characterTextPath,true));
            nameWriter.write(textToWrite);
            nameWriter.newLine();
            nameWriter.close();


        } catch(Exception e){
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }
    }

    public void replaceCharacter(String replaceCharacterName,String newCharacterName,boolean newCharacterSex,String characterImage,Bitmap characterIcon){

        ArrayList<String> untokenedLines= new ArrayList<String>();
        BufferedReader nameReader;
        try {
            FileInputStream fis= new FileInputStream(characterTextPath);
            DataInputStream in = new DataInputStream(fis);
            nameReader = new BufferedReader(new InputStreamReader(in));
            String line;

            while((line = nameReader.readLine())!= null){
                untokenedLines.add(line);

            }
            fis.close();
            in.close();

        }catch(IOException e){
            e.printStackTrace();
        }

        int indexLineToBeDeleted =0;

        for(int i = 0; i<untokenedLines.size() ; i++) {
            StringTokenizer tokens = new StringTokenizer(untokenedLines.get(i), ":");
            if(tokens.hasMoreTokens()){
                if(tokens.nextToken().matches(replaceCharacterName)){
                    indexLineToBeDeleted = i;
                }
            }

        }

        File characterImagePath = new File(characterDirectory,characterImage);

        BufferedWriter nameWriter;
        FileOutputStream outputStreamImage;

        try{
            if(characterIcon != null) {
                outputStreamImage = new FileOutputStream(characterImagePath);
                characterIcon.compress(Bitmap.CompressFormat.PNG, 100, outputStreamImage);
                outputStreamImage.close();
            }

            characterTextPath.delete();
            characterTextPath.createNewFile();
            nameWriter = new BufferedWriter(new FileWriter(characterTextPath,true));

            String textToWrite = (":"+newCharacterName+":"+characterImage+":"+newCharacterSex);

            for(int i=0;i<untokenedLines.size();i++){
                if(i == indexLineToBeDeleted){
                    nameWriter.write(textToWrite);
                }
                else{
                    nameWriter.write(untokenedLines.get(i));
                }
                nameWriter.newLine();
            }
            nameWriter.close();


        } catch(Exception e){
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }

    }

    public int checkForExistingCharacters(String characterName){
        int returnInt = -1;

        for(int i = 0;i<characterNames.length;i++){
            if(characterNames[i].matches(characterName)){
                returnInt  = i ;
            }
        }

        return returnInt;
    }

    public String[] getCharacterNames() {
        refreshPlayerList();
        return characterNames;
    }

    public String[] getCharacterIcons() {
        return characterIcons;
    }

    public boolean[] getCharacterSexes() {
        return characterSexes;
    }

    public File getCharacterDirectory() {
        return characterDirectory;
    }
}
