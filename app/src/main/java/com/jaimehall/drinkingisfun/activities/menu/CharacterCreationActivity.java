package com.jaimehall.drinkingisfun.activities.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaimehall.drinkingisfun.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class CharacterCreationActivity extends Activity {

    private ImageButton playerIcon;
    private EditText playerNameEditText;
    private CheckBox playerSexCheckBox;
    private static final int PICK_IMAGE = 100;
    private static final int CROP_IMAGE = 324;
    private Bitmap playerIconImage;
    private File characterDirectory;
    private File characterTextPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_creation);

        playerIcon = findViewById(R.id.playerIconImageButton);
        playerNameEditText = findViewById(R.id.playerNameEditText);
        playerSexCheckBox = findViewById(R.id.characterSexCheckBox);
        playerIconImage = null;

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        characterDirectory = cw.getDir("characters", Context.MODE_PRIVATE);

        if(!characterDirectory.exists()){
            characterDirectory.mkdir();
        }

        characterTextPath = new File(characterDirectory,"playerInformation");
        if(!characterTextPath.exists()){
            try {
                characterTextPath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }






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
        String imageName = "icon_"+System.currentTimeMillis()+".png";
        String playerName = playerNameEditText.getText().toString();
        boolean isFemale = playerSexCheckBox.isChecked();

        File myImagePath = new File(characterDirectory,imageName);
        if(!myImagePath.exists()){
            try {
                myImagePath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter nameWriter;
        FileOutputStream outputStreamImage;

        try{
            outputStreamImage = new FileOutputStream(myImagePath);
            playerIconImage.compress(Bitmap.CompressFormat.PNG,100,outputStreamImage);
            outputStreamImage.close();

            String textToWrite = (":"+playerName+":"+imageName+":"+isFemale);

            nameWriter = new BufferedWriter(new FileWriter(characterTextPath,true));
            nameWriter.newLine();
            nameWriter.write(textToWrite);

            nameWriter.close();


        } catch(Exception e){
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }

        Intent intent = new Intent(this,CharacterMenuActivity.class);
        startActivity(intent);
    }


}
