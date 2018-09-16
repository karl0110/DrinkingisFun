package com.jaimehall.drinkingisfun.activities.menu;


import android.content.Intent;
import android.graphics.Color;
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

import java.util.ArrayList;

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

        EditText firstEditText = new EditText(getApplicationContext());
        playerNames.add(firstEditText);
        firstEditText.setHint("Name");
        firstEditText.setLayoutParams(editTextParams);
        firstEditText.setEms(10);

        EditText secondEditText = new EditText(getApplicationContext());
        playerNames.add(secondEditText);
        secondEditText.setHint("Name");
        secondEditText.setLayoutParams(editTextParams);
        secondEditText.setEms(10);

        CheckBox firstCheckBox = new CheckBox(getApplicationContext());
        playerSexes.add(firstCheckBox);
        firstCheckBox.setLayoutParams(checkBoxParams);

        CheckBox secondCheckBox = new CheckBox(getApplicationContext());
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
            }
        });
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
            ArrayList<String> playerStringNames = new ArrayList<>();
            boolean[] playerBooleanSexes = new boolean[playerSexes.size()];
            for (int i = 0; i < playerNames.size(); i++) {
                if(playerNames.get(i).getText().length()!=0) {
                    playerStringNames.add(playerNames.get(i).getText().toString());
                    playerBooleanSexes[i] = playerSexes.get(i).isChecked();
                }
            }


            Intent intent = new Intent(this, GameActivity.class);
            intent.putStringArrayListExtra("playerNames", playerStringNames);
            intent.putExtra("playerSexes", playerBooleanSexes);
            startActivity(intent);
        }
    }

}
