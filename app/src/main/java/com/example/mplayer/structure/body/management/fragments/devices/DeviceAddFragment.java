package com.example.mplayer.structure.body.management.fragments.devices;

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
import androidx.fragment.app.Fragment;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.ManageDeviceActivity;
import com.example.mplayer.entities.Device;
import com.example.mplayer.utils.FirebaseHandler;

public class DeviceAddFragment extends Fragment {

    private static final String TAG = "DeviceAddFragment";
    private FirebaseHandler firebaseHandler;
    private Thread listener1,listener2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_add, container, false);

        Log.i(TAG, "Device add fragment started");

        firebaseHandler = FirebaseHandler.getInstance();
        final EditText deviceId = view.findViewById(R.id.deviceAddId);
        final Button addDeviceBtn = view.findViewById(R.id.deviceAddBtn);
        final Button backBtn = view.findViewById(R.id.deviceAddBackBtn);

//        String userId = ;
//        if(getArguments() != null) {
            String userId = getArguments().getString("userId2");
//        } else {
//            Log.e(TAG, "User id not received");
//            startActivity(new Intent(getActivity(), SingleActivity.class));
//        }

        final String finalUserId = userId;
        addDeviceBtn.setOnClickListener(v -> {
            if(deviceId.getText().toString().matches("")) {
                Log.e(TAG, "Empty device id");
                Toast.makeText(getActivity(), "Please enter a device id!", Toast.LENGTH_SHORT).show();
            } else {
                Device device = new Device(finalUserId);
                device.setId(deviceId.getText().toString());

                Log.d(TAG, "Adding device with id:" + device.getId());
                firebaseHandler.addDevice(device);

                if(getActivity() != null) {
                    Log.d(TAG, "Changing to device home fragment");
                    ((ManageDeviceActivity)getActivity()).setViewPager(0);
                } else {
                    Log.e(TAG, "Activity is null");
                }

            }
        });

        backBtn.setOnClickListener(v -> {
            if(getActivity() != null) {
                Log.d(TAG, "Changing to device home fragment");
                ((ManageDeviceActivity)getActivity()).setViewPager(0);
            } else {
                Log.e(TAG, "Activity is null");
            }
        });

        return view;
    }
}
