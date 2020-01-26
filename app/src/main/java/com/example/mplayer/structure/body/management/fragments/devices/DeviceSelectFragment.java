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

public class DeviceSelectFragment extends Fragment {

    private static final String TAG = "DeviceSelectFragment";
    private FirebaseHandler firebaseHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_select, container, false);

        Log.d(TAG, "Device view fragment started");

        firebaseHandler = FirebaseHandler.getInstance();

        //TODO set references
        final Spinner devicesSpinner = view.findViewById(R.id.deviceSelectSpinner);
        final Button selectBtn = view.findViewById(R.id.deviceSelectBtn);
        final Button backBtn = view.findViewById(R.id.deviceSelectBackBtn);

        String userId = null;
//        if(getArguments() != null) {
//            userId = getArguments().getString("roomId");
//        } else {
//            Log.e(TAG, "User id not received");
//            startActivity(new Intent(getActivity(), BaseActivity.class));
//        }

        List<Device> devices = firebaseHandler.getUserDevices(userId);
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

                    Bundle bundle = new Bundle();
                    bundle.putString("deviceId",  String.valueOf(devicesSpinner.getSelectedItem()));

                    //TODO send data to new activity
                    Log.d(TAG, "Changing to base activity");
                    startActivity(new Intent(getActivity(), BaseActivity.class));
                } else {
                    Log.e(TAG, "Device not selected");
                    Toast.makeText(getActivity(), "Please select a device!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to device home fragment");
                ((ManageDeviceActivity)getActivity()).setViewPager(0);
            }
        });

        return view;
    }
}