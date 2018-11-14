package com.jaimehall.drinkingisfun.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.jaimehall.drinkingisfun.R;

import java.util.ArrayList;
import java.util.Random;

public class SexButton extends ImageButton implements View.OnClickListener {

    private ArrayList<Bitmap> icons = new ArrayList<>();

    private int currentBitmap;

    public SexButton(Context context) {
        super(context);
        init();
    }

    public SexButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SexButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setOnClickListener(this);

        Resources resources = getContext().getResources();

        icons.add(BitmapFactory.decodeResource(resources, R.drawable.male));
        icons.add(BitmapFactory.decodeResource(resources, R.drawable.female));
        icons.add(BitmapFactory.decodeResource(resources, R.drawable.unknown));

        setImageBitmap(getRandomIcon());

    }

    private Bitmap getRandomIcon(){
        Random generator = new Random((long)(Math.random()*456345));
        int randomIndex = generator.nextInt(icons.size());
        return icons.get(randomIndex);
    }

    private Bitmap getNextIcon(){
        currentBitmap++;
        if(currentBitmap==icons.size()){
            currentBitmap=0;
        }
        return icons.get(currentBitmap);
    }


    @Override
    public void onClick(View view) {
        setImageBitmap(getNextIcon());
    }

    public String getSex(){
        String returnString = "";
        switch(currentBitmap){
            case 0:
                returnString = "male";
                break;
            case 1:
                returnString = "female";
                break;
            case 2:
                returnString = "unknown";
                break;
        }
        return returnString;

    }

    public void setSex(String sex){
        switch(sex){
            case "male":
                currentBitmap = 0;
                break;
            case "female":
                currentBitmap=1;
                break;
            case "unknown":
                currentBitmap = 2;
                break;
        }
        setImageBitmap(icons.get(currentBitmap));
    }
}
