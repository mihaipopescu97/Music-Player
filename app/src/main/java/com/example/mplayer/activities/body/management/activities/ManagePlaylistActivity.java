package com.example.mplayer.activities.body.management.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.mplayer.R;
import com.example.mplayer.activities.body.management.fragments.playlists.PlaylistAddFragment;
import com.example.mplayer.activities.body.management.fragments.playlists.PlaylistDeleteFragment;
import com.example.mplayer.activities.body.management.fragments.playlists.PlaylistHomeFragment;
import com.example.mplayer.activities.body.management.fragments.playlists.PlaylistSelectFragment;
import com.example.mplayer.adapters.fragments.PlaylistSectionAdapter;

public class ManagePlaylistActivity extends AppCompatActivity {

    private static final String TAG = "ManagePlaylistActivity";

    private PlaylistSectionAdapter playlistSectionAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_playlist);

        Log.i(TAG, "Manage playlist activity started");

        playlistSectionAdapter = new PlaylistSectionAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);
        setupViewPage(viewPager);
    }

    private void setupViewPage(ViewPager viewPager) {
        PlaylistSectionAdapter adapter = new PlaylistSectionAdapter(getSupportFragmentManager());

        Log.d(TAG, "Playlist home -> 0");
        adapter.addFragment(new PlaylistHomeFragment(), "PlaylistHomeFragment");

        Log.d(TAG, "Playlist select -> 1");
        adapter.addFragment(new PlaylistSelectFragment(), "PlaylistSelectFragment");

        Log.d(TAG, "Playlist add -> 2");
        adapter.addFragment(new PlaylistAddFragment(), "PlaylistAddFragment");

        Log.d(TAG, "Playlist delete -> 3");
        adapter.addFragment(new PlaylistDeleteFragment(), "PlaylistDeleteFragment");

        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }
}
