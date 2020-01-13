package com.example.mplayer.activities.body.management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.mplayer.R;
import com.example.mplayer.adapters.fragments.DeviceSectionAdapter;

public class ManageDeviceActivity extends AppCompatActivity {

    private static final String TAG = "ManageDeviceActivity";

    private DeviceSectionAdapter deviceSectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_device);

        Log.d(TAG, "Manage device activity started");

        deviceSectionAdapter = new DeviceSectionAdapter(getSupportFragmentManager());
    }
}
