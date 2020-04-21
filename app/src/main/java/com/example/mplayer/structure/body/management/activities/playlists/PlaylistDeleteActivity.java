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
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PlaylistDeleteActivity extends AppCompatActivity {

    private final String TAG = "PlaylistDeleteActivity";

    private AtomicReference<String> userId;
    private List<String> playlists;
    private FirebaseHandler firebaseHandler;
    private ArrayAdapter<String> adapter;

    private Spinner playlistSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_delete);

        Log.i(TAG, "Playlist delete fragment started");

        playlistSpinner = findViewById(R.id.playlistDeleteSpinner);

        SharedResources resources = SharedResources.getInstance();
        userId = new AtomicReference<>();
        adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, playlists);
        playlists = Collections.synchronizedList(new ArrayList<>());
        firebaseHandler = FirebaseHandler.getInstance();
        playlistSpinner.setAdapter(adapter);
        userId.set(resources.getUserId());
        new BackgroundTask(this).execute();



    }

    public void playlistDelete(View view) {
        if(playlistSpinner.getSelectedItem() != null) {
            Log.d(TAG, "Deleting playlist with id:" + playlistSpinner.getSelectedItem().toString());
            new DeleteTask(this).execute(String.valueOf(playlistSpinner.getSelectedItemId()));
        } else {
            Log.e(TAG, "Playlist not selected");
            Toast.makeText(getBaseContext(), "Please select a playlist!", Toast.LENGTH_SHORT).show();
        }
    }

    public void playlistDeleteDone(View view) {
        startActivity(new Intent(getBaseContext(), PlaylistHomeActivity.class));
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<PlaylistDeleteActivity> weakReference;

        BackgroundTask(PlaylistDeleteActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PlaylistDeleteActivity activity = weakReference.get();

            Log.i(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.firebaseHandler.getUserPlaylists(activity.userId.get(), activity.playlists, activity.adapter);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<String, Void, Void> {
        WeakReference<PlaylistDeleteActivity> weakReference;

        DeleteTask(PlaylistDeleteActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... strings) {
            PlaylistDeleteActivity activity = weakReference.get();

            Log.i(activity.TAG, LogMessages.ASYNC_WORKING.label);

           activity.firebaseHandler.deletePlaylist(strings[0], activity.playlists, activity.adapter);
           return null;
        }
    }
}
