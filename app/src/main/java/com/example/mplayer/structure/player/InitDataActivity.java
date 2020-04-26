package com.example.mplayer.structure.player;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.mplayer.R;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;
import com.example.mplayer.utils.helpers.DownloadTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InitDataActivity extends AppCompatActivity {
    private static final String TAG = "InitDataActivity";
    private FirebaseHandler firebaseHandler;
    private SharedResources resources;
    private List<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_data);
        Log.i(TAG ,LogMessages.ACTIVITY_START.label);
        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();
        urls = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    protected void onStart() {
        super.onStart();
        urls.clear();
        new BackgroundTask(this).execute();
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<InitDataActivity> weakReference;
        List<String> namesList;



        BackgroundTask(InitDataActivity activity) {
            weakReference = new WeakReference<>(activity);

            namesList = Collections.synchronizedList(new ArrayList<>());
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            InitDataActivity activity = weakReference.get();

            activity.firebaseHandler.getPlaylistSongsNames(activity.resources.getPlaylistId(), namesList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            InitDataActivity activity = weakReference.get();
            String[] names = new String[namesList.size()];
            names = namesList.toArray(names);
            DownloadTask task = new DownloadTask(activity, activity.urls);
            task.execute(names);
        }

        private String[] add(final String[] array, final String element) {
            String[] newArray = Arrays.copyOf(array, array.length + 1);
            newArray[0] = element;
            System.arraycopy(array, 0, newArray, 1, array.length);
            return newArray;
        }
    }
}
