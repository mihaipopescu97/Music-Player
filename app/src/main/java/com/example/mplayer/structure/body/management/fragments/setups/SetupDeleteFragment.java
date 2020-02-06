package com.example.mplayer.structure.body.management.fragments.setups;

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
import com.example.mplayer.structure.body.management.activities.BaseActivity;
import com.example.mplayer.structure.body.management.activities.settings.SetupSettingsActivity;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

public class SetupDeleteFragment extends Fragment {

    private static final String TAG = "SetupDeleteFragment";
    private FirebaseHandler firebaseHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_delete, container, false);

        Log.i(TAG, "Setup delete fragment started");

        final Spinner setupsSpinner = view.findViewById(R.id.setupDeleteSpinner);
        final Button deleteSetupBtn = view.findViewById(R.id.setupDeleteBtn);
        final Button doneBtn = view.findViewById(R.id.setupDeleteDoneBtn);

        firebaseHandler = FirebaseHandler.getInstance();

        String deviceId = null;
        if(getArguments() != null) {
            deviceId = getArguments().getString("deviceId");
        } else {
            Log.e(TAG, "Device id not received");
            startActivity(new Intent(getActivity(), BaseActivity.class));
        }

        final List<String> setupsId = new ArrayList<>();

        final String finalDeviceId = deviceId;
        //TODO add a check thread
        final Thread spinnerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Setup> setups = firebaseHandler.getDeviceSetups(finalDeviceId);
                setupsId.clear();

                for(Setup setup : setups) {
                    setupsId.add(setup.getId());
                }

                ArrayAdapter<String> adapter = null;
                if(getActivity() != null) {
                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, setupsId);
                } else {
                    Log.e(TAG, "Activity is null");
                }
                setupsSpinner.setAdapter(adapter);

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
                List<Setup> devices = firebaseHandler.getDeviceSetups(finalDeviceId);

                if(devices.isEmpty()) {
                    Log.w(TAG, "User:" + finalDeviceId + " has no more devices");
                    Toast.makeText(getActivity(), "You have no more devices to delete!", Toast.LENGTH_SHORT).show();
                    if(getActivity() != null) {
                        Log.d(TAG, "Changing to device home fragment");
                        ((SetupSettingsActivity)getActivity()).setViewPager(0);
                    } else {
                        Log.e(TAG, "Activity is null");
                    }
                    Log.d(TAG, "Changing to device home fragment");
                    ((SetupSettingsActivity)getActivity()).setViewPager(0);

                    //TODO not sure
                    Thread.currentThread().interrupt();
                }
            }
        });
        checkThread.start();



        deleteSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setupsSpinner.getSelectedItem() != null) {
                    Log.d(TAG, "Deleting setup with id:" + setupsSpinner.getSelectedItem());
                    firebaseHandler.deleteSetup(String.valueOf(setupsSpinner.getSelectedItemId()));
                } else {
                    Log.e(TAG, "Setup not selected");
                    Toast.makeText(getActivity(), "Please select a setup!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerThread.interrupt();
                checkThread.interrupt();
                if(getActivity() != null) {
                    Log.d(TAG, "Changing to device home fragment");
                    ((SetupSettingsActivity)getActivity()).setViewPager(0);
                } else {
                    Log.e(TAG, "Activity is null");
                }
            }
        });

        return view;
    }
}
