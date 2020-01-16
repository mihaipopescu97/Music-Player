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
import com.example.mplayer.structure.body.management.activities.ManageDeviceActivity;
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.entities.Song;
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAddFragment extends Fragment {

    private static final String TAG = "PlaylistAddFragment";
    private FirebaseHandler firebaseHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO set layout
        View view = inflater.inflate(R.layout.fragment_device_add, container, false);

        Log.i(TAG, "Playlist add fragment started");

        firebaseHandler = FirebaseHandler.getInstance();

        final List<Song> songs = firebaseHandler.getSongs();
        final List<String> songNames = new ArrayList<>();
        final List<String> playlistSongs = new ArrayList<>();

        //TODO set element references
        final Spinner songsSpinner = view.findViewById();
        final Spinner playlistSongsSpinner = view.findViewById();
        final Button addSongBtn = view.findViewById();
        final Button doneBtn = view.findViewById();
        final Button backBtn = view.findViewById();

        String userId = null;
        if(getArguments() != null) {
            userId = getArguments().getString("roomId");
        } else {
            Log.e(TAG, "User id not received");
            startActivity(new Intent(getActivity(), BaseActivity.class));
        }

        for(Song song : songs) {
            songNames.add(song.getName());
        }


        ArrayAdapter<String> songsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, songNames);
        songsSpinner.setAdapter(songsAdapter);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
               ArrayAdapter<String> playlistAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, playlistSongs);
               playlistSongsSpinner.setAdapter(playlistAdapter);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        addSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(songsSpinner.getSelectedItem() != null) {
                   playlistSongs.add(String.valueOf(songsSpinner.getSelectedItemId()));
               } else {
                   Log.e(TAG, "Song not selected");
                   Toast.makeText(getActivity(), "Please select a song to be added!", Toast.LENGTH_SHORT).show();
               }
            }
        });

        final String finalUserId = userId;
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Playlist playlist = new Playlist(finalUserId);
                playlist.setSongs(playlistSongs);

                Log.d(TAG, "Adding playlist with id:" + playlist.getId());
                firebaseHandler.addPlaylist(playlist);

                thread.interrupt();

                Log.d(TAG, "Changing to playlist home fragment");
                ((ManageDeviceActivity)getActivity()).setViewPager(0);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.interrupt();
                Log.d(TAG, "Changing to playlist home fragment");
                ((ManageDeviceActivity)getActivity()).setViewPager(0);
            }
        });

        return view;
    }
}
