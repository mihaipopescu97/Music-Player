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
import com.example.mplayer.structure.body.SingleActivity;
import com.example.mplayer.structure.body.management.activities.ManagePlaylistActivity;

public class PlaylistHomeFragment extends Fragment {

    private static final String TAG = "PlaylistHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_home, container, false);

        Log.d(TAG, "Playlist home fragment started");

        final Button selectPlaylistBtn = view.findViewById(R.id.playlistHomeSelectBtn);
        final Button addPlaylistBtn = view.findViewById(R.id.playlistHomeAddBtn);
        final Button deletePlaylistBtn = view.findViewById(R.id.playlistHomeDeleteBtn);
        final Button backBtn = view.findViewById(R.id.playlistHomeBackBtn);

        //TODO get playlistId
        final String playlistId = "";

        selectPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    Log.d(TAG, "Changing to playlist select fragment");
                    ((ManagePlaylistActivity)getActivity()).setViewPager(1);
                } else {
                    Log.e(TAG, "Activity is null");
                }
            }
        });

        addPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    Log.d(TAG, "Changing to playlist add fragment");
                    ((ManagePlaylistActivity)getActivity()).setViewPager(2);
                } else {
                    Log.e(TAG, "Activity is null");
                }
            }
        });

        deletePlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    Log.d(TAG, "Changing to playlist delete fragment");
                    ((ManagePlaylistActivity)getActivity()).setViewPager(3);
                } else {
                    Log.e(TAG, "Activity is null");
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Changing to base activity");
                Intent intent = new Intent(getActivity(), SingleActivity.class);
                intent.putExtra("playlistId", playlistId);
                startActivity(new Intent(getActivity(), SingleActivity.class));
            }
        });

        return view;
    }
}
