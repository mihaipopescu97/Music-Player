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
import com.example.mplayer.activities.body.management.ManageDeviceActivity;

public class DeviceHomeFragment extends Fragment {

    private static final String TAG = "DeviceHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        Log.d(TAG, "Device home fragment started");

        final Button selectDevicesBtn = view.findViewById();
        final Button addDeviceBtn = view.findViewById();
        final Button removeDeviceBtn = view.findViewById();
        final Button backBtn = view.findViewById();

        selectDevicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ManageDeviceActivity)getActivity()).setViewPager(1);
            }
        });

        addDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ManageDeviceActivity)getActivity()).setViewPager(2);
            }
        });

        removeDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ManageDeviceActivity)getActivity()).setViewPager(3);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BaseActivity.class));
            }
        });

        return view;
    }
}
