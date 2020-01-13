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
import com.example.mplayer.utils.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

public class DeletePlaylistFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        Spinner playlistSpinner = view.findViewById();
        Button deleteBtn = view.findViewById();
        Button backBtn = view.findViewById();

        List<Playlist> playlists = FirebaseHandler.getPlaylists();
        List<String> playlistId = new ArrayList<>();

        for(Playlist playlist : playlists) {
            playlistId.add(playlist.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, playlistId);
        playlistSpinner.setAdapter(adapter);



        return view;
    }

}
