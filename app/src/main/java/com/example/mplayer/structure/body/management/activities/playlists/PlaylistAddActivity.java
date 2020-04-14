package com.example.mplayer.structure.body.management.activities.playlists;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mplayer.R;
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PlaylistAddActivity extends AppCompatActivity {

    private final String TAG = "PlaylistAddActivity";

    private FirebaseHandler firebaseHandler;
    private SharedResources resources;


    private List<String> songNames;

    private AtomicReference<Class> prevActivity;
    private List<String> playlistSongs;

    private Spinner songsSpinner;
    private Spinner playlistSpinner;

    private ArrayAdapter<String> songsAdapter;

    private ArrayAdapter<String> playlistAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_add);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        firebaseHandler = FirebaseHandler.getInstance();

        songNames = new ArrayList<>();

        prevActivity = new AtomicReference<>();
        playlistSongs = new ArrayList<>();
        resources = SharedResources.getInstance();

        songsSpinner  = findViewById(R.id.playlistAddSpinner);
        playlistSpinner = findViewById(R.id.playlistAddSongSpinner);

        new BackgroundTask(this).execute();

        songsAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, songNames);
        songsSpinner.setAdapter(songsAdapter);

        playlistAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, playlistSongs);
        playlistSpinner.setAdapter(playlistAdapter);
    }

    public void addSong(View view) {
        if(songsSpinner.getSelectedItem() != null) {
            playlistSongs.add(String.valueOf(songsSpinner.getSelectedItem()));
            playlistAdapter.notifyDataSetChanged();
        } else {
            Log.e(TAG, "Song not selected");
            Toast.makeText(getBaseContext(), "Please select a song to be added!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteSong(View view) {
        if(playlistSpinner.getSelectedItem() != null) {
            playlistSongs.remove(String.valueOf(songsSpinner.getSelectedItem()));
            playlistAdapter.notifyDataSetChanged();
        } else {
            Log.e(TAG, "Song not selected");
            Toast.makeText(getBaseContext(), "Please select a song to be deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    public void donePlaylistAdd(View view) {
        Playlist playlist = new Playlist(resources.getRoomID());
        playlist.setSongs(playlistSongs);

        Log.d(TAG, "Adding playlist with id:" + playlist.getId() + "to room:" + resources.getRoomID());

        new AddPlaylistAsync(this, playlist).execute();

        Class<?> cls = prevActivity.get();
        Intent intent = new Intent(getBaseContext(),cls);
        startActivity(intent);
    }

    public void backPlaylistSelect(View view) {
        Class<?> cls = prevActivity.get();
        Intent intent = new Intent(getBaseContext(),cls);
        startActivity(intent);
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<PlaylistAddActivity> weakReference;

        BackgroundTask(PlaylistAddActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PlaylistAddActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.firebaseHandler.getSongs(activity.songNames, activity.songsAdapter);
            Intent intent = activity.getIntent();
            activity.prevActivity.set((Class) intent.getExtras().get("prevActivity"));
            return null;
        }
    }

    private static class AddPlaylistAsync extends AsyncTask<Void, Void, Void> {
        WeakReference<PlaylistAddActivity> weakReference;
        Playlist playlist;

        AddPlaylistAsync(PlaylistAddActivity activity, Playlist playlist) {
            weakReference = new WeakReference<>(activity);
            this.playlist = playlist;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PlaylistAddActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.firebaseHandler.addPlaylist(playlist);
            return null;
        }
    }
}
