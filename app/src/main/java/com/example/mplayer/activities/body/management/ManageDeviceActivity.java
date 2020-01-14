package com.example.mplayer.activities.body.management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.mplayer.R;
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

        Log.d(TAG, "Manage device activity started");

        deviceSectionAdapter = new DeviceSectionAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);
        setupViewPage(viewPager);
    }

    private void setupViewPage(ViewPager viewPager) {
        DeviceSectionAdapter adapter = new DeviceSectionAdapter(getSupportFragmentManager());
        adapter.addFragment(new SetupFragment(), "SetupFragment");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }
}
