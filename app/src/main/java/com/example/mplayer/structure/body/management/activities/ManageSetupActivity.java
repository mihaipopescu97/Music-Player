package com.example.mplayer.structure.body.management.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.fragments.setups.SetupAddFragment;
import com.example.mplayer.structure.body.management.fragments.setups.SetupDeleteFragment;
import com.example.mplayer.structure.body.management.fragments.setups.SetupHomeFragment;
import com.example.mplayer.structure.body.management.fragments.setups.SetupSelectFragment;
import com.example.mplayer.adapters.fragments.SetupSectionAdapter;

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

        final String deviceId = getIntent().getStringExtra("deviceId");
        Bundle bundle = new Bundle();
        bundle.putString("deviceId", deviceId);

        SetupAddFragment setupAddFragment = new SetupAddFragment();
        setupAddFragment.setArguments(bundle);

        SetupDeleteFragment setupDeleteFragment = new SetupDeleteFragment();
        setupDeleteFragment.setArguments(bundle);

        SetupSelectFragment setupSelectFragment = new SetupSelectFragment();
        setupSelectFragment.setArguments(bundle);
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
