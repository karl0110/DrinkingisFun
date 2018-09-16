package com.jaimehall.drinkingisfun.activities.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.activities.GameActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void startPlayerSelect(View view){
        Intent intent = new Intent(this,PlayerSelectActivity.class);
        startActivity(intent);
    }
}
