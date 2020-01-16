package com.example.mplayer.structure.body.management.fragments.devices;

import android.content.Intent;
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
import com.example.mplayer.structure.body.BaseActivity;
import com.example.mplayer.structure.body.management.activities.ManageDeviceActivity;
import com.example.mplayer.entities.Device;
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

public class DeviceDeleteFragment extends Fragment {

    private static final String TAG = "DeviceDeleteFragment";
    private FirebaseHandler firebaseHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO set layout
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        Log.i(TAG, "Device delete fragment started");

        firebaseHandler = FirebaseHandler.getInstance();

        //TODO set element references
        final Spinner devicesSpinner = view.findViewById();
        final Button deleteDeviceBtn = view.findViewById();
        final Button doneBtn = view.findViewById();

        String userId = null;
        if(getArguments() != null) {
            userId = getArguments().getString("roomId");
        } else {
            Log.e(TAG, "User id not received");
            startActivity(new Intent(getActivity(), BaseActivity.class));
        }


        final String finalUserId = userId;
        final Thread spinnerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Device> devices = firebaseHandler.getUserDevices(finalUserId);
                List<String> devicesId = new ArrayList<>();

                for(Device device : devices) {
                    devicesId.add(device.getId());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, devicesId);
                devicesSpinner.setAdapter(adapter);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        spinnerThread.start();

        final Thread checkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Device> devices = firebaseHandler.getUserDevices(finalUserId);

                if(devices.isEmpty()) {
                    Log.w(TAG, "User:" + finalUserId + " has no more devices");
                    Toast.makeText(getActivity(), "You have no more devices to delete!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Changing to device home fragment");
                    ((ManageDeviceActivity)getActivity()).setViewPager(0);

                    //TODO not sure
                    Thread.currentThread().interrupt();
                }
            }
        });
        checkThread.start();

        deleteDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(devicesSpinner.getSelectedItem() != null) {
                    Log.d(TAG, "Deleting device:" + devicesSpinner.getSelectedItem().toString());
                    firebaseHandler.deleteDevice(String.valueOf(devicesSpinner.getSelectedItemId()));
                } else {
                    Log.e(TAG, "Device not selected");
                    Toast.makeText(getActivity(), "Please select a device!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerThread.interrupt();
                checkThread.interrupt();
                Log.d(TAG, "Changing to device home fragment");
                ((ManageDeviceActivity)getActivity()).setViewPager(0);
            }
        });


        return view;
    }
}
