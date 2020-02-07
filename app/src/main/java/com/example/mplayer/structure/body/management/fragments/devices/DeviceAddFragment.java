package com.example.mplayer.structure.body.management.fragments.devices;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

//TODO test then freeze
public class DeviceAddFragment extends Fragment {

    private final String TAG = "DeviceAddFragment";

    private FirebaseHandler firebaseHandler;

    private EditText deviceIdEt;

    private AtomicReference<String> userId;
    private AtomicReference<String> deviceId;
    private List<Device> devices;
    private List<Device> userDevices;

    private Boolean isEmpty;
    private Boolean isAvailable;
    private Boolean isDuplicate;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_add, container, false);

        Log.i(TAG, LogMessages.FRAGMENT_START.label);

        firebaseHandler = FirebaseHandler.getInstance();
        deviceIdEt = view.findViewById(R.id.deviceAddId);
        final Button addDeviceBtn = view.findViewById(R.id.deviceAddBtn);
        final Button backBtn = view.findViewById(R.id.deviceAddBackBtn);

        userId = new AtomicReference<>();
        deviceId = new AtomicReference<>();
        devices = Collections.synchronizedList(new ArrayList<>());
        userDevices = Collections.synchronizedList(new ArrayList<>());

        Thread thread = new Thread(() -> {
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

        final String finalUserId = userId.get();
        addDeviceBtn.setOnClickListener(v -> {
            if(isEmpty) {
                Log.e(TAG, "Empty device id");
                Toast.makeText(getActivity(), "Please enter a device id!", Toast.LENGTH_SHORT).show();
            } else if (!isAvailable) {
                Log.e(TAG, "Device not registered");
                Toast.makeText(getActivity(), "Serial incorrect!", Toast.LENGTH_SHORT).show();
            } else if(isDuplicate) {
                Log.e(TAG, "Device already registered for your user!");
                Toast.makeText(getActivity(), "Device already registered for your user!", Toast.LENGTH_SHORT).show();
            } else {
                Device device = new Device(finalUserId);
                device.setId(deviceIdEt.getText().toString());

                Log.d(TAG, "Adding device with id:" + device.getId());
                firebaseHandler.addDevice(device);

                if(getActivity() != null) {
                    Log.d(TAG, "Changing to device home fragment");
                    ((DeviceSettingsActivity)getActivity()).setViewPager(0);
                } else {
                    Log.e(TAG, LogMessages.ACTIVITY_NULL.label);
                }

            }
        });

        backBtn.setOnClickListener(v -> {
            if(getActivity() != null) {
                Log.d(TAG, LogMessages.CHANGE_HOME.label);
                ((DeviceSettingsActivity)getActivity()).setViewPager(0);
            } else {
                Log.e(TAG, LogMessages.ACTIVITY_NULL.label);
            }
        });

        return view;
    }

    private static class BackgroundTasks extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceAddFragment> weakReference;

        BackgroundTasks(DeviceAddFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            DeviceAddFragment fragment = weakReference.get();
            Log.d(fragment.TAG, LogMessages.ASYNC_START.label);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            DeviceAddFragment fragment = weakReference.get();

            Log.d(fragment.TAG, LogMessages.ASYNC_WORKING.label);
            Bundle bundle = fragment.getArguments();
            if (bundle != null) {
                fragment.userId.set(bundle.getString("userId"));
                //TODO update firebase handler
                fragment.firebaseHandler.getUserDevices(fragment.userId.get());
            } else {
                Log.e(fragment.TAG, LogMessages.USER_FETCH_ERROR.label);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            DeviceAddFragment fragment = weakReference.get();
            Log.d(fragment.TAG, LogMessages.ASYNC_END.label);
        }
    }

    private static class CheckTask extends AsyncTask<Void, Void, Void> {
        WeakReference<DeviceAddFragment> weakReference;

        CheckTask(DeviceAddFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            DeviceAddFragment fragment = weakReference.get();
            Log.d(fragment.TAG, LogMessages.ASYNC_START.label);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            DeviceAddFragment fragment = weakReference.get();

            //noinspection InfiniteLoopStatement
            while(true) {
                fragment.isEmpty = true;
                fragment.isAvailable = false;
                fragment.isDuplicate = false;

                if(!fragment.deviceId.get().isEmpty()) {
                    fragment.isEmpty = false;

                    fragment.userDevices.forEach(userDevice -> {
                        if (userDevice.getId().equals(fragment.deviceId.get())) {
                            fragment.isDuplicate = true;
                        }
                    });

                    fragment.devices.forEach(device -> {
                        if (device.getId().equals(fragment.deviceId.get())) {
                            fragment.isAvailable = true;
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    fragment.isEmpty = false;
                }
            }
        }
    }
}
