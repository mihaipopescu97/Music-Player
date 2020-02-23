package com.example.mplayer.structure.body.management.activities.playlists;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mplayer.R;
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PlaylistSelectActivity extends AppCompatActivity {
    private final String TAG = "PlaylistSelectActivity";

    private Spinner playlistSpinner;

    private AtomicReference<String> userId;
    private List<Playlist> playlists;

    private List<String> playlistsId;

    private FirebaseHandler firebaseHandler;
    private SharedResources resources;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_select);
        Log.d(TAG, LogMessages.ACTIVITY_START.label);

        playlistSpinner = findViewById(R.id.playlistSelectSpinner);
        userId = new AtomicReference<>();
        playlists = Collections.synchronizedList(new ArrayList<>());
        playlistsId = new ArrayList<>();
        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();

        new BackgroundTask(this).execute();

        while (playlists.isEmpty()) {
            Log.d(TAG, "Waiting for playlist list...");
        }

        playlists.forEach(playlist -> playlistsId.add(playlist.getId()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, playlistsId);
        playlistSpinner.setAdapter(adapter);
    }

    public void playlistSelect(View view) {
        if(playlistSpinner.getSelectedItem() != null) {
            resources.setPlaylistId(String.valueOf(playlistSpinner.getSelectedItem()));
        } else {
          Log.e(TAG, "Playlist not selected");
          Toast.makeText(getBaseContext(), "Please select a playlist!", Toast.LENGTH_SHORT).show();
        }
    }

    public void playlistSelectBack(View view) {
        startActivity(new Intent(getBaseContext(), PlaylistHomeActivity.class));
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<PlaylistSelectActivity> weakReference;

        BackgroundTask(PlaylistSelectActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PlaylistSelectActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.userId.set(activity.resources.getUserId());
            activity.firebaseHandler.getUserPlaylists(activity.userId.get(), activity.playlists);

            return null;
        }
    }
}
