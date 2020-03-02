package com.example.mplayer.structure.body.management.activities.devices;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mplayer.R;
import com.example.mplayer.entities.Device;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DeviceSelectActivity extends AppCompatActivity {

    private final String TAG = "DeviceSelectFragment";

    private Spinner devicesSpinner;

    private AtomicReference<String> userId;
    private List<Device> devices;
    private List<String> devicesId;

    private SharedResources resources;
    private FirebaseHandler firebaseHandler;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_select);
        Log.d(TAG, LogMessages.ACTIVITY_START.label);

        devicesSpinner = findViewById(R.id.deviceSelectSpinner);

        userId = new AtomicReference<>();
        devices = Collections.synchronizedList(new ArrayList<>());
        devicesId = new ArrayList<>();

        resources = SharedResources.getInstance();
        firebaseHandler = FirebaseHandler.getInstance();

        new BackgroundTask(this).execute();

        while(devices.isEmpty()) {
            Log.d(TAG, "Waiting for device list...");
        }

        devices.forEach(device -> devicesId.add(device.getId()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, devicesId);
        devicesSpinner.setAdapter(adapter);
    }

    public void deviceSelect(View view) {
        if(devicesSpinner.getSelectedItem() != null) {
            resources.setDeviceId(String.valueOf(devicesSpinner.getSelectedItemId()));
            Log.d(TAG, LogMessages.CHANGE_HOME.label);
            startActivity(new Intent(getBaseContext(), DeviceHomeActivity.class));
        } else {
            Log.e(TAG, "Device not selected");
            Toast.makeText(getBaseContext(), "Please select a device!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deviceSelectBack(View view) {
        startActivity(new Intent(getBaseContext(), DeviceHomeActivity.class));
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceSelectActivity> weakReference;

        BackgroundTask(DeviceSelectActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DeviceSelectActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.userId.set(activity.resources.getUserId());
            activity.firebaseHandler.getUserDevices(activity.userId.get(), activity.devices);

            return null;
        }
    }
}
