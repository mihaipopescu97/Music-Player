package com.example.mplayer.fragments;

import android.os.Bundle;
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
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.entities.Song;
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdatePlaylist extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection, container, false);

        final List<Song> songs = FirebaseHandler.getSongs();
        final List<String> songNames = new ArrayList<>();
        final HashMap<String, String> songsMap = new HashMap<>();

        //Initial spinner - GONE after selecting
        final Spinner playlistSpinner = view.findViewById();

        //List songs spinner
        final Spinner songSpinner = view.findViewById();

        //List playlist songs
        final Spinner playlistSongsSpinner = view.findViewById();

        //Set selected playlist
        Button setPlaylist = view.findViewById();

        //2 cases after selecting the playlist
        final Button deleteFromPlaylistBtn = view.findViewById();
        final Button addToPlaylistBtn = view.findViewById();
        final Button addSongBtn = view.findViewById();

        //Initially gone
        playlistSongsSpinner.setVisibility(View.GONE);
        songSpinner.setVisibility(View.GONE);
        deleteFromPlaylistBtn.setVisibility(View.GONE);
        addToPlaylistBtn.setVisibility(View.GONE);
        addSongBtn.setVisibility(View.GONE);

        //Step 1
        //Set the playlist spinner
        List<Playlist> playlists = FirebaseHandler.getPlaylists();
        List<String> playlistsId = new ArrayList<>();

        for(Playlist playlist1 : playlists) {
            playlistsId.add(playlist1.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, playlistsId);
        playlistSpinner.setAdapter(adapter);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (1) {

                    songNames.clear();
                    playlistMap.clear();

                    for(Song song : songs) {
                        songNames.add(song.getName());
                        songsMap.put(song.getName(), song.getId());
                    }

                    ArrayAdapter<String> songAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, songNames);
                    playlistSpinner.setAdapter(songAdapter);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //Step 1
        setPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistSpinner.setVisibility(View.GONE);
                deleteFromPlaylistBtn.setVisibility(View.VISIBLE);
                addToPlaylistBtn.setVisibility(View.VISIBLE);
            }
        });

        //Branch 1
        addToPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPlaylistBtn.setVisibility(View.GONE);
                deleteFromPlaylistBtn.setVisibility(View.GONE);
                songSpinner.setVisibility(View.VISIBLE);
                playlistSongsSpinner.setVisibility(View.VISIBLE);
                addSongBtn.setVisibility(View.VISIBLE);

                songNames.clear();
                songsMap.clear();

                for(Song song : songs) {
                    songNames.add(song.getName());
                    songsMap.put(song.getName(), song.getId());
                }

                ArrayAdapter<String> songAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, songNames);
                playlistSpinner.setAdapter(songAdapter);

                thread.start();
            }
        });

        addSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songSpinner.getSelectedItem() != null) {

                }
            }
        });



        return view;
    }
}
