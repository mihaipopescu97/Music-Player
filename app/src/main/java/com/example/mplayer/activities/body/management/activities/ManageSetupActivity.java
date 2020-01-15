package com.example.mplayer.activities.body.management.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.mplayer.R;
import com.example.mplayer.activities.body.management.fragments.setups.SetupAddFragment;
import com.example.mplayer.activities.body.management.fragments.setups.SetupDeleteFragment;
import com.example.mplayer.activities.body.management.fragments.setups.SetupHomeFragment;
import com.example.mplayer.activities.body.management.fragments.setups.SetupSelectFragment;
import com.example.mplayer.adapters.fragments.PlaylistSectionAdapter;
import com.example.mplayer.adapters.fragments.SetupSectionAdapter;
import com.example.mplayer.fragments.SetupFragment;

public class ManageSetupActivity extends AppCompatActivity {

    private static final String TAG = "ManageSetupActivity";

    private SetupSectionAdapter setupsSectionAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_setups);

        Log.i(TAG, "Manage setups activity started");

        setupsSectionAdapter = new SetupSectionAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);
        setupViewPage(viewPager);

        //TODO get device id
        final String deviceId = "";
        Bundle bundle = new Bundle();
        bundle.putString("deviceId", deviceId);
    }

    private void setupViewPage(ViewPager viewPager) {
        SetupSectionAdapter adapter = new SetupSectionAdapter(getSupportFragmentManager());

        Log.d(TAG, "Setup home -> 0");
        adapter.addFragment(new SetupHomeFragment(), "SetupHomeFragment");

        Log.d(TAG, "Setup select -> 1");
        adapter.addFragment(new SetupSelectFragment(), "SetupSelectFragment");

        Log.d(TAG, "Setup add -> 2");
        adapter.addFragment(new SetupAddFragment(), "SetupAddFragment");

        Log.d(TAG, "Setup delete -> 3");
        adapter.addFragment(new SetupDeleteFragment(), "SetupDeleteFragment");

        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }
}
