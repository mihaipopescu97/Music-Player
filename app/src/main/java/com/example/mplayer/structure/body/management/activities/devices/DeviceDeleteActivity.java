package com.example.mplayer.structure.body.management.activities.devices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mplayer.R;
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

    private Spinner devicesSpinner;
    private ArrayAdapter<String> adapter;
    private AtomicReference<String> userId;
    private List<String> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_delete);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        devicesSpinner = findViewById(R.id.deviceDeleteSpinner);

        userId = new AtomicReference<>();
        devices = Collections.synchronizedList(new ArrayList<>());

        firebaseHandler = FirebaseHandler.getInstance();
        SharedResources resources = SharedResources.getInstance();

        adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, devices);
        devicesSpinner.setAdapter(adapter);

        userId.set(resources.getUserId());
        new BackgroundTasks(this).execute();


    }

    public void deleteDevice(View view) {
        if(devicesSpinner.getSelectedItem() != null) {
            Log.d(TAG, "Deleting device:" + devicesSpinner.getSelectedItem().toString());
//            firebaseHandler.deleteDevice(String.valueOf(devicesSpinner.getSelectedItemId()), devices, adapter);
            new DeleteTask(this).execute(String.valueOf(devicesSpinner.getSelectedItemId()));
        } else {
            Log.e(TAG, "Device not selected");
            Toast.makeText(getBaseContext(), "Please select a device!", Toast.LENGTH_SHORT).show();
        }
    }

    public void doneDeleteDevice(View view) {
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

            activity.firebaseHandler.getUserDevices(activity.userId.get(), activity.devices, activity.adapter);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<String, Void, Void> {
        WeakReference<DeviceDeleteActivity> weakReference;

        DeleteTask(DeviceDeleteActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(String... strings) {
            DeviceDeleteActivity activity = weakReference.get();

            activity.firebaseHandler.deleteDevice(strings[0], activity.devices, activity.adapter);
            return null;
        }
    }
}
