package com.example.mplayer.structure.body.management.fragments.devices;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.settings.DeviceSettingsActivity;
import com.example.mplayer.entities.Device;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DeviceDeleteFragment extends Fragment {

    private final String TAG = "DeviceDeleteFragment";
    private FirebaseHandler firebaseHandler;

    private AtomicReference<String> userId;
    private List<Device> devices;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_delete, container, false);

        Log.i(TAG, LogMessages.FRAGMENT_START.label);

        firebaseHandler = FirebaseHandler.getInstance();

        final Spinner devicesSpinner = view.findViewById(R.id.deviceDeleteSpinner);
        final Button deleteDeviceBtn = view.findViewById(R.id.deviceDeleteBtn);
        final Button doneBtn = view.findViewById(R.id.deviceDeleteDoneBtn);

        userId = new AtomicReference<>();

        devices = Collections.synchronizedList(new ArrayList<>());

        final String finalUserId = userId.get();
        final Thread spinnerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Device> devices = firebaseHandler.getUserDevices(finalUserId);
                List<String> devicesId = new ArrayList<>();

                for(Device device : devices) {
                    devicesId.add(device.getId());
                }

                ArrayAdapter<String> adapter = null;
                if(getActivity() != null) {
                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, devicesId);
                } else {
                    Log.e(TAG, "Activity not started");
                }

                devicesSpinner.setAdapter(adapter);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        spinnerThread.start();

        deleteDeviceBtn.setOnClickListener(v -> {
            if(devicesSpinner.getSelectedItem() != null) {
                Log.d(TAG, "Deleting device:" + devicesSpinner.getSelectedItem().toString());
                firebaseHandler.deleteDevice(String.valueOf(devicesSpinner.getSelectedItemId()));
            } else {
                Log.e(TAG, "Device not selected");
                Toast.makeText(getActivity(), "Please select a device!", Toast.LENGTH_SHORT).show();
            }
        });

        doneBtn.setOnClickListener(v -> {
            spinnerThread.interrupt();
            if(getActivity() != null) {
                Log.d(TAG, "Changing to device home fragment");
                ((DeviceSettingsActivity)getActivity()).setViewPager(0);
            } else {
                Log.e(TAG, "Activity is null");
            }
        });
        return view;
    }

    //TODO make service for reading deleting etc for firebase
    private static class BackupTasks extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceDeleteFragment> weakReference;

        public BackupTasks(DeviceDeleteFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            DeviceDeleteFragment fragment = weakReference.get();
            Log.d(fragment.TAG, LogMessages.ASYNC_START.label);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DeviceDeleteFragment fragment = weakReference.get();

            Bundle bundle = fragment.getArguments();
            if (bundle != null) {
                fragment.userId.set(bundle.getString("userId"));
                //TODO FIX THIS SHIT
                fragment.firebaseHandler.getUserDevices(fragment.userId.get());
            } else {
                Log.e(fragment.TAG, LogMessages.USER_FETCH_ERROR.label);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class CheckTask extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceDeleteFragment> weakReference;

        public CheckTask(DeviceDeleteFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            DeviceDeleteFragment fragment = weakReference.get();
            Log.d(fragment.TAG, LogMessages.ASYNC_START.label);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            DeviceDeleteFragment fragment = weakReference.get();
            Log.d(fragment.TAG, LogMessages.ASYNC_START.label);
        }
    }
}
