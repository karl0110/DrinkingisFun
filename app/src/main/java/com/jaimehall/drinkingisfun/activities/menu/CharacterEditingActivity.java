package com.jaimehall.drinkingisfun.activities.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.helpers.BitmapLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class CharacterEditingActivity extends Activity {

    private ImageButton playerIcon;
    private EditText playerNameEditText;
    private CheckBox playerSexCheckBox;
    private static final int PICK_IMAGE = 100;
    private static final int CROP_IMAGE = 324;
    private Bitmap playerIconImage;
    private File characterDirectory;
    private File characterTextPath;
    private File characterIcon;
    private String characterName;

    private String imageName;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_editing);

        playerIcon = findViewById(R.id.imageButtonEditCharacterIcon);
        playerNameEditText = findViewById(R.id.editTextEditCharacterName);
        playerSexCheckBox = findViewById(R.id.checkBoxEditCharacterSex);



        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        characterDirectory = cw.getDir("characters", Context.MODE_PRIVATE);
        characterTextPath = new File(characterDirectory,"playerInformation");

        final Intent intent = getIntent();

        playerIcon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = playerIcon.getWidth();
                if (width > 0) {
                    playerIcon.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    imageName = intent.getStringExtra("characterIcon");
                    characterIcon = new File(characterDirectory,imageName);
                    playerIcon.setImageBitmap(BitmapFactory.decodeFile(characterIcon.getPath()));
                    playerIcon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,playerIcon.getWidth(),1.0f));

                }

            }
        });


        characterName = intent.getStringExtra("characterName");
        playerNameEditText.setText(characterName);


        playerSexCheckBox.setChecked(intent.getBooleanExtra("characterSex",false));


        playerIconImage = null;




    }



    public void selectPlayerIcon(View view){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Uri imageUri = data.getData();
                cropImage(imageUri);

            }
            if (requestCode == CROP_IMAGE) {

                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    playerIconImage = bundle.getParcelable("data");
                    playerIcon.setImageBitmap(playerIconImage);
                    playerIcon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,playerIcon.getWidth(),1.0f));

                }
                else{
                    Toast.makeText(this,"Fail",Toast.LENGTH_SHORT);
                }


            }


        }

    }

    private void cropImage(Uri uri){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop","true");
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 300);
            cropIntent.putExtra("aspectX",1);
            cropIntent.putExtra("aspectY",1);
            cropIntent.putExtra("scaleUpIfNeeded",true);
            cropIntent.putExtra("scaleDownIfNeeded",true);
            cropIntent.putExtra("return-data",true);

            startActivityForResult(cropIntent, CROP_IMAGE);
        }
        catch(ActivityNotFoundException e){
            e.printStackTrace();
        }
    }

    public void save(View view){

        if(playerIconImage != null) {
            characterIcon.delete();
        }

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
               if(tokens.nextToken().matches(characterName)){
                    indexLineToBeDeleted = i;
               }
            }

        }


        if(playerIconImage != null) {
            imageName = "icon_" + System.currentTimeMillis() + ".png";
        }

        String playerName = playerNameEditText.getText().toString();
        boolean isFemale = playerSexCheckBox.isChecked();

        File myImagePath = new File(characterDirectory,imageName);



        BufferedWriter nameWriter;
        FileOutputStream outputStreamImage;

        try{
            if(playerIconImage != null) {
                outputStreamImage = new FileOutputStream(myImagePath);
                playerIconImage.compress(Bitmap.CompressFormat.PNG, 100, outputStreamImage);
                outputStreamImage.close();
            }

            characterTextPath.delete();
            characterTextPath.createNewFile();
            nameWriter = new BufferedWriter(new FileWriter(characterTextPath,true));

            String textToWrite = (":"+playerName+":"+imageName+":"+isFemale);

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

        finish();
    }

    public void delete(View view){
        characterIcon.delete();

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

        Intent intent = new Intent(this,CharacterMenuActivity.class);
        finish();
        startActivity(intent);

    }

}
