package com.example.mplayer.activities.body.management.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.mplayer.R;
import com.example.mplayer.activities.body.management.fragments.devices.DeviceAddFragment;
import com.example.mplayer.activities.body.management.fragments.devices.DeviceDeleteFragment;
import com.example.mplayer.activities.body.management.fragments.devices.DeviceHomeFragment;
import com.example.mplayer.activities.body.management.fragments.devices.DeviceSelectFragment;
import com.example.mplayer.activities.body.management.fragments.setups.SetupHomeFragment;
import com.example.mplayer.adapters.fragments.DeviceSectionAdapter;
import com.example.mplayer.fragments.SetupFragment;

public class ManageDeviceActivity extends AppCompatActivity {

    private static final String TAG = "ManageDeviceActivity";

    private DeviceSectionAdapter deviceSectionAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_device);

        Log.i(TAG, "Manage device activity started");

        deviceSectionAdapter = new DeviceSectionAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);
        setupViewPage(viewPager);

        //TODO get user id
        final String userId = "";
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
    }

    private void setupViewPage(ViewPager viewPager) {
        DeviceSectionAdapter adapter = new DeviceSectionAdapter(getSupportFragmentManager());

        Log.d(TAG, "Device home -> 0");
        adapter.addFragment(new DeviceHomeFragment(), "DeviceHomeFragment");

        Log.d(TAG, "Device select -> 1");
        adapter.addFragment(new DeviceSelectFragment(), "DeviceSelectFragment");

        Log.d(TAG, "Device add -> 2");
        adapter.addFragment(new DeviceAddFragment(), "DeviceAddFragment");

        Log.d(TAG, "Device delete -> 3");
        adapter.addFragment(new DeviceDeleteFragment(), "DeviceDeleteFragment");

        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }
}
