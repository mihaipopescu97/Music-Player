package com.example.mplayer.structure.body.management.fragments.devices;

import android.os.AsyncTask;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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

    private Spinner devicesSpinner;
    private ArrayAdapter<String> adapter;
    private AtomicReference<String> userId;
    private List<Device> devices;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_delete, container, false);

        Log.i(TAG, LogMessages.FRAGMENT_START.label);

        firebaseHandler = FirebaseHandler.getInstance();

        devicesSpinner = view.findViewById(R.id.deviceDeleteSpinner);
        final Button deleteDeviceBtn = view.findViewById(R.id.deviceDeleteBtn);
        final Button doneBtn = view.findViewById(R.id.deviceDeleteDoneBtn);

        userId = new AtomicReference<>();
        adapter = null;

        devices = Collections.synchronizedList(new ArrayList<>());

        new BackgroundTasks(this).execute();
        new CheckTask(this).execute();

        Thread thread = new Thread(() -> {
            devicesSpinner.setAdapter(adapter);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

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
            thread.interrupt();
            if(getActivity() != null) {
                Log.d(TAG, LogMessages.CHANGE_HOME.label);
                ((DeviceSettingsActivity)getActivity()).setViewPager(0);
            } else {
                Log.e(TAG, LogMessages.ACTIVITY_NULL.label);
            }
        });
        return view;
    }

    //TODO make service for reading deleting etc for firebase
    private static class BackgroundTasks extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceDeleteFragment> weakReference;

        BackgroundTasks(DeviceDeleteFragment fragment) {
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

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class CheckTask extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceDeleteFragment> weakReference;

        CheckTask(DeviceDeleteFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            DeviceDeleteFragment fragment = weakReference.get();
            Log.d(fragment.TAG, LogMessages.ASYNC_START.label);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            DeviceDeleteFragment fragment = weakReference.get();

            Log.d(fragment.TAG, LogMessages.ASYNC_WORKING.label);
            //TODO ALSO FIX DIS
            //noinspection InfiniteLoopStatement
            while (true) {
                List<String> devicesId = new ArrayList<>();

                fragment.devices.forEach(device -> devicesId.add(device.getId()));

                FragmentActivity fragmentActivity = fragment.getActivity();

                if (fragmentActivity != null) {
                    fragment.adapter = new ArrayAdapter<>(fragmentActivity, android.R.layout.simple_spinner_dropdown_item, devicesId);
                } else {
                    //TODO make log message
                    Log.e(fragment.TAG, "Activity not started");
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            DeviceDeleteFragment fragment = weakReference.get();
            Log.d(fragment.TAG, LogMessages.ASYNC_START.label);
        }
    }
}
