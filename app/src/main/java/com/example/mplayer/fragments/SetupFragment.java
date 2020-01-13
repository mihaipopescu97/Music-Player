package com.example.mplayer.fragments;

import android.content.Intent;
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
import com.example.mplayer.activities.HomeActivity;
import com.example.mplayer.activities.PlayerActivity;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

public class SetupFragment extends Fragment {

    private static final String  TAG= "SetupFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup, container, false);


        Log.d(TAG, "Setup fragment created");

        final Spinner setups = view.findViewById(R.id.setups);
        final Button playBtn = view.findViewById(R.id.play);
        final Button backBtn = view.findViewById(R.id.back);

        playBtn.setVisibility(View.GONE);


        List<Setup> setupsObj = FirebaseHandler.getSetups();
        List<String> setupsId = new ArrayList<>();

        //Spinner setup
        for(Setup setup : setupsObj) {
            setupsId.add(setup.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, setupsId);
        setups.setAdapter(adapter);

        //Check if a setup is selected, else do not display the play button
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(setups.getSelectedItem() != null) {
                    playBtn.setVisibility(View.VISIBLE);
                    Log.d(TAG, "Setup selected");
                }
            }
        });

        thread.start();

        //Button functionality
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.interrupt();
                startActivity(new Intent(getActivity(), PlayerActivity.class));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.interrupt();
                ((HomeActivity)getActivity()).setViewPager(0);
            }
        });

        return view;
    }





}

