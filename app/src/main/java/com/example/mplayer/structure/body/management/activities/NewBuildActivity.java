package com.example.mplayer.structure.body.management.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.mplayer.R;
import com.example.mplayer.entities.Device;
import com.example.mplayer.structure.body.management.activities.devices.DeviceAddActivity;
import com.example.mplayer.structure.body.management.activities.playlists.RoomSelectActivity;
import com.example.mplayer.structure.body.management.activities.setups.SetupAddActivity;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.enums.LogMessages;

//REMOVE ADD DEVICE AFTER
public class NewBuildActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_build);

        String TAG = "NewBuildActivity";
        Log.i(TAG, LogMessages.ACTIVITY_START.label);
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

    public void addDeviceKms(View view) {
        FirebaseHandler.getInstance().addDevice(new Device("0"));
    }
}
