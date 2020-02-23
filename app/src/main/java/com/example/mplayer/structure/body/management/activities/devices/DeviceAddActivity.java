package com.example.mplayer.structure.body.management.activities.devices;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mplayer.R;
import com.example.mplayer.entities.Device;
import com.example.mplayer.structure.body.management.activities.NewBuildActivity;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

//TODO test then freeze
public class DeviceAddActivity extends AppCompatActivity {

    private final String TAG = "DeviceAddActivity";

    private FirebaseHandler firebaseHandler;

    private EditText deviceIdEt;

    private SharedResources resources;
    private Thread thread;

    private AtomicReference<Class> prevActivity;
    private AtomicReference<String> deviceId;
    private List<Device> devices;
    private List<Device> userDevices;

    private Boolean isEmpty;
    private Boolean isAvailable;
    private Boolean isDuplicate;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_add);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        firebaseHandler = FirebaseHandler.getInstance();
        deviceIdEt = findViewById(R.id.deviceAddId);
        resources = SharedResources.getInstance();
        prevActivity = new AtomicReference<>();
        deviceId = new AtomicReference<>();
        devices = Collections.synchronizedList(new ArrayList<>());
        userDevices = Collections.synchronizedList(new ArrayList<>());

        //Update the device id once per sec with the user input
        thread = new Thread(() -> {
            while (true) {
                deviceId.set(deviceIdEt.getText().toString());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        new BackgroundTasks(this).execute();
        new CheckTask(this).execute();
    }

    public void addDevice(View view) {
        if(isEmpty) {
            Log.e(TAG, "Empty device id");
            Toast.makeText(getBaseContext(), "Please enter a device id!", Toast.LENGTH_SHORT).show();
        } else if (!isAvailable) {
            Log.e(TAG, "Device not registered");
            Toast.makeText(getBaseContext(), "Serial incorrect!", Toast.LENGTH_SHORT).show();
        } else if(isDuplicate) {
            Log.e(TAG, "Device already registered for your user!");
            Toast.makeText(getBaseContext(), "Device already registered for your user!", Toast.LENGTH_SHORT).show();
        } else {
            Device device = new Device(resources.getUserId());
            device.setId(deviceId.get());

            Log.d(TAG, "Adding device with id:" + device.getId());
            firebaseHandler.updateDevice(device.getId(), device);
            thread.interrupt();
            startActivity(new Intent(getBaseContext(), NewBuildActivity.class));
        }
    }

    public void backDeviceAddActivity(View view) {
        thread.interrupt();
        Class<?> cls = prevActivity.get();
        Intent intent = new Intent(DeviceAddActivity.this, cls);
        startActivity(intent);
    }

    private static class BackgroundTasks extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceAddActivity> weakReference;

        BackgroundTasks(DeviceAddActivity fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DeviceAddActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_START.label);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            DeviceAddActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);

            Intent intent = activity.getIntent();
            activity.prevActivity.set((Class) intent.getExtras().get("prevActivity"));
            activity.firebaseHandler.getUserDevices(activity.resources.getUserId(), activity.userDevices);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DeviceAddActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_END.label);
        }
    }

    private static class CheckTask extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceAddActivity> weakReference;

        CheckTask(DeviceAddActivity fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DeviceAddActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_START.label);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            DeviceAddActivity activity = weakReference.get();

            //noinspection InfiniteLoopStatement
            while(true) {
                activity.isEmpty = true;
                activity.isAvailable = false;
                activity.isDuplicate = false;

                if(!activity.deviceId.get().isEmpty()) {
                    activity.isEmpty = false;
                    activity.userDevices.forEach(userDevice -> {
                        if (userDevice.getId().equals(activity.deviceId.get())) {
                            activity.isDuplicate = true;
                        }
                    });

                    activity.devices.forEach(device -> {
                        if (device.getId().equals(activity.deviceId.get())) {
                            activity.isAvailable = true;
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    activity.isEmpty = true;
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DeviceAddActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_END.label);
        }
    }
}
