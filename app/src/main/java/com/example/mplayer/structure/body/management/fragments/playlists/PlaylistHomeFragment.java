package com.example.mplayer.structure.body.management.fragments.playlists;

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
import com.example.mplayer.structure.body.BaseActivity;
import com.example.mplayer.structure.body.management.activities.ManagePlaylistActivity;

public class PlaylistHomeFragment extends Fragment {

    private static final String TAG = "PlaylistHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO set layout
        View view = inflater.inflate(R.layout.fragment_device_add, container, false);

        Log.d(TAG, "Playlist home fragment started");

        //TODO set references
        final Button selectPlaylistBtn = view.findViewById();
        final Button addPlaylistBtn = view.findViewById();
        final Button deletePlaylistBtn = view.findViewById();
        final Button backBtn = view.findViewById();

        //TODO get playlistId
        final String playlistId = "";

        selectPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to playlist select fragment");
                ((ManagePlaylistActivity)getActivity()).setViewPager(1);
            }
        });

        addPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to playlist add fragment");
                ((ManagePlaylistActivity)getActivity()).setViewPager(2);
            }
        });

        deletePlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to playlist delete fragment");
                ((ManagePlaylistActivity)getActivity()).setViewPager(3);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to base activity");
                Intent intent = new Intent(getActivity(), BaseActivity.class);
                intent.putExtra("playlistId", playlistId);
                startActivity(new Intent(getActivity(), BaseActivity.class));
            }
        });

        return view;
    }
}
