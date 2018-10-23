package com.jaimehall.drinkingisfun.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.helpers.CharacterIO;


public class CharacterMenuActivity extends Activity {


    private LinearLayout linearLayout;

    private CharacterIO characterIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_menu);
        linearLayout = findViewById(R.id.characterMenuLinearLayout);

        characterIO = (CharacterIO) getIntent().getSerializableExtra("characterIO");

        refreshUI();

    }

    public void startCharacterCreation(View view){
        Intent intent = new Intent(this,CharacterCreationActivity.class);
        intent.putExtra("characterIO",characterIO);
        startActivity(intent);
    }

    public void deleteAllCharacters(View view){

        characterIO.deleteAllCharacters();
    }

    public void onResume(){
        super.onResume();
        refreshUI();
    }

    private void refreshUI(){

        linearLayout.removeAllViews();

        String[] playerNames = characterIO.getCharacterNames();

        LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        for(int i = 0;i<playerNames.length;i++){
            final int index =i;
            Button button = new Button(this);
            button.setText(playerNames[i]);
            button.setLayoutParams(buttonLayout);
            button.setTextSize(30f);
            button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startCharacterEditing(index);
                    }
                });
            linearLayout.addView(button);

            }

    }

    public void startCharacterEditing(int index){
        Intent intent = new Intent(this,CharacterEditingActivity.class);
        intent.putExtra("characterIO",characterIO);
        intent.putExtra("characterIcon",characterIO.getCharacterIcons()[index]);
        intent.putExtra("characterName",characterIO.getCharacterNames()[index]);
        intent.putExtra("characterSex",characterIO.getCharacterSexes()[index]);
        this.startActivity(intent);
    }


}
