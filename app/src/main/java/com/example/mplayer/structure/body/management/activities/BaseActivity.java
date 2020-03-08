package com.example.mplayer.structure.body.management.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.setups.SetupSelectActivity;
import com.example.mplayer.structure.player.PlayerActivity;
//import com.example.mplayer.utils.BluetoothSender;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.io.IOException;

//FROZEN
public class BaseActivity extends AppCompatActivity {

    private SharedResources resources;
    //private BluetoothSender bluetoothSender;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        String TAG = "BaseActivity";
        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        resources = SharedResources.getInstance();
//        try {
////            bluetoothSender = BluetoothSender.getInstance();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        resources.resetPlayType();
    }

    public void createNewSetup(View view) {
        startActivity( new Intent(getBaseContext(), NewBuildActivity.class));
    }

    public void useSetup(View view) {
        startActivity(new Intent(getBaseContext(), SetupSelectActivity.class));
    }

    public void manageSettings(View view) {
        startActivity(new Intent(getBaseContext(), SettingsActivity.class));
    }

    public void play(View view) {
        if(resources.getSetupId() != null && resources.getSetupId().isEmpty()) {
         //   try {
              //  bluetoothSender.write(resources.getSetupId());
                startActivity(new Intent(BaseActivity.this, PlayerActivity.class));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void backBase(View view) {
        startActivity( new Intent(BaseActivity.this, SelectActivity.class));
    }

}
