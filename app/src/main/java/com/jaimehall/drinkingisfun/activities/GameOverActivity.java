package com.jaimehall.drinkingisfun.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaimehall.drinkingisfun.R;
import com.jaimehall.drinkingisfun.activities.menu.MainActivity;

public class GameOverActivity extends Activity {

    private LinearLayout linearLayout;

    private String[] playerNames;
    private long[] playerScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();

        playerNames = intent.getStringArrayExtra("playerNames");
        playerScores = intent.getLongArrayExtra("playerScores");

        quickSort(playerScores,playerNames,0,playerScores.length-1);
        invert(playerScores);
        invert(playerNames);


        linearLayout = findViewById(R.id.scoreboardLinearLayout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams horizontalLinearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        for(int i = 0; i<playerScores.length;i++){

            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setLayoutParams(new WindowManager.LayoutParams());
            horizontalLayout.setLayoutParams(horizontalLinearLayoutParams);


            TextView nameTextView = new TextView(this);
            nameTextView.setLayoutParams(layoutParams);
            String displayNameText = (i+1)+".   "+playerNames[i];
            nameTextView.setText(displayNameText);
            nameTextView.setTextSize(30);
            nameTextView.setTextColor(Color.WHITE);

            horizontalLayout.addView(nameTextView);

            TextView scoreTextView = new TextView(this);
            scoreTextView.setLayoutParams(layoutParams);
            String displayScoreText = "    Score: "+playerScores[i];
            scoreTextView.setText(displayScoreText);
            scoreTextView.setTextSize(30);
            scoreTextView.setTextColor(Color.WHITE);

            horizontalLayout.addView(scoreTextView);

            linearLayout.addView(horizontalLayout);






        }



    }

    public void goToMainMenu(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void quickSort(long[] sortArray, String[] additionalArray,int start,int end){
        if (start < end) {
            int pIndex = partition(sortArray,additionalArray,start,end);
            quickSort(sortArray,additionalArray,start,pIndex-1);
            quickSort(sortArray,additionalArray,pIndex+1,end);

        }

    }

    private int partition(long[] sortArray, String[] additionalArray,int start,int end){
        int pivot = end;
        int pIndex = start;
        for(int i = start; i<end;i++){
            if(sortArray[i]<=sortArray[pivot]){
                swap(sortArray,additionalArray,i,pIndex);
                pIndex++;
            }
        }
        swap(sortArray,additionalArray,pIndex,pivot);
        return pIndex;
    }

    private void swap(long[] sortArray, String[] additionalArray,int index1,int index2){
        long backupLong = sortArray[index1];
        String backupString = additionalArray[index1];

        sortArray[index1] = sortArray[index2];
        additionalArray[index1] = additionalArray[index2];

        sortArray[index2] = backupLong;
        additionalArray[index2] = backupString;
    }

    private void invert(Object[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            Object temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }

    private void invert(long[] array){
        for (int i = 0; i < array.length / 2; i++) {
            long temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }

}
