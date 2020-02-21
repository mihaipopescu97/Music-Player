package com.example.mplayer.structure.body.management.fragments.devices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.settings.SettingsActivity;
import com.example.mplayer.utils.enums.LogMessages;

public class DeviceHomeActivity extends AppCompatActivity {

    private final String TAG = "DeviceHomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_home);

        Log.d(TAG, LogMessages.ACTIVITY_START.label);
    }

    public void homeAddDevice(View view) {
        startActivity(new Intent(getBaseContext(), DeviceAddActivity.class));
    }

    public void homeSelectDevice(View view) {
        startActivity(new Intent(getBaseContext(), DeviceSelectActivity.class));
    }

    public void homeDeleteDevice(View view) {
        startActivity(new Intent(getBaseContext(), DeviceAddActivity.class));
    }

    public void homeDeviceBack(View view) {
        startActivity(new Intent(getBaseContext(), SettingsActivity.class));
    }
}
