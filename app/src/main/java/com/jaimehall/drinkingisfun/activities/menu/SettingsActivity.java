package com.jaimehall.drinkingisfun.activities.menu;

import android.os.Bundle;
import android.preference.*;
import android.support.annotation.Nullable;

import com.jaimehall.drinkingisfun.R;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return false;
    }
}
