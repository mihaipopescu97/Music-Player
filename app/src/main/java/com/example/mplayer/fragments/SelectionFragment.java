package com.example.mplayer.fragments;

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
import com.example.mplayer.activities.HomeActivity;
import com.example.mplayer.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SelectionFragment extends Fragment {

    private static final String TAG = "SelectionFragment";
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection, container, false);
        Log.d(TAG, "Selection fragment created");

        Button setupBtn = view.findViewById(R.id.goToSetup);
        Button createPlaylistBtn = view.findViewById(R.id.goToMap);
        Button mapBtn = view.findViewById(R.id);
        Button logOutBtn = view.findViewById(R.id);


        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).setViewPager(1);
            }
        });


        createPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).setViewPager(2);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).setViewPager(3);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });


        return view;
    }
}
