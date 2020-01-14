package com.example.mplayer.activities.body;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mplayer.R;
import com.example.mplayer.activities.body.management.ManageDeviceActivity;
import com.example.mplayer.activities.body.management.ManagePlaylistActivity;
import com.example.mplayer.activities.body.management.ManageSetupActivity;
import com.example.mplayer.activities.login.MainActivity;
import com.example.mplayer.activities.player.PlayerActivity;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Log.d(TAG, "Base activity started");

        Button deviceBtn = findViewById();
        Button setupBtn = findViewById();
        Button playlistBtn = findViewById();
        Button playBtn = findViewById();
        Button logOutBtn = findViewById();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void manageDevice(View view) {
        startActivity(new Intent(BaseActivity.this, ManageDeviceActivity.class));
    }

    public void manageSetups(View view) {
        startActivity(new Intent(BaseActivity.this, ManageSetupActivity.class));
    }

    public void managePlaylist(View view) {
        startActivity(new Intent(BaseActivity.this, ManagePlaylistActivity.class));
    }

    public void play(View view) {
        startActivity(new Intent(BaseActivity.this, PlayerActivity.class));
    }

    public void logOutFromBase(View view) {
        firebaseAuth.signOut();
        startActivity(new Intent(BaseActivity.this, MainActivity.class));
    }
}
