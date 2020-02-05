package com.example.mplayer.structure.body.management.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SettingsActivity extends AppCompatActivity {

    private Button manageDevicesBtn;
    private Button manageSetupsBtn;
    private Button managePlaylistBtn;
    private Button backSettingsBtn;

    private AtomicReference<String> userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        manageDevicesBtn = findViewById(R.id.manageDevicesBtn);
        manageSetupsBtn = findViewById(R.id.manageSetupsBtn);
        managePlaylistBtn = findViewById(R.id.managePlaylistBtn);
        backSettingsBtn = findViewById(R.id.backSettingsBtn);

        userId = new AtomicReference<>();

        new BackgroundTask(this).execute();
    }

    public void manageDevices(View view) {
        if(!userId.get().isEmpty()) {
            Intent intent = new Intent(SettingsActivity.this, ManageDeviceActivity.class);
            intent.putExtra("userId", userId.get())
                    .putExtra("prevActivity", getBaseContext().toString())
                    .putExtra("type", "all");
            startActivity(intent);
        }
    }

    public void manageSetups(View view) {
        if(!userId.get().isEmpty()) {
            Intent intent = new Intent(SettingsActivity.this, ManageSetupActivity.class);
            intent.putExtra("userId", userId.get())
                    .putExtra("prevActivity", getBaseContext().toString()).putExtra("type", "ALL");
            startActivity(intent);
        }
    }

    public void managePlaylist(View view) {
        if(!userId.get().isEmpty()) {
            Intent intent = new Intent(SettingsActivity.this, ManagePlaylistActivity.class);
            intent.putExtra("userId", userId.get()).putExtra("prevActivity", getBaseContext().toString());
            startActivity(intent);
        }
    }

    public void backSettings(View view) {
        Intent intent = new Intent(SettingsActivity.this, BaseActivity.class);
        startActivity(intent);
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

            Intent intent = activity.getIntent();
            activity.userId.set(intent.getStringExtra("userId"));

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            SettingsActivity activity = weakReference.get();

            List<Button> list = Arrays.asList(
                    activity.manageDevicesBtn,
                    activity.manageSetupsBtn,
                    activity.managePlaylistBtn,
                    activity.backSettingsBtn);
            list.forEach(button -> button.setVisibility(View.VISIBLE));
        }
    }
}
