package com.example.mplayer.structure.body.management.activities.playlists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.SettingsActivity;
import com.example.mplayer.utils.enums.LogMessages;

public class PlaylistHomeActivity extends AppCompatActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "PlaylistHomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_home);

        Log.d(TAG, LogMessages.ACTIVITY_START.label);
    }

    public void homeAddPlaylist(View view) {
        Intent intent = new Intent(getBaseContext(), PlaylistAddActivity.class);
        intent.putExtra("prevActivity", getBaseContext().toString());
        startActivity(intent);
    }

    public void homeDeletePlaylist(View view) {
        startActivity(new Intent(getBaseContext(), PlaylistDeleteActivity.class));
    }

    public void homeSelectPlaylist(View view) {
        startActivity(new Intent(getBaseContext(), PlaylistSelectActivity.class));
    }

    public void homeBackPlaylist(View view) {
        startActivity(new Intent(getBaseContext(), SettingsActivity.class));
    }
}
