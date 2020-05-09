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
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;
import com.example.mplayer.utils.helpers.DownloadTask;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitDataActivity extends AppCompatActivity {
    private static final String TAG = "InitDataActivity";
    private FirebaseHandler firebaseHandler;
    private SharedResources resources;
    private List<String> urls;
    private List<String> namesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_data);
        Log.i(TAG ,LogMessages.ACTIVITY_START.label);
        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();
        urls = Collections.synchronizedList(new ArrayList<>());
        namesList = Collections.synchronizedList(new ArrayList<>());
    }

    public void getDataBtn(View view) {
        namesList.clear();
        new BackgroundTask(this).execute();
    }

    public void getUrls(View view) {
        urls.clear();
        String[] names = new String[namesList.size()];
        names = namesList.toArray(names);
        DownloadTask task = new DownloadTask(urls);
        task.execute(names);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void playBtn(View view) {
        urls.forEach(url -> Log.i(TAG, url));
        Intent intent = new Intent(InitDataActivity.this, PlayerActivity.class);
        intent.putExtra("playlist", (Serializable) urls);
        startActivity(intent);
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<InitDataActivity> weakReference;




        BackgroundTask(InitDataActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            InitDataActivity activity = weakReference.get();

            activity.firebaseHandler.getPlaylistSongsNames(activity.resources.getPlaylistId(), activity.namesList);
            return null;
        }
    }
}
