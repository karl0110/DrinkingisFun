package com.jaimehall.drinkingisfun.activities.menu;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jaimehall.drinkingisfun.R;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class CharacterSelectActivity extends Activity {

    private String[] characterNames;
    private String[] characterBitmapPaths;
    private boolean[] characterSexes;
    private File characterDirectory;
    private File characterTextPath;

    private CheckBox[] characterSelectedCheckBox;

    private LinearLayout linearLayout;

    private LinearLayout.LayoutParams buttonLayoutParams;
    private LinearLayout.LayoutParams checkBoxLayoutParams;
    private LinearLayout.LayoutParams horizontalLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        characterDirectory = cw.getDir("characters", Context.MODE_PRIVATE);
        characterTextPath = new File(characterDirectory,"playerInformation");

        refreshPlayerList();

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
        System.out.println("finishing up");

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPlayerList();
        refreshUi();
    }

    private void refreshUi(){
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
        intent.putExtra("characterIcon",characterBitmapPaths[index]);
        intent.putExtra("characterName",characterNames[index]);
        intent.putExtra("characterSex",characterSexes[index]);
        this.startActivity(intent);
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

            for(int i = 0; i<untokenedPlayerNames.size() ; i++) {
                StringTokenizer tokens = new StringTokenizer(untokenedPlayerNames.get(i), ":");
                if(tokens.hasMoreTokens()){
                    characterNames[i] = tokens.nextToken();

                    characterBitmapPaths[i] = tokens.nextToken();


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

}
