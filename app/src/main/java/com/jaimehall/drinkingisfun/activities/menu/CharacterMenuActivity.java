package com.jaimehall.drinkingisfun.activities.menu;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.helpers.CharacterIO;


public class CharacterMenuActivity extends Activity {


    private LinearLayout linearLayout;

    private CharacterIO characterIO;

    private LinearLayout.LayoutParams buttonLayoutParams;
    private LinearLayout.LayoutParams horizontalLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_menu);
        linearLayout = findViewById(R.id.characterMenuLinearLayout);

        characterIO = (CharacterIO) getIntent().getSerializableExtra("characterIO");

         buttonLayoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
         horizontalLayoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        refreshUI();

    }

    public void startCharacterCreation(View view){
        Intent intent = new Intent(this,CharacterCreationActivity.class);
        intent.putExtra("characterIO",characterIO);
        startActivity(intent);
    }

    public void deleteAllCharacters(View view){

        characterIO.deleteAllCharacters();
        refreshUI();
    }

    public void onResume(){
        super.onResume();
        refreshUI();
    }

    private void refreshUI(){

        linearLayout.removeAllViews();

        String[] characterNames = characterIO.getCharacterNames();


        for(int i = 0; i<characterNames.length;i++){
            Button tempButton = new Button(getApplicationContext());
            tempButton.setLayoutParams(buttonLayoutParams);
            tempButton.setText(characterNames[i]);
            tempButton.setEms(10);


            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setLayoutParams(new WindowManager.LayoutParams());
            horizontalLayout.setLayoutParams(horizontalLayoutParams);


            horizontalLayout.addView(tempButton);


            linearLayout.addView(horizontalLayout);

            final int index = i;

            tempButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startCharacterEditing(index);
                }
            });



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
