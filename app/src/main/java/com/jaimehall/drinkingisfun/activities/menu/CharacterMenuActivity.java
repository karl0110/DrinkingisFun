package com.jaimehall.drinkingisfun.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.jaimehall.drinkingisfun.R;

public class CharacterMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_menu);
    }

    public void startCharacterCreation(View view){
        Intent intent = new Intent(this,CharacterCreationActivity.class);
        startActivity(intent);
    }

}
