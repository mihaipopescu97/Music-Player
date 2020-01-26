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
import com.example.mplayer.structure.body.BaseActivity;
import com.example.mplayer.structure.body.management.activities.ManageSetupActivity;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

public class SetupSelectFragment extends Fragment {

    private static final String TAG = "SetupSelectFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_select, container, false);

        Log.i(TAG, "Device view fragment started");

        FirebaseHandler firebaseHandler = FirebaseHandler.getInstance();

        final Spinner setupsSpinner = view.findViewById(R.id.setupSelectSpinner);
        final Button selectBtn = view.findViewById(R.id.setupSelectBtn);
        final Button backBtn = view.findViewById(R.id.setupSelectBackBtn);

        //TODO update when firebase handler is done
        List<Setup> setups = firebaseHandler.getSetups();
        List<String> setupsId = new ArrayList<>();

        String deviceId = null;
        if(getArguments() != null) {
            deviceId = getArguments().getString("deviceId");
        } else {
            Log.e(TAG, "Device id not received");
            startActivity(new Intent(getActivity(), BaseActivity.class));
        }

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

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setupsSpinner.getSelectedItem() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("setupId",  String.valueOf(setupsSpinner.getSelectedItem()));

                    if(getActivity() != null) {
                        Log.d(TAG, "Changing to setup home fragment");
                        ((ManageSetupActivity)getActivity()).setViewPager(0);
                    } else {
                        Log.e(TAG, "Activity is null");
                    }
                } else {
                    Log.e(TAG, "Setup not selected");
                    Toast.makeText(getActivity(), "Please select a setup!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    Log.d(TAG, "Changing to setup home fragment");
                    ((ManageSetupActivity)getActivity()).setViewPager(0);
                } else {
                    Log.e(TAG, "Activity is null");
                }
            }
        });

        return view;
    }
}