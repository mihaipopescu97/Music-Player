package com.example.mplayer.structure.body.management.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.devices.DeviceAddActivity;
import com.example.mplayer.structure.body.management.activities.playlists.RoomSelectActivity;
import com.example.mplayer.structure.body.management.activities.setups.SetupAddActivity;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

//REMOVE ADD DEVICE AFTER
public class NewBuildActivity extends AppCompatActivity {

    private SharedResources resources;
    private Button setupAddBtn;
    private Button playlistAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_build);

        String TAG = "NewBuildActivity";
        Log.i(TAG, LogMessages.ACTIVITY_START.label);
        resources = SharedResources.getInstance();
        setupAddBtn = findViewById(R.id.addNewSetupBtn);
        playlistAddBtn = findViewById(R.id.addNewPlaylistBtn);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(resources.getDeviceId() == null) {
            setupAddBtn.setVisibility(View.GONE);
            playlistAddBtn.setVisibility(View.GONE);
        } else if (resources.getSetupId() == null) {
            playlistAddBtn.setVisibility(View.GONE);
        }
    }

    public void addNewDevice(View view) {
        Intent intent = new Intent(getBaseContext(), DeviceAddActivity.class);
        intent.putExtra("prevActivity", this.getClass());
        startActivity(intent);
    }

    public void addNewSetup(View view) {
        Intent intent = new Intent(getBaseContext(), SetupAddActivity.class);
        intent.putExtra("prevActivity", this.getClass());
        startActivity(intent);
    }

    public void addNewPlaylist(View view) {
        Intent intent = new Intent(getBaseContext(), RoomSelectActivity.class);
        intent.putExtra("prevActivity",this.getClass());
        startActivity(intent);
    }

    public void backNewBuild(View view) {
        startActivity(new Intent(getBaseContext(), BaseActivity.class));
    }
}
