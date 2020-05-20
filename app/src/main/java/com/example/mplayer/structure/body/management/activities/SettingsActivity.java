package com.example.mplayer.structure.body.management.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.devices.DeviceHomeActivity;
import com.example.mplayer.structure.body.management.activities.playlists.PlaylistHomeActivity;
import com.example.mplayer.structure.body.management.activities.setups.SetupHomeActivity;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

//FROZEN
public class SettingsActivity extends AppCompatActivity {

    private SharedResources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String TAG = "SettingsActivity";
        Log.i(TAG, LogMessages.ACTIVITY_START.label);
        resources = SharedResources.getInstance();
    }

    public void manageDevices(View view) {
        startActivity(new Intent(getBaseContext(), DeviceHomeActivity.class));
    }

    public void manageSetups(View view) {
        startActivity(new Intent(getBaseContext(), SetupHomeActivity.class));
    }

    public void managePlaylist(View view) {
        if(resources.getSetupId() != null) {
            startActivity(new Intent(getBaseContext(), PlaylistHomeActivity.class));
        } else {
            Toast.makeText(getBaseContext(), "Select a setup first!", Toast.LENGTH_SHORT).show();
        }

    }

    public void backSettings(View view) {
        startActivity(new Intent(getBaseContext(), BaseActivity.class));
    }
}
