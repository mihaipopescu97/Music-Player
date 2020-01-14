package com.example.mplayer.activities.body.management.fragments.devices;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mplayer.R;
import com.example.mplayer.activities.body.management.ManageDeviceActivity;
import com.example.mplayer.entities.Device;
import com.example.mplayer.utils.FirebaseHandler;

public class DeviceAddFragment extends Fragment {

    private static final String TAG = "DeviceAddFragment";
    private FirebaseHandler firebaseHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO set layout
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        Log.i(TAG, "Device add fragment started");

        firebaseHandler = FirebaseHandler.getInstance();

        //TODO set element references
        final TextView deviceId = view.findViewById();
        final Button addDeviceBtn = view.findViewById();
        final Button backBtn = view.findViewById();

        //TODO pass user id;
        final String userId = "";

        addDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deviceId.getText().toString().matches("")) {
                    Log.e(TAG, "Empty device id");
                    Toast.makeText(getActivity(), "Please enter a device id!", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO check if valid id
                    Device device = new Device(userId);
                    device.setId(deviceId.getText().toString());

                    Log.d(TAG, "Adding device with id:" + device.getId());
                    firebaseHandler.addDevice(device);

                    Log.d(TAG, "Changing to device home fragment");
                    ((ManageDeviceActivity)getActivity()).setViewPager(0);
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
