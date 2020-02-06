package com.example.mplayer.structure.body.management.activities.settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mplayer.R;
import com.example.mplayer.utils.enums.Actions;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

//TODO test then freeze
public class SettingsActivity extends AppCompatActivity {

    private final String TAG = "SettingsActivity";

    private Button manageDevicesBtn;
    private Button manageSetupsBtn;
    private Button managePlaylistBtn;
    private Button backSettingsBtn;

    private AtomicReference<String> userId;
    private AtomicReference<String> prevActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        manageDevicesBtn = findViewById(R.id.manageDevicesBtn);
        manageSetupsBtn = findViewById(R.id.manageSetupsBtn);
        managePlaylistBtn = findViewById(R.id.managePlaylistBtn);
        backSettingsBtn = findViewById(R.id.backSettingsBtn);

        userId = new AtomicReference<>();
        prevActivity = new AtomicReference<>();

        new BackgroundTask(this).execute();
    }

    public void manageDevices(View view) {
        if(!userId.get().isEmpty()) {
            Intent intent = new Intent(SettingsActivity.this, DeviceSettingsActivity.class);
            intent.putExtra("userId", userId.get())
                    .putExtra("prevActivity", getBaseContext().toString())
                    .putExtra("type", Actions.ALL.label);
            startActivity(intent);
        } else {
            Log.e(TAG, LogMessages.USER_FETCH_ERROR.label);
        }
    }

    public void manageSetups(View view) {
        if(!userId.get().isEmpty()) {
            Intent intent = new Intent(SettingsActivity.this, SetupSettingsActivity.class);
            intent.putExtra("userId", userId.get())
                    .putExtra("prevActivity", getBaseContext().toString())
                    .putExtra("type", Actions.ALL.label);
            startActivity(intent);
        } else {
            Log.e(TAG, LogMessages.USER_FETCH_ERROR.label);
        }
    }

    public void managePlaylist(View view) {
        if(!userId.get().isEmpty()) {
            Intent intent = new Intent(SettingsActivity.this, PlaylistSettingsActivity.class);
            intent.putExtra("userId", userId.get())
                    .putExtra("prevActivity", getBaseContext().toString()).putExtra("type", Actions.ALL.label);
            startActivity(intent);
        } else {
            Log.e(TAG, LogMessages.USER_FETCH_ERROR.label);
        }
    }

    public void backSettings(View view) {
        //TODO test this specific thing
        Intent intent;
        try {
            intent = new Intent(SettingsActivity.this, Class.forName(prevActivity.get()));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<SettingsActivity> weakReference;

        BackgroundTask(SettingsActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SettingsActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_START.label);
            List<Button> list = Arrays.asList(
                    activity.manageDevicesBtn,
                    activity.manageSetupsBtn,
                    activity.managePlaylistBtn,
                    activity.backSettingsBtn);
            list.forEach(button -> button.setVisibility(View.GONE));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SettingsActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);
            Intent intent = activity.getIntent();
            activity.userId.set(intent.getStringExtra("userId"));
            activity.prevActivity.set(intent.getStringExtra("prevActivity"));

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            SettingsActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_END.label);
            List<Button> list = Arrays.asList(
                    activity.manageDevicesBtn,
                    activity.manageSetupsBtn,
                    activity.managePlaylistBtn,
                    activity.backSettingsBtn);
            list.forEach(button -> button.setVisibility(View.VISIBLE));
        }
    }
}
