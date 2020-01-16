package com.example.mplayer.structure.body.management.fragments.setups;

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
import com.example.mplayer.structure.body.BaseActivity;
import com.example.mplayer.structure.body.management.activities.ManageDeviceActivity;

public class SetupHomeFragment extends Fragment {
    private static final String TAG = "SetupHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO set layout
        View view = inflater.inflate(R.layout.fragment_device_add, container, false);

        Log.i(TAG, "Setup home fragment started");

        //TODO set references
        final Button selectSetupBtn = view.findViewById();
        final Button addSetupBtn = view.findViewById();
        final Button deleteSetupBtn = view.findViewById();
        final Button backBtn = view.findViewById();

        //TODO get setupId
        final String setupId = "";

        selectSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to setup select fragment");
                ((ManageDeviceActivity)getActivity()).setViewPager(1);
            }
        });

        addSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to setup add fragment");
                ((ManageDeviceActivity)getActivity()).setViewPager(2);
            }
        });

        deleteSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to setup delete fragment");
                ((ManageDeviceActivity)getActivity()).setViewPager(3);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to base activity");
                Intent intent = new Intent(getActivity(), BaseActivity.class);
                intent.putExtra("setupId", setupId);
                startActivity(new Intent(getActivity(), BaseActivity.class));
            }
        });

        return view;
    }
}
