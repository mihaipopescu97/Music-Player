package com.example.mplayer.structure.body.management.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.setups.SetupSelectActivity;
import com.example.mplayer.structure.player.PlayerActivity;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;

//TODO needs testing then can freeze
//Back - OK
public class BaseActivity extends AppCompatActivity {

    private final String TAG = "BaseActivity";

    private SharedResources resources;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        resources = SharedResources.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        resources.resetPlayType();
    }

    public void createNewSetup(View view) {
        startActivity( new Intent(getBaseContext(), NewBuildActivity.class));
    }

    public void useSetup(View view) {
        startActivity(new Intent(getBaseContext(), SetupSelectActivity.class));
    }

    public void manageSettings(View view) {
        startActivity(new Intent(getBaseContext(), SettingsActivity.class));
    }

    public void play(View view) {
        if(resources.getSetupId() != null && resources.getSetupId().isEmpty()) {
            startActivity(new Intent(BaseActivity.this, PlayerActivity.class));
        }
    }

    public void backBase(View view) {
        startActivity( new Intent(BaseActivity.this, SelectActivity.class));
    }

}
