package com.example.mplayer.activities.body.management.fragments.playlists;

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
import com.example.mplayer.activities.body.management.ManagePlaylistActivity;
import com.example.mplayer.activities.body.management.ManageSetupActivity;
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.entities.Setup;
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

        Log.d(TAG, "Playlist delete fragment started");

        //TODO set element references
        final Spinner playlistSpinner = view.findViewById();
        final Button deletePlaylistBtn = view.findViewById();
        final Button backBtn = view.findViewById();

        //TODO update when firebase handler is done
        List<Playlist> playlists = firebaseHandler.getPlaylists();
        List<String> playlistsId = new ArrayList<>();

        for(Playlist playlist : playlists) {
            playlistsId.add(playlist.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, playlistsId);
        playlistSpinner.setAdapter(adapter);

        deletePlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playlistSpinner.getSelectedItem() != null) {
                    firebaseHandler.deletePlaylist(String.valueOf(playlistSpinner.getSelectedItemId()));
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ManagePlaylistActivity)getActivity()).setViewPager(0);
            }
        });

        return view;
    }
}
