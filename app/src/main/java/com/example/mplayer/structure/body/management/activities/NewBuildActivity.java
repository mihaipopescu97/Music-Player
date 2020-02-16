package com.example.mplayer.structure.body.management.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.fragments.devices.DeviceAddActivity;
import com.example.mplayer.structure.body.management.fragments.playlists.PlaylistAddActivity;
import com.example.mplayer.structure.body.management.fragments.setups.SetupAddActivity;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

//TODO needs testing before freeze
public class NewBuildActivity extends AppCompatActivity {

    private final String TAG = "NewBuildActivity";
    private AtomicReference<Class> prevActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_build);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        prevActivity = new AtomicReference<>();
        new BackgroundTask(this).execute();
    }

    public void addNewDevice(View view) {
        Intent intent = new Intent(getBaseContext(), DeviceAddActivity.class);
        intent.putExtra("prevActivity", getBaseContext().toString());
        startActivity(intent);
    }

    public void addNewSetup(View view) {
            Intent intent = new Intent(getBaseContext(), SetupAddActivity.class);
            intent.putExtra("prevActivity", getBaseContext().toString());
            startActivity(intent);
    }

    public void addNewPlaylist(View view) {
        Intent intent = new Intent(getBaseContext(), PlaylistAddActivity.class);
        intent.putExtra("prevActivity", getBaseContext().toString());
        startActivity(intent);
    }

    //Get previous activity and play type
    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<NewBuildActivity> weakReference;

        BackgroundTask(NewBuildActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            NewBuildActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_START.label);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            NewBuildActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);
            Intent intent = activity.getIntent();
            activity.prevActivity.set((Class) intent.getExtras().get("prevActivity"));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            NewBuildActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_END.label);
        }
    }
}
