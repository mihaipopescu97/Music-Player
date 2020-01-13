package com.example.mplayer.activities.body.management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.mplayer.R;
import com.example.mplayer.adapters.fragments.SetupsSectionAdapter;

public class ManageSetupsActivity extends AppCompatActivity {

    private static final String TAG = "ManageSetupsActivity";

    private SetupsSectionAdapter setupsSectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_setups);

        Log.d(TAG, "Manage setups activity started");

        setupsSectionAdapter = new SetupsSectionAdapter(getSupportFragmentManager());
    }
}
