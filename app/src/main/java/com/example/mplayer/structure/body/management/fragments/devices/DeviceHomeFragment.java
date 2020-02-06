package com.example.mplayer.structure.body.management.fragments.devices;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.BaseActivity;
import com.example.mplayer.structure.body.management.activities.settings.DeviceSettingsActivity;

import java.util.concurrent.atomic.AtomicReference;

public class DeviceHomeFragment extends Fragment {

    private static final String TAG = "DeviceHomeFragment";
    private Thread getUserThread;
    private AtomicReference<String> userId = new AtomicReference<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_home, container, false);
        Log.d(TAG, "Device home fragment started");

        final Button selectDevicesBtn = view.findViewById(R.id.deviceHomeSelectBtn);
        final Button addDeviceBtn = view.findViewById(R.id.deviceHomeAddBtn);
        final Button deleteDeviceBtn = view.findViewById(R.id.deviceHomeDeleteBtn);
        final Button backBtn = view.findViewById(R.id.deviceHomeBackBtn);


        //TODO get devID from select
        final String deviceId = "";

        getUserThread = new Thread(() -> {
            Bundle bundle = getArguments();
            assert bundle != null;
            userId.set(bundle.getString("userId2"));
        });

        selectDevicesBtn.setOnClickListener(v -> {
            if(getActivity() != null) {
                Log.d(TAG, "Changing to device select fragment");
                ((DeviceSettingsActivity)getActivity()).setViewPager(1);
            } else {
                Log.e(TAG, "Activity is null");
            }
        });

        addDeviceBtn.setOnClickListener(v -> {
            if(getActivity() != null) {
                Log.d(TAG, "Changing to device add fragment");
                ((DeviceSettingsActivity)getActivity()).setViewPager(2);
            } else {
                Log.e(TAG, "Activity is null");
            }
        });

        deleteDeviceBtn.setOnClickListener(v -> {
            if(getActivity() != null) {
                Log.d(TAG, "Changing to device delete fragment");
                ((DeviceSettingsActivity)getActivity()).setViewPager(3);
            } else {
                Log.e(TAG, "Activity is null");
            }
        });

        backBtn.setOnClickListener(v -> {
            Log.d(TAG, "Changing to device base activity");
            Intent intent = new Intent(getActivity(), BaseActivity.class);
            intent.putExtra("deviceId", deviceId);
            startActivity(new Intent(getActivity(), BaseActivity.class));
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getUserThread.start();

        while (getUserThread.isAlive()) {
            Log.d(TAG, "Waiting for thread to read");
        }

        Log.i(TAG, "Got user:" + userId.get());
    }
}

