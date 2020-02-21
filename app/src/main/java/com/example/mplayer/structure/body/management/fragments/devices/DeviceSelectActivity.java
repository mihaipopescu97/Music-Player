package com.example.mplayer.structure.body.management.fragments.devices;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mplayer.R;
import com.example.mplayer.entities.Device;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DeviceSelectActivity extends AppCompatActivity {

    private final String TAG = "DeviceSelectFragment";
    private FirebaseHandler firebaseHandler;

    private AtomicReference<String> userId;
    private List<Device> devices;
    private List<String> devicesId;

    private SharedResources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_select);

        Log.d(TAG, LogMessages.ACTIVITY_START.label);

        firebaseHandler = FirebaseHandler.getInstance();

        final Spinner devicesSpinner = findViewById(R.id.deviceSelectSpinner);

        userId = new AtomicReference<>();
        devices = Collections.synchronizedList(new ArrayList<>());
        devicesId = Collections.synchronizedList(new ArrayList<>());

        resources = SharedResources.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, devicesId);
        devicesSpinner.setAdapter(adapter);
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceSelectActivity> weakReference;

        BackgroundTask(DeviceSelectActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            DeviceSelectActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.userId.set(activity.resources.getUserId());

            activity.firebaseHandler.getUserDevices(activity.userId.get(), activity.devices);
            activity.devices.forEach(device -> activity.devicesId.add(device.getId()));

            return null;
        }
    }
}
