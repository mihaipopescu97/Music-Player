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

public class DeviceDeleteActivity extends AppCompatActivity {

    private final String TAG = "DeviceDeleteActivity";

    private FirebaseHandler firebaseHandler;
    private SharedResources resources;

    private Spinner devicesSpinner;
    private AtomicReference<ArrayAdapter<String>> adapter;
    private AtomicReference<String> userId;
    private List<Device> devices;

    private UpdateSpinner updateSpinner;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_delete);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        devicesSpinner = findViewById(R.id.deviceDeleteSpinner);

        userId = new AtomicReference<>();
        adapter = new AtomicReference<>();

        devices = Collections.synchronizedList(new ArrayList<>());

        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();

        new BackgroundTasks(this).execute();

        updateSpinner = new UpdateSpinner(this);
        updateSpinner.execute();

        thread = new Thread(() -> {
            devicesSpinner.setAdapter(adapter.get());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void deleteDevice(View view) {
        if(devicesSpinner.getSelectedItem() != null) {
            Log.d(TAG, "Deleting device:" + devicesSpinner.getSelectedItem().toString());
            firebaseHandler.deleteDevice(String.valueOf(devicesSpinner.getSelectedItemId()));
        } else {
            Log.e(TAG, "Device not selected");
            Toast.makeText(getBaseContext(), "Please select a device!", Toast.LENGTH_SHORT).show();
        }
    }

    public void doneDeleteDevice(View view) {
        thread.interrupt();
        updateSpinner.cancel(true);
        startActivity(new Intent(getBaseContext(), DeviceHomeActivity.class));
    }

    private static class BackgroundTasks extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceDeleteActivity> weakReference;

        BackgroundTasks(DeviceDeleteActivity fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DeviceDeleteActivity activity = weakReference.get();

            Log.i(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.userId.set(activity.resources.getUserId());
            activity.firebaseHandler.getUserDevices(activity.userId.get(), activity.devices);
            return null;
        }
    }

    private static class UpdateSpinner extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceDeleteActivity> weakReference;

        UpdateSpinner(DeviceDeleteActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            DeviceDeleteActivity activity = weakReference.get();
            Log.i(activity.TAG, LogMessages.ASYNC_WORKING.label);

            List<String> devicesId = new ArrayList<>();
            //noinspection InfiniteLoopStatement
            while(true) {
                devicesId.clear();
                activity.devices.forEach(device -> devicesId.add(device.getId()));
                activity.adapter.set(new ArrayAdapter<>(activity.getBaseContext(), android.R.layout.simple_spinner_dropdown_item, devicesId));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
