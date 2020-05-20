package com.example.mplayer.structure.player;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mplayer.R;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.utils.BluetoothConnectionService;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;
import com.example.mplayer.utils.enums.PlayType;
import com.example.mplayer.utils.helpers.DownloadTask;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class InitDataActivity extends AppCompatActivity {
    private static final String TAG = "InitDataActivity";
    private FirebaseHandler firebaseHandler;
    private SharedResources resources;
    private List<String> urls;
    private List<String> namesList;
    private AtomicReference<Setup> setup;
    private AtomicReference<String> playlistId;
    private StringBuilder stringBuilder;
    private BluetoothConnectionService bluetoothConnectionService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_data);
        Log.i(TAG ,LogMessages.ACTIVITY_START.label);
        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();
        playlistId = new AtomicReference<>();
        urls = Collections.synchronizedList(new ArrayList<>());
        namesList = Collections.synchronizedList(new ArrayList<>());
        setup = new AtomicReference<>();
        if (resources.getPlayType().equals(PlayType.FAMILY.label)) {
            bluetoothConnectionService = BluetoothConnectionService.getInstance();
        }

        stringBuilder = new StringBuilder();
        stringBuilder.append("data:");
        stringBuilder.append(resources.getUserId());
        stringBuilder.append(":");
        stringBuilder.append(resources.getSetupId());
    }

    public void getSongs(View view) {
        namesList.clear();
        new SongTask(this).execute();
        if (resources.getPlayType().equals(PlayType.FAMILY.label)) {
            Log.i(TAG, "getSongs: writing, GOD HELP");
            bluetoothConnectionService.write(stringBuilder.toString().getBytes());
        }
    }

    public void getUrls(View view) {
        urls.clear();
        String[] names = new String[namesList.size()];
        names = namesList.toArray(names);
        DownloadTask task = new DownloadTask(urls);
        task.execute(names);
    }

    public void getRoom(View view) {
        new RoomTask(this).execute();
    }

    public void getPlaylist(View view) {
        new PlaylistTask(this, setup.get().getRooms().get(0)).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void playBtn(View view) {
        urls.forEach(url -> Log.i(TAG, url));
        Intent intent = new Intent(InitDataActivity.this, PlayerActivity.class);
        intent.putExtra("playlist", (Serializable) urls);
        startActivity(intent);
    }

    private static class SongTask extends AsyncTask<Void, Void, Void> {
        WeakReference<InitDataActivity> weakReference;

        SongTask(InitDataActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            InitDataActivity activity = weakReference.get();

            activity.firebaseHandler.getPlaylistSongsNames(activity.playlistId.get(), activity.namesList);
            return null;
        }
    }

    private static class RoomTask extends AsyncTask<Void, Void, Void> {
        WeakReference<InitDataActivity> weakReference;

        RoomTask(InitDataActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            InitDataActivity activity = weakReference.get();

            activity.firebaseHandler.getSetup(activity.resources.getSetupId(), activity.setup);
            return null;
        }
    }

    private static class PlaylistTask extends AsyncTask<Void, Void, Void> {
        WeakReference<InitDataActivity> weakReference;
        String id;

        PlaylistTask(InitDataActivity activity, final String id) {
            weakReference = new WeakReference<>(activity);
            this.id = id;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            InitDataActivity activity = weakReference.get();

            activity.firebaseHandler.getRoomPlaylistId(id, activity.playlistId);
            return null;
        }
    }
}
