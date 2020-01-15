package com.example.mplayer.activities.body.management.fragments.playlists;

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
import com.example.mplayer.activities.body.BaseActivity;
import com.example.mplayer.activities.body.management.activities.ManagePlaylistActivity;
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDeleteFragment extends Fragment {
    private static final String TAG = "PlaylistDeleteFragment";
    private FirebaseHandler firebaseHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO set layout
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        Log.i(TAG, "Playlist delete fragment started");

        firebaseHandler = FirebaseHandler.getInstance();

        //TODO set element references
        final Spinner playlistSpinner = view.findViewById();
        final Button deletePlaylistBtn = view.findViewById();
        final Button doneBtn = view.findViewById();

        String userId = null;
        if(getArguments() != null) {
            userId = getArguments().getString("userId");
        } else {
            Log.e(TAG, "User id not received");
            startActivity(new Intent(getActivity(), BaseActivity.class));
        }

        final String finalUserId = userId;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Playlist> playlists = firebaseHandler.getUserPlaylists(finalUserId);
                List<String> playlistsId = new ArrayList<>();

                for(Playlist playlist : playlists) {
                    playlistsId.add(playlist.getId());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, playlistsId);
                playlistSpinner.setAdapter(adapter);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        //TODO create a check thread


        deletePlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playlistSpinner.getSelectedItem() != null) {
                    Log.d(TAG, "Deleting playlist with id:" + playlistSpinner.getSelectedItem().toString());
                    firebaseHandler.deletePlaylist(String.valueOf(playlistSpinner.getSelectedItemId()));
                } else {
                    Log.e(TAG, "Playlist not selected");
                    Toast.makeText(getActivity(), "Please select a playlist!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.interrupt();
                Log.d(TAG, "Changing to playlist home fragment");
                ((ManagePlaylistActivity)getActivity()).setViewPager(0);
            }
        });

        return view;
    }
}
