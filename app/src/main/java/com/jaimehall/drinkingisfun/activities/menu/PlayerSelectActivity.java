package com.jaimehall.drinkingisfun.activities.menu;


import android.content.Intent;
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
import com.jaimehall.drinkingisfun.helpers.CharacterIO;

import java.io.File;
import java.util.ArrayList;

public class PlayerSelectActivity extends Activity {

    private final int SELECT_CHARACTERS = 34;

    private ArrayList<EditText> playerNames;
    private EditText tempEditText;
    private int indexForPlayerNames =0;

    private ArrayList<CheckBox> playerSexes;
    private CheckBox tempCheckBox;
    private LinearLayout linearLayout;
    private Button playButton;
    private LinearLayout.LayoutParams editTextParams;
    private LinearLayout.LayoutParams checkBoxParams;
    private LinearLayout.LayoutParams horizontalLinearLayoutParams;

    private CharacterIO characterIO;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select);

        characterIO = (CharacterIO) getIntent().getSerializableExtra("characterIO");

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
                    if(!playerInputCreated){
                        addNewPlayerInput();
                        addTempViewsToList();
                        playerInputCreated=true;
                    }
                    playButton.setText(R.string.activity_player_select_play);
                    playButton.setTextColor(getResources().getColor(R.color.colorPrimary));

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
                playButton.setText(R.string.activity_player_select_play);
                playButton.setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        });



    }

    public void startCharacterSelection(View view){
        Intent intent = new Intent(this,CharacterSelectActivity.class);
        intent.putExtra("characterIO",characterIO);
        startActivityForResult(intent,SELECT_CHARACTERS);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_CHARACTERS){

                addCharacters(data.getBooleanArrayExtra("charactersSelected"));
            }
        }
    }

    private void addCharacters(boolean[] selectedCharacters){
        String[] characterNames = characterIO.getCharacterNames();
        boolean[] characterSexes = characterIO.getCharacterSexes();

        for(int i = 0;i<characterNames.length;i++){
            if(selectedCharacters[i]){
                while(playerNames.get(indexForPlayerNames).getText().length() != 0){
                    indexForPlayerNames++;
                }
                playerNames.get(indexForPlayerNames).setText(characterNames[i]);
                playerSexes.get(indexForPlayerNames).setChecked(characterSexes[i]);
            }
        }
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
                    if(!playerInputCreated){
                        addNewPlayerInput();
                        addTempViewsToList();
                        playerInputCreated=true;
                    }
                    playButton.setText(R.string.activity_player_select_play);
                    playButton.setTextColor(getResources().getColor(R.color.colorPrimary));


                }
            }
        });

    }

    public void startGame(View view){

        if(playerNames.get(0).getText().length()==0 || playerNames.get(1).getText().length()==0) {
            playButton.setText(R.string.activity_player_not_enough_players);
            playButton.setTextColor(getResources().getColor(R.color.lightRed));
        }
        else{
            int arrayLength = 0;

            while(playerNames.get(arrayLength).getText().length() != 0){
                arrayLength++;
            }

            String[] characterNames = characterIO.getCharacterNames();
            String[] characterBitmapPaths = characterIO.getCharacterIcons();
            boolean[] characterSexes = characterIO.getCharacterSexes();

            String[] playerStringNames = new String[arrayLength];
            String[] playerBitmapIconPaths = new String[arrayLength];
            boolean[] playerBooleanSexes = new boolean[arrayLength];

            for (int i = 0; i < arrayLength; i++) {
                if(playerNames.get(i).getText().length()!=0) {

                    if(characterIO.checkForExistingCharacters(playerNames.get(i).getText().toString()) != (-1)){
                        int characterIndex = characterIO.checkForExistingCharacters(playerNames.get(i).getText().toString());
                        playerStringNames[i] = characterNames[characterIndex];
                        File tempIcon = new File(characterIO.getCharacterDirectory(),characterBitmapPaths[characterIndex]);
                        playerBitmapIconPaths[i] = tempIcon.getPath();
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






}
