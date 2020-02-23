package com.example.mplayer.structure.body.management.activities.setups;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.settings.SettingsActivity;
import com.example.mplayer.utils.enums.LogMessages;

public class SetupHomeActivity extends AppCompatActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "SetupHomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_home);

        Log.d(TAG, LogMessages.ACTIVITY_START.label);
    }

    public void homeAddSetup(View view) {
        startActivity(new Intent(getBaseContext(), SetupAddActivity.class));
    }

    public void homeDeleteSetup(View view) {
        startActivity(new Intent(getBaseContext(), SetupDeleteActivity.class));
    }

    public void homeSelectSetup(View view) {
        startActivity(new Intent(getBaseContext(), SetupSelectActivity.class));
    }

    public void backHomeSetup(View view) {
        startActivity(new Intent(getBaseContext(), SettingsActivity.class));
    }
}
