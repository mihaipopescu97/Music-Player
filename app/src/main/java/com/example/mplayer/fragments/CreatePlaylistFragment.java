package com.example.mplayer.fragments;

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
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.entities.Song;
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreatePlaylistFragment extends Fragment {

    private final static String TAG = "CreatePlaylistFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO create layout
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        final HashMap<String, String> hashMap = new HashMap();
        final List<String> addedSongs = new ArrayList<>();
        final Playlist playlist = new Playlist();
        List<Song> songsList = FirebaseHandler.getSongs();
        List<String> songs = new ArrayList<>();


        playlist.setId();

        final Spinner songSpinner = view.findViewById();
        Button addBtn = view.findViewById();
        final Button createBtn = view.findViewById();
        Button backBtn = view.findViewById();

        createBtn.setVisibility(View.GONE);
        addBtn.setVisibility(View.GONE);

        //Fetch from db and set spinner
        for(Song song : songsList) {
            songs.add(song.getName());
            hashMap.put(song.getName(), song.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, songs);
        songSpinner.setAdapter(adapter);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(songSpinner.getSelectedItem() != null) {
                    createBtn.setVisibility(View.VISIBLE);
                    addBtn.setVisibility(View.VISIBLE);
                    Log.d(TAG, "Playlist selected");
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addedSongs.add(hashMap.get(songSpinner.getSelectedItem()));
            }
        });


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlist.setSongs(addedSongs);
                FirebaseHandler.addPlaylist(playlist);
                thread.interrupt();
                ((HomeActivity)getActivity()).setViewPager(0);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedSongs.clear();
                thread.interrupt();
                ((HomeActivity)getActivity()).setViewPager(0);
            }
        });

        return view;
    }
}
