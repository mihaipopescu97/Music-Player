package com.example.mplayer.structure.body.management.fragments.playlists;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mplayer.R;
import com.example.mplayer.entities.Room;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.structure.body.management.activities.NewBuildActivity;
import com.example.mplayer.structure.body.management.activities.settings.DeviceSettingsActivity;
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.entities.Song;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PlaylistAddActivity extends AppCompatActivity {

    private static final String TAG = "PlaylistAddActivity";
    private FirebaseHandler firebaseHandler;

    private SharedResources resources;
    private List<Song> songs;
    private List<Room> rooms;
    private List<String> songNames;
    private List<String> playlistSongs;

    private EditText roomId;
    private Spinner songsSpinner;
    private Spinner playlistSongsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_playlist_add);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        firebaseHandler = FirebaseHandler.getInstance();

        songs = Collections.synchronizedList(new ArrayList<>());
        rooms = Collections.synchronizedList(new ArrayList<>());

        songNames = new ArrayList<>();
        playlistSongs = new ArrayList<>();
        resources = SharedResources.getInstance();

        roomId = findViewById();
        songsSpinner  = findViewById(R.id.playlistAddSpinner);
        playlistSongsSpinner  = findViewById(R.id.playlistAddSongSpinner);

        for(Song song : songs) {
            songNames.add(song.getName());
        }


        ArrayAdapter<String> songsAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, songNames);
        songsSpinner.setAdapter(songsAdapter);

        final Thread thread = new Thread(() -> {
            ArrayAdapter<String> playlistAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, playlistSongs);
            playlistSongsSpinner.setAdapter(playlistAdapter);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void doneRoom(View view) {
        AtomicBoolean check = new AtomicBoolean();
        check.set(false);
        AtomicReference<Setup> setup = new AtomicReference<>();
        firebaseHandler.getSetup(resources.getUserId(), setup);


        firebaseHandler.getSetupRooms(setup.get().getId(), rooms);

        rooms.forEach(room -> {
            if(room.equals(roomId.getText().toString().trim())) {
                check.set(true);
            }
        });

        if(check.get()) {
            Log.e(TAG)
        }
    }

    public void addSong(View view) {
        if(songsSpinner.getSelectedItem() != null) {
            playlistSongs.add(String.valueOf(songsSpinner.getSelectedItemId()));
        } else {
            Log.e(TAG, "Song not selected");
            Toast.makeText(getBaseContext(), "Please select a song to be added!", Toast.LENGTH_SHORT).show();
        }
    }

    public void donePlaylistAdd(View view) {
        Playlist playlist = new Playlist(roomId.getText().toString().trim());
        playlist.setSongs(playlistSongs);

        Log.d(TAG, "Adding playlist with id:" + playlist.getId() + "to room:" + roomId.getText().toString().trim());

        //TODO move to background
        firebaseHandler.addPlaylist(playlist);

        Log.d(TAG, "Changing to new build activity");
        startActivity(new Intent(getBaseContext(), NewBuildActivity.class));
    }
}
