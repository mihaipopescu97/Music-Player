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
import com.example.mplayer.utils.enums.Actions;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BaseActivity extends AppCompatActivity {

    private final String TAG = "BaseActivity";

    private AtomicReference<String> userId;
    private AtomicReference<String> setupId;
    private AtomicReference<String> prevActivity;
    private AtomicReference<String> playType;

    private Button createNewSetupBtn;
    private Button useSetupBtn;
    private Button manageSettingsBtn;
    private Button playBtn;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        createNewSetupBtn = findViewById(R.id.createNewSetupBtn);
        useSetupBtn = findViewById(R.id.useSetupBtn);
        manageSettingsBtn = findViewById(R.id.manageSettingsBtn);
        playBtn = findViewById(R.id.playBtn);

        userId = new AtomicReference<>();
        setupId = new AtomicReference<>();
        prevActivity = new AtomicReference<>();
        playType = new AtomicReference<>();

        new BackgroundTasks(this).execute();
    }


    public void createNewSetup(View view) {
        if(!userId.get().isEmpty()) {
            Log.i(TAG, "Sending user:" + userId.get());
            Intent intent = new Intent(BaseActivity.this, ManageDeviceActivity.class);
            intent.putExtra("userId", userId.get())
                    .putExtra("prevActivity", getBaseContext().toString())
                    .putExtra("type", Actions.CREATE);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "User not received! Please login again", Toast.LENGTH_SHORT).show();
        }
    }

    public void useSetup(View view) {
        if(!userId.get().isEmpty()) {
            Intent intent = new Intent(BaseActivity.this, ManageSetupActivity.class);
            intent.putExtra("userId", userId.get())
                    .putExtra("type", Actions.SELECT);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "User not received! Please login again", Toast.LENGTH_SHORT).show();
        }
    }

    public void manageSettings(View view) {
        if(!userId.get().isEmpty()) {
            Intent intent = new Intent(BaseActivity.this, SettingsActivity.class);
            intent.putExtra("userId", userId.get());
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "User not received! Please login again", Toast.LENGTH_SHORT).show();
        }
    }

    public void play(View view) {
        if(!setupId.get().isEmpty()) {
            Intent intent = new Intent(BaseActivity.this, PlayerActivity.class);
            intent.putExtra("setupId", setupId.get())
                    .putExtra("playType", playType);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Setup not received! Please login again", Toast.LENGTH_SHORT).show();
        }
    }

    public void backSingle(View view) {
        //TODO test this approach
        try {
            startActivity(new Intent(BaseActivity.this, Class.forName(prevActivity.get())));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static class BackgroundTasks extends AsyncTask<Void, Void, Void> {
        WeakReference<BaseActivity> weakReference;

        BackgroundTasks(BaseActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            BaseActivity activity = weakReference.get();

            Log.i(activity.TAG, LogMessages.ASYNC_START.label);
            List<Button> list = Arrays.asList(
                    activity.createNewSetupBtn,
                    activity.useSetupBtn,
                    activity.manageSettingsBtn,
                    activity.playBtn);
            list.forEach(button -> button.setVisibility(View.GONE));
        }

        @Override
        protected Void doInBackground(Void... voids) {

            BaseActivity activity = weakReference.get();
            Intent intent = activity.getIntent();
            Log.d(activity.TAG, LogMessages.ASYNC_FETCH.label);
            activity.userId.set(intent.getStringExtra("userId"));
            activity.setupId.set(intent.getStringExtra("setupId"));
            activity.playType.set(intent.getStringExtra("playType"));

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            BaseActivity activity = weakReference.get();
            Log.i(activity.TAG, LogMessages.ASYNC_END.label);
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
