package com.example.mplayer.structure.body;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private FirebaseAuth firebaseAuth;
    private FirebaseHandler firebaseHandler;

    private String userId;
    private String deviceId;
    private String setupId;
    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Log.i(TAG, "Base activity started");

        Button deviceBtn = findViewById();
        Button setupBtn = findViewById();
        Button playlistBtn = findViewById();
        Button playBtn = findViewById();
        Button logOutBtn = findViewById();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseHandler = FirebaseHandler.getInstance();

        final String email = getIntent().getStringExtra("email");
        userId = firebaseHandler.getUserId(email);

        deviceId = getIntent().getStringExtra("deviceId");
        roomId = getIntent().getStringExtra("roomId");
        setupId = getIntent().getStringExtra("setupId");


    }

    public void manageDevice(View view) {
        Intent intent = new Intent(BaseActivity.this, ManageDeviceActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public void manageSetups(View view) {
        Intent intent = new Intent(BaseActivity.this, ManageSetupActivity.class);
        intent.putExtra("deviceId", deviceId);
        startActivity(intent);
    }

    public void managePlaylist(View view) {
        Intent intent = new Intent(BaseActivity.this, ManagePlaylistActivity.class)
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
}
