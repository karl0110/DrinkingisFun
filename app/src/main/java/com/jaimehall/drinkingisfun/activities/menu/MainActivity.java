package com.jaimehall.drinkingisfun.activities.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.helpers.CharacterIO;

public class MainActivity extends Activity {

    private CharacterIO characterIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        characterIO = new CharacterIO(getApplicationContext());
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
