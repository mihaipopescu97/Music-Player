package com.example.mplayer.structure.body;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.ManageDeviceActivity;
import com.example.mplayer.structure.body.management.activities.ManageSetupActivity;
import com.example.mplayer.structure.body.management.activities.SettingsActivity;
import com.example.mplayer.structure.player.PlayerActivity;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SingleActivity extends AppCompatActivity {

    private final String TAG = "SingleActivity";

    private AtomicReference<String> userId;
    private AtomicReference<String> setupId;
    private AtomicReference<String> prevActivity;

    private Button createNewSetupBtn;
    private Button useSetupBtn;
    private Button manageSettingsBtn;
    private Button playBtn;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        Log.i(TAG, "Base activity started");

        createNewSetupBtn = findViewById(R.id.createNewSetupBtn);
        useSetupBtn = findViewById(R.id.useSetupBtn);
        manageSettingsBtn = findViewById(R.id.manageSettingsBtn);
        playBtn = findViewById(R.id.playBtn);

        userId = new AtomicReference<>();
        setupId = new AtomicReference<>();
        prevActivity = new AtomicReference<>();

        new BackgroundTasks(this).execute();
    }


    public void singleCreateNewSetup(View view) {
        if(!userId.get().isEmpty()) {
            Log.i(TAG, "Sending user:" + userId.get());
            Intent intent = new Intent(SingleActivity.this, ManageDeviceActivity.class);
            intent.putExtra("userId", userId.get())
                    .putExtra("prevActivity", getBaseContext().toString())
                    .putExtra("type", "create");
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "User not received! Please login again", Toast.LENGTH_SHORT).show();
        }
    }

    public void singleUseSetup(View view) {
        if(!userId.get().isEmpty()) {
            Intent intent = new Intent(SingleActivity.this, ManageSetupActivity.class);
            intent.putExtra("userId", userId.get())
                    .putExtra("type", "select");
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "User not received! Please login again", Toast.LENGTH_SHORT).show();
        }
    }

    public void singleManageSettings(View view) {
        if(!userId.get().isEmpty()) {
            Intent intent = new Intent(SingleActivity.this, SettingsActivity.class);
            intent.putExtra("userId", userId.get());
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "User not received! Please login again", Toast.LENGTH_SHORT).show();
        }
    }

    public void singlePlay(View view) {
        if(!setupId.get().isEmpty()) {
            Intent intent = new Intent(SingleActivity.this, PlayerActivity.class);
            intent.putExtra("setupId", setupId.get());
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Setup not received! Please login again", Toast.LENGTH_SHORT).show();
        }
    }

    public void backSingle(View view) {
        //TODO test this approach
        try {
            startActivity(new Intent(SingleActivity.this, Class.forName(prevActivity.get())));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static class BackgroundTasks extends AsyncTask<Void, Void, Void> {
        WeakReference<SingleActivity> weakReference;

        BackgroundTasks(SingleActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SingleActivity activity = weakReference.get();

            Log.i(activity.TAG, "Async task started...");
            List<Button> list = Arrays.asList(
                    activity.createNewSetupBtn,
                    activity.useSetupBtn,
                    activity.manageSettingsBtn,
                    activity.playBtn);
            list.forEach(button -> button.setVisibility(View.GONE));
        }

        @Override
        protected Void doInBackground(Void... voids) {

            SingleActivity activity = weakReference.get();
            Intent intent = activity.getIntent();
            Log.d(activity.TAG, "Fetching userId and setupId if available");
            activity.userId.set(intent.getStringExtra("userId"));
            activity.setupId.set(intent.getStringExtra("setupId"));

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            SingleActivity activity = weakReference.get();
            Log.i(activity.TAG, "Async task ended");
            List<Button> list = Arrays.asList(
                    activity.createNewSetupBtn,
                    activity.useSetupBtn,
                    activity.manageSettingsBtn);
            list.forEach(button -> button.setVisibility(View.VISIBLE));

            if(!activity.setupId.get().isEmpty()) {
                activity.playBtn.setVisibility(View.VISIBLE);
            }
        }
    }
}
