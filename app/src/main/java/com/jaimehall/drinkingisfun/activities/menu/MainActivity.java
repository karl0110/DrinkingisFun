package com.jaimehall.drinkingisfun.activities.menu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.helpers.CharacterIO;

public class MainActivity extends Activity {

    private CharacterIO characterIO;

    private static final int REQUEST_PERMISSION_CAMERA = 122;
    private static final int REQUEST_PERMISSION_EXTERNAL = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        characterIO = new CharacterIO(getApplicationContext());
        checkPermissions();
    }

    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISSION_CAMERA);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_EXTERNAL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
    }

    public void startPlayerSelect(View view){
        Intent intent = new Intent(this,PlayerSelectActivity.class);
        intent.putExtra("characterIO",characterIO);
        startActivity(intent);
    }

    public void startSettings(View view){
        Intent intent = new Intent(this,SettingsActivity.class);
        intent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName() );
        intent.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
        startActivity(intent);
    }

    public void startCharacterMenu(View view){
        Intent intent = new Intent(this,CharacterMenuActivity.class);
        intent.putExtra("characterIO",characterIO);
        startActivity(intent);
    }


}
