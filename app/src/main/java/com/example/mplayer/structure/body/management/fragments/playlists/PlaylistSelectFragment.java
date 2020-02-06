package com.example.mplayer.structure.body.management.fragments.playlists;

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
import com.example.mplayer.structure.body.management.activities.settings.PlaylistSettingsActivity;
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSelectFragment extends Fragment {

    private static final String TAG = "PlaylistSelectFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_select, container, false);

        Log.d(TAG, "Device view fragment started");

        FirebaseHandler firebaseHandler = FirebaseHandler.getInstance();

        final Spinner playlistSpinner = view.findViewById(R.id.playlistSelectSpinner);
        final Button selectBtn = view.findViewById(R.id.playlistSelectBtn);
        final Button backBtn = view.findViewById(R.id.playlistSelectBackBtn);

        String userId = null;
        if(getArguments() != null) {
            userId = getArguments().getString("roomId");
        } else {
            Log.e(TAG, "User id not received");
            startActivity(new Intent(getActivity(), BaseActivity.class));
        }

        //TODO update when firebase handler is done
        List<Playlist> playlists = firebaseHandler.getRoomPlaylists(userId);
        List<String> playlistsId = new ArrayList<>();

        for(Playlist playlist : playlists) {
            playlistsId.add(playlist.getId());
        }

        ArrayAdapter<String> adapter = null;
        if(getActivity() != null) {
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, playlistsId);
        } else {
            Log.e(TAG, "Activity is null");
        }
        playlistSpinner.setAdapter(adapter);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playlistSpinner.getSelectedItem() != null) {

                    Bundle bundle = new Bundle();
                    bundle.putString("playlistId", String.valueOf(playlistSpinner.getSelectedItem()));

                    if(getActivity() != null) {
                        Log.d(TAG, "Changing to playlist home fragment");
                        ((PlaylistSettingsActivity)getActivity()).setViewPager(0);
                    } else {
                        Log.e(TAG, "Activity is null");
                    }

                } else {
                    Log.e(TAG, "Playlist not selected");
                    Toast.makeText(getActivity(), "Please select a playlist!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    Log.d(TAG, "Changing to playlist home fragment");
                    ((PlaylistSettingsActivity)getActivity()).setViewPager(0);
                } else {
                    Log.e(TAG, "Activity is null");
                }
            }
        });

        return view;
    }
}
