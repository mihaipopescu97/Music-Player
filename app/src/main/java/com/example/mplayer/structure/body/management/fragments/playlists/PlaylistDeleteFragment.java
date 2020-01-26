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
import com.example.mplayer.structure.body.BaseActivity;
import com.example.mplayer.structure.body.management.activities.ManagePlaylistActivity;
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
        View view = inflater.inflate(R.layout.fragment_playlist_delete, container, false);

        Log.i(TAG, "Playlist delete fragment started");

        firebaseHandler = FirebaseHandler.getInstance();

        final Spinner playlistSpinner = view.findViewById(R.id.playlistDeleteSpinner);
        final Button deletePlaylistBtn = view.findViewById(R.id.playlistDeleteBtn);
        final Button doneBtn = view.findViewById(R.id.playlistDeleteDoneBtn);

        String userId = null;
        if(getArguments() != null) {
            userId = getArguments().getString("roomId");
        } else {
            Log.e(TAG, "User id not received");
            startActivity(new Intent(getActivity(), BaseActivity.class));
        }

        final String finalUserId = userId;
        final Thread spinnerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Playlist> playlists = firebaseHandler.getRoomPlaylists(finalUserId);
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
                List<Playlist> playlists = firebaseHandler.getRoomPlaylists(finalUserId);

                if(playlists.isEmpty()) {
                    Log.w(TAG, "User:" + finalUserId + " has no more playlists");
                    Toast.makeText(getActivity(), "You have no more playlists to delete!", Toast.LENGTH_SHORT).show();
                    if(getActivity() != null) {
                        Log.d(TAG, "Changing to playlist home fragment");
                        ((ManagePlaylistActivity)getActivity()).setViewPager(0);
                    } else {
                        Log.e(TAG, "Activity is null");
                    }

                    //TODO not sure
                    Thread.currentThread().interrupt();
                }
            }
        });
        checkThread.start();


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
                spinnerThread.interrupt();
                checkThread.interrupt();
                if(getActivity() != null) {
                    Log.d(TAG, "Changing to playlist home fragment");
                    ((ManagePlaylistActivity)getActivity()).setViewPager(0);
                } else {
                    Log.e(TAG, "Activity is null");
                }
            }
        });

        return view;
    }
}
