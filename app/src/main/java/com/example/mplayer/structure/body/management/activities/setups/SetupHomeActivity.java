package com.example.mplayer.structure.body.management.activities.setups;

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
public class SetupHomeActivity extends AppCompatActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "SetupHomeActivity";
    private FirebaseHandler firebaseHandler;
    private SharedResources resources;
    private List<String> rooms;
    private AtomicReference<String> playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_home);
        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();
        Log.d(TAG, LogMessages.ACTIVITY_START.label);
        rooms = new ArrayList<>();
        playlist = new AtomicReference<>();
        if(resources.getPlaylistId() == null) {
            new BackgroundTask(this).execute();
        }
    }

    public void homeAddSetup(View view) {
        setPlaylist();
        Intent intent = new Intent(getBaseContext(), SetupAddActivity.class);
        intent.putExtra("prevActivity", this.getClass());
        startActivity(intent);
    }

    public void homeDeleteSetup(View view) {
        setPlaylist();
        startActivity(new Intent(getBaseContext(), SetupDeleteActivity.class));
    }

    public void homeSelectSetup(View view) {
        setPlaylist();
        Intent intent = new Intent(getBaseContext(), SetupSelectActivity.class);
        intent.putExtra("prevActivity", this.getClass());
        startActivity(intent);
    }

    public void backHomeSetup(View view) {
        resources.setPlaylistId(playlist.get());
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
        WeakReference<SetupHomeActivity> weakReference;

        BackgroundTask(final SetupHomeActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SetupHomeActivity activity = weakReference.get();

            activity.firebaseHandler.getSetupRooms(activity.resources.getSetupId(), activity.rooms);
            return null;
        }
    }

    private static class PlaylistTask extends AsyncTask<Void, Void, Void> {
        WeakReference<SetupHomeActivity> weakReference;

        PlaylistTask(final SetupHomeActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SetupHomeActivity activity = weakReference.get();

            activity.firebaseHandler.getRoomPlaylistId(activity.rooms.get(0), activity.playlist);
            return null;
        }
    }
}
