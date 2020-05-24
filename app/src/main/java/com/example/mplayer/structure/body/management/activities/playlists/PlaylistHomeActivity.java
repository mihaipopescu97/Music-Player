package com.example.mplayer.structure.body.management.activities.playlists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.SettingsActivity;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

//FROZEN
public class PlaylistHomeActivity extends AppCompatActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "PlaylistHomeActivity";
    private FirebaseHandler firebaseHandler;
    private SharedResources resources;
    private List<String> rooms;
    private AtomicReference<String> playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_home);
        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();
        Log.d(TAG, LogMessages.ACTIVITY_START.label);
        rooms = new ArrayList<>();
        playlist = new AtomicReference<>();
        if(resources.getPlaylistId() == null) {
            new BackgroundTask(this).execute();
        }
    }

    public void homeAddPlaylist(View view) {
        setPlaylist();
        Intent intent = new Intent(getBaseContext(), PlaylistAddActivity.class);
        intent.putExtra("prevActivity", this.getClass());
        startActivity(intent);
    }

    public void homeDeletePlaylist(View view) {
        setPlaylist();
        startActivity(new Intent(getBaseContext(), PlaylistDeleteActivity.class));
    }

    public void homeSelectPlaylist(View view) {
        setPlaylist();
        startActivity(new Intent(getBaseContext(), PlaylistSelectActivity.class));
    }

    public void homeBackPlaylist(View view) {
        setPlaylist();
        startActivity(new Intent(getBaseContext(), SettingsActivity.class));
    }

    public void getPlaylists(View view) {
        new PlaylistTask(this).execute();
    }

    private void setPlaylist() {
        if (resources.getPlaylistId() == null) {
            resources.setPlaylistId(playlist.get());
        }
    }


    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<PlaylistHomeActivity> weakReference;

        BackgroundTask(final PlaylistHomeActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PlaylistHomeActivity activity = weakReference.get();

            activity.firebaseHandler.getSetupRooms(activity.resources.getSetupId(), activity.rooms);
            return null;
        }
    }

    private static class PlaylistTask extends AsyncTask<Void, Void, Void> {
        WeakReference<PlaylistHomeActivity> weakReference;

        PlaylistTask(final PlaylistHomeActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PlaylistHomeActivity activity = weakReference.get();

            activity.firebaseHandler.getRoomPlaylistId(activity.rooms.get(0), activity.playlist);
            return null;
        }
    }
}
