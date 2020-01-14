package com.example.mplayer.activities.body.management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.mplayer.R;
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

        Log.d(TAG, "Manage setups activity started");

        setupsSectionAdapter = new SetupSectionAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);
        setupViewPage(viewPager);
    }

    private void setupViewPage(ViewPager viewPager) {
        SetupSectionAdapter adapter = new SetupSectionAdapter(getSupportFragmentManager());
        adapter.addFragment(new SetupFragment(), "SetupsFragment");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }
}
