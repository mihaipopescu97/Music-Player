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

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.ManageDeviceActivity;
import com.example.mplayer.structure.body.management.activities.ManagePlaylistActivity;
import com.example.mplayer.structure.body.management.activities.ManageSetupActivity;
import com.example.mplayer.structure.login.MainActivity;
import com.example.mplayer.structure.player.PlayerActivity;
import com.example.mplayer.utils.FirebaseHandler;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private FirebaseAuth firebaseAuth;
    final FirebaseHandler firebaseHandler = FirebaseHandler.getInstance();

//    private String deviceId;
//    private String setupId;
//    private String roomId;
    private AtomicReference<String> email = new AtomicReference<>();
    private AtomicReference<String> userId = new AtomicReference<>();

    private Button logOutBtn;
    private Button managePlayliststBtn;
    private Button manageSetupsBtn;
    private Button manageDevicesBtn;
    private Button playBtn;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Log.i(TAG, "Base activity started");

        firebaseAuth = FirebaseAuth.getInstance();

        logOutBtn = findViewById(R.id.logOutBtn);
        manageDevicesBtn = findViewById(R.id.deviceBtn);
        managePlayliststBtn = findViewById(R.id.playlistBtn);
        manageSetupsBtn = findViewById(R.id.setupBtn);
        playBtn = findViewById(R.id.playBtn);

        //Initially, buttons are gone
//        List<Button> list = Arrays.asList(manageDevicesBtn, managePlayliststBtn,
//                manageSetupsBtn, playBtn);
//        list.forEach(button -> button.setVisibility(View.GONE));
//
//        thread = new Thread(() -> {
//            Intent intent = getIntent();
//            email.set(intent.getStringExtra("email"));
//            firebaseHandler.getUserId(email.get(), userId);
//        });

        new BackgroundTasks(this).execute();
//        deviceId = getIntent().getStringExtra("deviceId");
//        roomId = getIntent().getStringExtra("roomId");
//        setupId = getIntent().getStringExtra("setupId");


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        thread.start();
//
//        while(true) {
//            if (thread.isAlive()) {
//                Log.w(TAG, "Thread not done reading");
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Log.i(TAG, "Received email:" + email.get());
//                logOutBtn.setVisibility(View.VISIBLE);
//                manageDevicesBtn.setVisibility(View.VISIBLE);
//                break;
//            }
//        }
//    }

    public void manageDevice(View view) {
        Log.i(TAG, "Sending user:" + userId.get());
        Intent intent = new Intent(BaseActivity.this, ManageDeviceActivity.class);
        intent.putExtra("userId", userId.toString());
        startActivity(intent);
    }

    public void manageSetups(View view) {
        Intent intent = new Intent(BaseActivity.this, ManageSetupActivity.class);
        intent.putExtra("deviceId", deviceId);
        startActivity(intent);
    }

    public void managePlaylist(View view) {
        Intent intent = new Intent(BaseActivity.this, ManagePlaylistActivity.class);
        intent.putExtra("roomId", roomId);
        startActivity(intent);
    }

    public void play(View view) {
        Intent intent = new Intent(BaseActivity.this, PlayerActivity.class);
        intent.putExtra("setupId", setupId);
        startActivity(intent);
    }

    public void logOutFromBase(View view) {
        firebaseAuth.signOut();
        startActivity(new Intent(BaseActivity.this, MainActivity.class));
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

            List<Button> list = Arrays.asList(activity.manageDevicesBtn, activity.managePlayliststBtn,
                    activity.manageSetupsBtn, activity.playBtn);
            list.forEach(button -> button.setVisibility(View.GONE));
        }

        @Override
        protected Void doInBackground(Void... voids) {

            BaseActivity activity = weakReference.get();
            Intent intent = activity.getIntent();
            activity.email.set(intent.getStringExtra("email"));
            activity.firebaseHandler.getUserId(activity.email.get(), activity.userId);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            BaseActivity activity = weakReference.get();
            activity.manageDevicesBtn.setVisibility(View.VISIBLE);
            activity.playBtn.setVisibility(View.VISIBLE);
        }
    }
}
