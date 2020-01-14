package com.example.mplayer.activities.body.management.fragments.setups;

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
import com.example.mplayer.activities.body.management.ManageSetupActivity;
import com.example.mplayer.entities.Device;
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
        //TODO set layout
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        Log.d(TAG, "Setup delete fragment started");

        //TODO set element references
        final Spinner setupsSpinner = view.findViewById();
        final Button deleteSetupBtn = view.findViewById();
        final Button backBtn = view.findViewById();

        firebaseHandler = FirebaseHandler.getInstance();

        //TODO update when firebase handler is done
        List<Setup> setups = firebaseHandler.getSetups();
        List<String> setupsId = new ArrayList<>();

        for(Setup setup : setups) {
            setupsId.add(setup.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, setupsId);
        setupsSpinner.setAdapter(adapter);

        deleteSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setupsSpinner.getSelectedItem() != null) {
                    firebaseHandler.deleteSetup(String.valueOf(setupsSpinner.getSelectedItemId()));
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ManageSetupActivity)getActivity()).setViewPager(0);
            }
        });

        return view;
    }
}