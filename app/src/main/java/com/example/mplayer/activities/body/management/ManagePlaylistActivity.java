package com.example.mplayer.activities.body.management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.mplayer.R;
import com.example.mplayer.adapters.fragments.PlaylistSectionAdapter;
import com.example.mplayer.fragments.SetupFragment;

public class ManagePlaylistActivity extends AppCompatActivity {

    private static final String TAG = "ManagePlaylistActivity";

    private PlaylistSectionAdapter playlistSectionAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_playlist);

        playlistSectionAdapter = new PlaylistSectionAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);
        setupViewPage(viewPager);
    }

    private void setupViewPage(ViewPager viewPager) {
        PlaylistSectionAdapter adapter = new PlaylistSectionAdapter(getSupportFragmentManager());
        adapter.addFragment(new SetupFragment(), "PlaylistFragment");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }
}
