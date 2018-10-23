package com.jaimehall.drinkingisfun.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.helpers.CharacterIO;


public class CharacterSelectActivity extends Activity {


    private CheckBox[] characterSelectedCheckBox;

    private LinearLayout linearLayout;

    private LinearLayout.LayoutParams buttonLayoutParams;
    private LinearLayout.LayoutParams checkBoxLayoutParams;
    private LinearLayout.LayoutParams horizontalLayoutParams;

    private CharacterIO characterIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);

        characterIO = (CharacterIO) getIntent().getSerializableExtra("characterIO");


        linearLayout = findViewById(R.id.linearLayoutCharacterSelect);

         checkBoxLayoutParams = new LinearLayout.LayoutParams(1,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
         buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT-checkBoxLayoutParams.width,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);

         horizontalLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

         refreshUi();

    }

    public void finish(View view){
        Intent data = new Intent();

        boolean[] charactersSelected = new boolean[characterSelectedCheckBox.length];
        for(int  i= 0; i<characterSelectedCheckBox.length;i++){
            charactersSelected[i] = characterSelectedCheckBox[i].isChecked();
        }

        data.putExtra("charactersSelected",charactersSelected);
        setResult(RESULT_OK,data);

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUi();
    }

    private void refreshUi(){
        String[] characterNames = characterIO.getCharacterNames();

        linearLayout.removeAllViews();
        characterSelectedCheckBox = new CheckBox[characterNames.length];

        for(int i = 0; i<characterNames.length;i++){
            Button tempButton = new Button(getApplicationContext());
            tempButton.setLayoutParams(buttonLayoutParams);
            tempButton.setText(characterNames[i]);
            tempButton.setEms(10);

            characterSelectedCheckBox[i] = new CheckBox(getApplicationContext());
            characterSelectedCheckBox[i].setLayoutParams(checkBoxLayoutParams);

            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setLayoutParams(new WindowManager.LayoutParams());
            horizontalLayout.setLayoutParams(horizontalLayoutParams);

            horizontalLayout.addView(characterSelectedCheckBox[i]);
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
