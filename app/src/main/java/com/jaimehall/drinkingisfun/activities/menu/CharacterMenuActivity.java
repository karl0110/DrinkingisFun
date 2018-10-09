package com.jaimehall.drinkingisfun.activities.menu;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.app.Activity;
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

public class CharacterMenuActivity extends Activity {

    private ArrayList<String> playerNames= new ArrayList<String>();
    private File characterDirectory;
    private File characterTextPath;

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_menu);
        linearLayout = findViewById(R.id.characterMenuLinearLayout);
        refreshPlayerList();

    }

    public void startCharacterCreation(View view){
        Intent intent = new Intent(this,CharacterCreationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("resuming");
        refreshPlayerList();
    }

    private void refreshPlayerList(){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        characterDirectory = cw.getDir("characters", Context.MODE_PRIVATE);
        characterTextPath = new File(characterDirectory,"playerInformation");

        if(characterTextPath.exists()){
            System.out.println("reading characters");
            linearLayout.removeAllViews();
            playerNames.clear();


            BufferedReader nameReader = null;
            try {
                FileInputStream fis= new FileInputStream(characterTextPath);
                DataInputStream in = new DataInputStream(fis);
                nameReader = new BufferedReader(new InputStreamReader(in));
                String line;

                while((line = nameReader.readLine())!= null){
                    playerNames.add(line);

                }
                fis.close();
                in.close();
                System.out.println("lines:"+playerNames.size());

            }catch(IOException e){
                e.printStackTrace();
            }

            LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            for(int i = 0;i<playerNames.size();i++){
                Button button = new Button(this);
                button.setText(playerNames.get(i));
                button.setLayoutParams(buttonLayout);
                linearLayout.addView(button);

            }




        }
        else{
            System.out.println("File doesnt exist");
        }




    }
}
