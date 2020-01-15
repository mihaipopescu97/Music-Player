package com.example.mplayer.activities.body.management.fragments.devices;

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
import com.example.mplayer.activities.body.BaseActivity;
import com.example.mplayer.activities.body.management.activities.ManageDeviceActivity;

public class DeviceHomeFragment extends Fragment {

    private static final String TAG = "DeviceHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO set layout
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        Log.d(TAG, "Device home fragment started");

        //TODO set references
        final Button selectDevicesBtn = view.findViewById();
        final Button addDeviceBtn = view.findViewById();
        final Button deleteDeviceBtn = view.findViewById();
        final Button backBtn = view.findViewById();

        selectDevicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to device select fragment");
                ((ManageDeviceActivity)getActivity()).setViewPager(1);
            }
        });

        addDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to device add fragment");
                ((ManageDeviceActivity)getActivity()).setViewPager(2);
            }
        });

        deleteDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to device delete fragment");
                ((ManageDeviceActivity)getActivity()).setViewPager(3);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to device base activity");
                startActivity(new Intent(getActivity(), BaseActivity.class));
            }
        });

        return view;
    }
}
