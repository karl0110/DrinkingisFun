package com.jaimehall.drinkingisfun.activities.menu;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.activities.GameActivity;
import com.jaimehall.drinkingisfun.helpers.CharacterIO;
import com.jaimehall.drinkingisfun.helpers.SexButton;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class CharacterCreationActivity extends Activity {

    private ImageButton playerIcon;
    private EditText playerNameEditText;
    private SexButton playerSexButton;
    private static final int PICK_IMAGE = 100;
    private static final int CROP_IMAGE = 324;
    private static final int REQUEST_CAMERA =243;
    private Bitmap playerIconImage;

    private CharacterIO characterIO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_creation);

        characterIO = (CharacterIO) getIntent().getSerializableExtra("characterIO");

        playerIcon = findViewById(R.id.imageButtonCreateCharacterName);
        playerNameEditText = findViewById(R.id.editTextCreateCharacterName);
        playerSexButton = findViewById(R.id.sexButtonCreateCharacterSex);
        playerIconImage = null;



    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    private void openCamera(){
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {

                 Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 startActivityForResult(camera,REQUEST_CAMERA );

            } else {
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public void selectPlayerIcon(View view){

                final CharSequence[] options = {"Gallerie", "Kamera"};
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,android.R.style.Theme_DeviceDefault));
                builder.setTitle("Bild für Charakter Auswählen");



                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Gallerie")) {
                            dialog.dismiss();
                            openGallery();
                        } else if (options[item].equals("Kamera")) {
                            dialog.dismiss();
                            openCamera();
                        }
                    }
                });
                builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE ) {
                Uri imageUri = data.getData();
                cropImage(imageUri);

            }
            else if (requestCode == CROP_IMAGE) {

                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    playerIconImage = bundle.getParcelable("data");
                    playerIcon.setImageBitmap(playerIconImage);
                    playerIcon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,playerIcon.getWidth(),1.0f));

                }

            }
            else if(requestCode == REQUEST_CAMERA){
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Uri uri = getImageURI(this,(Bitmap)bundle.get("data"));
                    cropImage(uri);
                }
            }


        }

    }

    public Uri getImageURI(Context context, Bitmap bitmap) {

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
                return Uri.parse(path);

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
        String playerSex = playerSexButton.getSex();

        characterIO.save(imageName,playerIconImage,playerName,playerSex);

        Intent intent = new Intent(this,CharacterMenuActivity.class);
        finish();
        startActivity(intent);
    }


}
