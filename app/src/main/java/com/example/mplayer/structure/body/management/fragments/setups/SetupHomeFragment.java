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
import com.example.mplayer.structure.body.management.activities.BaseActivity;
import com.example.mplayer.structure.body.management.activities.settings.SetupSettingsActivity;

public class SetupHomeFragment extends Fragment {
    private static final String TAG = "SetupHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_home, container, false);

        Log.i(TAG, "Setup home fragment started");

        final Button selectSetupBtn = view.findViewById(R.id.setupHomeSelectBtn);
        final Button addSetupBtn = view.findViewById(R.id.setupHomeAddBtn);
        final Button deleteSetupBtn = view.findViewById(R.id.setupHomeDeleteBtn);
        final Button backBtn = view.findViewById(R.id.setupHomeBackBtn);

        //TODO get setupId
        final String setupId = "";

        selectSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    Log.d(TAG, "Changing to setup select fragment");
                    ((SetupSettingsActivity)getActivity()).setViewPager(1);
                } else {
                    Log.e(TAG, "Activity is null");
                }
            }
        });

        addSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    Log.d(TAG, "Changing to setup add fragment");
                    ((SetupSettingsActivity)getActivity()).setViewPager(2);
                } else {
                    Log.e(TAG, "Activity is null");
                }
            }
        });

        deleteSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    Log.d(TAG, "Changing to setup delete fragment");
                    ((SetupSettingsActivity)getActivity()).setViewPager(3);
                } else {
                    Log.e(TAG, "Activity is null");
                }
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
