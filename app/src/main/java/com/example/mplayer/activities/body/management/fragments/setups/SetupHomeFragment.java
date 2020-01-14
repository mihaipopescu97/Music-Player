package com.example.mplayer.activities.body.management.fragments.setups;

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

public class SetupHomeFragment extends Fragment {
    private static final String TAG = "SetupHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        Log.d(TAG, "Setup home fragment started");

        final Button selectSetupBtn = view.findViewById();
        final Button addSetupBtn = view.findViewById();
        final Button removeSetupBtn = view.findViewById();
        final Button backBtn = view.findViewById();

        selectSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ManageDeviceActivity)getActivity()).setViewPager(1);
            }
        });

        addSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ManageDeviceActivity)getActivity()).setViewPager(2);
            }
        });

        removeSetupBtn.setOnClickListener(new View.OnClickListener() {
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
