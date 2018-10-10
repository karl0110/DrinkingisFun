package com.jaimehall.drinkingisfun.activities.menu;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.activities.GameActivity;
import com.jaimehall.drinkingisfun.helpers.BitmapLoader;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class PlayerSelectActivity extends Activity {

    private ArrayList<EditText> playerNames;
    private EditText tempEditText;
    private ArrayList<CheckBox> playerSexes;
    private CheckBox tempCheckBox;
    private LinearLayout linearLayout;
    private Button playButton;
    private LinearLayout.LayoutParams editTextParams;
    private LinearLayout.LayoutParams checkBoxParams;
    private LinearLayout.LayoutParams horizontalLinearLayoutParams;


    private String[] characterNames;
    private String[] characterBitmapPaths;
    private boolean[] characterSexes;
    private File characterDirectory;
    private File characterTextPath;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);

        playerNames=new ArrayList<>();
        playerSexes= new ArrayList<>();

        linearLayout = findViewById(R.id.playerNameLinearLayout);
        playButton = findViewById(R.id.playerSelectPlayButton);

        editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        checkBoxParams = new LinearLayout.LayoutParams(1,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        horizontalLinearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        final EditText firstEditText = new EditText(getApplicationContext());
        playerNames.add(firstEditText);
        firstEditText.setHint("Name");
        firstEditText.setLayoutParams(editTextParams);
        firstEditText.setEms(10);

        final EditText secondEditText = new EditText(getApplicationContext());
        playerNames.add(secondEditText);
        secondEditText.setHint("Name");
        secondEditText.setLayoutParams(editTextParams);
        secondEditText.setEms(10);

        CheckBox firstCheckBox = new CheckBox(getApplicationContext());
        playerSexes.add(firstCheckBox);
        firstCheckBox.setLayoutParams(checkBoxParams);

        final CheckBox secondCheckBox = new CheckBox(getApplicationContext());
        playerSexes.add(secondCheckBox);
        secondCheckBox.setLayoutParams(checkBoxParams);

        LinearLayout firstHorizontalLayout = new LinearLayout(this);
        firstHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        firstHorizontalLayout.setLayoutParams(new WindowManager.LayoutParams());
        firstHorizontalLayout.setLayoutParams(horizontalLinearLayoutParams);

        firstHorizontalLayout.addView(firstEditText);
        firstHorizontalLayout.addView(firstCheckBox);

        LinearLayout secondHorizontalLayout = new LinearLayout(this);
        secondHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        secondHorizontalLayout.setLayoutParams(new WindowManager.LayoutParams());
        secondHorizontalLayout.setLayoutParams(horizontalLinearLayoutParams);

        secondHorizontalLayout.addView(secondEditText);
        secondHorizontalLayout.addView(secondCheckBox);

        linearLayout.addView(firstHorizontalLayout);
        linearLayout.addView(secondHorizontalLayout);


        secondEditText.addTextChangedListener(new TextWatcher() {
            private boolean playerInputCreated=false;
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>0){
                    if(playerInputCreated==false){
                        addNewPlayerInput();
                        addTempViewsToList();
                        playerInputCreated=true;
                    }
                    playButton.setText("Play");
                    playButton.setTextColor(Color.BLACK);

                    if(checkForExistingCharacters(editable.toString()) != (-1)){
                        secondEditText.setBackgroundColor(Color.rgb(220,20,20));
                    }
                    else{
                        secondEditText.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });

        firstEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                playButton.setText("Play");
                playButton.setTextColor(Color.BLACK);

                if(checkForExistingCharacters(editable.toString()) != (-1)){
                    firstEditText.setBackgroundColor(Color.rgb(220,20,20));
                }
                else{
                    firstEditText.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        characterDirectory = cw.getDir("characters", Context.MODE_PRIVATE);
        characterTextPath = new File(characterDirectory,"playerInformation");

        refreshPlayerList();


    }

    public void addTempViewsToList(){
        playerNames.add(tempEditText);
        playerSexes.add(tempCheckBox);
    }

    public void addNewPlayerInput(){
        tempEditText = new EditText(getApplicationContext());
        tempEditText.setHint("Name");
        tempEditText.setLayoutParams(editTextParams);
        tempEditText.setEms(10);

        tempCheckBox = new CheckBox(getApplicationContext());
        tempCheckBox.setLayoutParams(checkBoxParams);

        LinearLayout horizontalLayout = new LinearLayout(this);
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLayout.setLayoutParams(new WindowManager.LayoutParams());
        horizontalLayout.setLayoutParams(horizontalLinearLayoutParams);

        horizontalLayout.addView(tempEditText);
        horizontalLayout.addView(tempCheckBox);

        linearLayout.addView(horizontalLayout);

        tempEditText.addTextChangedListener(new TextWatcher() {
            private boolean playerInputCreated=false;
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>0){
                    if(playerInputCreated==false){
                        addNewPlayerInput();
                        addTempViewsToList();
                        playerInputCreated=true;
                    }
                    playButton.setText("Play");
                    playButton.setTextColor(Color.BLACK);

                    if(checkForExistingCharacters(editable.toString()) != (-1)){
                        tempEditText.setBackgroundColor(Color.rgb(220,20,20));
                    }
                    else{
                        tempEditText.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });

    }

    public void startGame(View view){
        if(playerNames.get(0).getText().length()==0 || playerNames.get(1).getText().length()==0) {
            playButton.setText("Bitte gibt eure Namen ein");
            playButton.setTextColor(Color.RED);
        }
        else{
            int arrayLength = 0;

            while(playerNames.get(arrayLength).getText().length() != 0){
                arrayLength++;
            }

            String[] playerStringNames = new String[arrayLength];
            String[] playerBitmapIconPaths = new String[arrayLength];
            boolean[] playerBooleanSexes = new boolean[arrayLength];

            for (int i = 0; i < arrayLength; i++) {
                if(playerNames.get(i).getText().length()!=0) {

                    if(checkForExistingCharacters(playerNames.get(i).getText().toString()) != (-1)){
                        int characterIndex = checkForExistingCharacters(playerNames.get(i).getText().toString());
                        playerStringNames[i] = characterNames[characterIndex];
                        playerBitmapIconPaths[i] = characterBitmapPaths[characterIndex];
                        playerBooleanSexes[i] = characterSexes[characterIndex];
                    }
                    else{
                        playerStringNames[i] = playerNames.get(i).getText().toString();
                        playerBitmapIconPaths[i] = "Color";
                        playerBooleanSexes[i] = playerSexes.get(i).isChecked();
                    }

                }
            }


            Intent intent = new Intent(this, GameActivity.class);

            intent.putExtra("playerNames",playerStringNames);
            intent.putExtra("playerIcons",playerBitmapIconPaths);
            intent.putExtra("playerSexes",playerBooleanSexes);

            startActivity(intent);
        }
    }

    private void refreshPlayerList(){
        if(characterTextPath.exists()){

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
            characterBitmapPaths = new String[untokenedPlayerNames.size()];
            characterSexes = new boolean[untokenedPlayerNames.size()];

            File myImagePath;

            for(int i = 0; i<untokenedPlayerNames.size() ; i++) {
                StringTokenizer tokens = new StringTokenizer(untokenedPlayerNames.get(i), ":");
                if(tokens.hasMoreTokens()){
                    characterNames[i] = tokens.nextToken();


                    myImagePath = new File(characterDirectory,tokens.nextToken());
                    characterBitmapPaths[i] = myImagePath.getPath();


                    if(tokens.nextToken().matches("true")){
                        characterSexes[i] = true;
                    }
                    else{
                        characterSexes[i] = false;
                    }
                }

            }

        }
    }

    private int checkForExistingCharacters(String string){

        int returnInt = -1;

        for(int i = 0;i<characterNames.length;i++){
            if(characterNames[i].matches(string)){
                returnInt  = i ;
            }
        }

        return returnInt;
    }



}
