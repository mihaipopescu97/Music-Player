package com.example.mplayer.activities.body.management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mplayer.R;
import com.example.mplayer.adapters.fragments.PlaylistSectionAdapter;

public class ManagePlaylistActivity extends AppCompatActivity {

    private static final String TAG = "ManagePlaylistActivity";

    private PlaylistSectionAdapter playlistSectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_playlist);

        playlistSectionAdapter = new PlaylistSectionAdapter(getSupportFragmentManager());
    }
}
