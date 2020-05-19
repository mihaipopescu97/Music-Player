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
import com.example.mplayer.structure.player.BluetoothActivity;
import com.example.mplayer.structure.player.InitDataActivity;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;
import com.example.mplayer.utils.enums.PlayType;


//FROZEN
public class BaseActivity extends AppCompatActivity {

    private SharedResources resources;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        String TAG = "BaseActivity";
        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        resources = SharedResources.getInstance();
    }

    public void createNewSetup(View view) {
        startActivity( new Intent(getBaseContext(), NewBuildActivity.class));
    }

    public void useSetup(View view) {
        Intent intent = new Intent(getBaseContext(), SetupSelectActivity.class);
        intent.putExtra("prevActivity", this.getClass());
        startActivity(intent);
    }

    public void manageSettings(View view) {
        startActivity(new Intent(getBaseContext(), SettingsActivity.class));
    }

    public void play(View view) {
        if(resources.getSetupId() != null) {
            if (resources.getPlayType().equals(PlayType.FAMILY.label)) {
                startActivity(new Intent(BaseActivity.this, BluetoothActivity.class));
            } else {
                startActivity(new Intent(getBaseContext(), InitDataActivity.class));
            }

        }
    }

    public void backBase(View view) {
        startActivity( new Intent(BaseActivity.this, SelectActivity.class));
    }

}
