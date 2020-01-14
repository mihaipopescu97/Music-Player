package com.example.mplayer.activities.body.management.fragments.devices;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mplayer.R;
import com.example.mplayer.activities.body.management.ManageDeviceActivity;
import com.example.mplayer.entities.Device;
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

public class DeviceSelectFragment extends Fragment {

    private static final String TAG = "DeviceSelectFragment";
    private FirebaseHandler firebaseHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        Log.d(TAG, "Device view fragment started");

        firebaseHandler = FirebaseHandler.getInstance();

        final Spinner devicesSpinner = view.findViewById();
        final Button selectBtn = view.findViewById();
        final Button backBtn = view.findViewById();

        //TODO update when firebase handler is done
        List<Device> devices = firebaseHandler.getDevices();
        List<String> devicesId = new ArrayList<>();

        for(Device device : devices) {
            devicesId.add(device.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, devicesId);
        devicesSpinner.setAdapter(adapter);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(devicesSpinner.getSelectedItem() != null) {
                    //TODO send device id
                    String.valueOf(devicesSpinner.getSelectedItem());
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ManageDeviceActivity)getActivity()).setViewPager(0);
            }
        });

        return view;
    }
}
