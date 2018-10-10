package com.jaimehall.drinkingisfun.activities.menu;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jaimehall.drinkingisfun.R;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class CharacterMenuActivity extends Activity {

    private String[] playerNames;
    private String[] imagePaths;
    private boolean[] playerSexes;
    private File characterDirectory;
    private File characterTextPath;

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_menu);
        linearLayout = findViewById(R.id.characterMenuLinearLayout);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        characterDirectory = cw.getDir("characters", Context.MODE_PRIVATE);
        characterTextPath = new File(characterDirectory,"playerInformation");

        refreshPlayerList();

    }

    public void startCharacterCreation(View view){
        Intent intent = new Intent(this,CharacterCreationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPlayerList();
    }

    public void deleteAllCharacters(View view){

        try {
            characterTextPath.delete();
            characterTextPath.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshPlayerList();
    }

    private void refreshPlayerList(){


        if(characterTextPath.exists()){

            linearLayout.removeAllViews();

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

            playerNames = new String[untokenedPlayerNames.size()];
            imagePaths = new String[untokenedPlayerNames.size()];
            playerSexes = new boolean[untokenedPlayerNames.size()];

            for(int i = 0; i<untokenedPlayerNames.size() ; i++) {
                StringTokenizer tokens = new StringTokenizer(untokenedPlayerNames.get(i), ":");
                if(tokens.hasMoreTokens()){
                    playerNames[i] = tokens.nextToken();
                    imagePaths[i] = tokens.nextToken();
                    if(tokens.nextToken().matches("true")){
                        playerSexes[i] = true;
                    }
                    else{
                        playerSexes[i] = false;
                    }
                }

            }


            LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            for(int i = 0;i<playerNames.length;i++){
                Button button = new Button(this);
                button.setText(playerNames[i]);
                button.setLayoutParams(buttonLayout);
                button.setTextSize(30f);
                button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                linearLayout.addView(button);

            }
        }
    }
}
