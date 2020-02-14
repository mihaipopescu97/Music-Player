package com.example.mplayer.structure.body.management.activities.settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.mplayer.R;
import com.example.mplayer.adapters.fragments.FragmentSectionAdapter;
import com.example.mplayer.structure.body.management.fragments.devices.DeviceAddActivity;
import com.example.mplayer.structure.body.management.fragments.devices.DeviceDeleteFragment;
import com.example.mplayer.structure.body.management.fragments.devices.DeviceHomeFragment;
import com.example.mplayer.structure.body.management.fragments.devices.DeviceSelectFragment;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

//TODO test then freeze
public class DeviceSettingsActivity extends AppCompatActivity {

    private final String TAG = "DeviceSettingsActivity";

    private ViewPager viewPager;
    private AtomicReference<String> userId;
    private AtomicReference<Class> prevActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_device);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        userId = new AtomicReference<>();
        prevActivity = new AtomicReference<>();

        viewPager = findViewById(R.id.setupsContainer);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(0);

        new BackgroundTasks(this).execute();
    }


    private  void setupViewPager(ViewPager viewPager) {
        FragmentSectionAdapter adapter = new FragmentSectionAdapter(getSupportFragmentManager());

        Log.d(TAG, "Device home -> 0");
        adapter.addFragment(new DeviceHomeFragment(), "DeviceHomeFragment");

        Log.d(TAG, "Device select -> 1");
        adapter.addFragment(new DeviceSelectFragment(), "DeviceSelectFragment");

        Log.d(TAG, "Device add -> 2");
        adapter.addFragment(new DeviceAddActivity(), "DeviceAddActivity");

        Log.d(TAG, "Device delete -> 3");
        adapter.addFragment(new DeviceDeleteFragment(), "DeviceDeleteFragment");

        viewPager.setAdapter(adapter);
    }

    public synchronized void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }

    private static class BackgroundTasks extends AsyncTask<Void, Void, Void> {

        private WeakReference<DeviceSettingsActivity> weakReference;

        BackgroundTasks(DeviceSettingsActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            DeviceSettingsActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_START.label);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            DeviceSettingsActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);
            Intent intent = activity.getIntent();
            activity.userId.set(intent.getStringExtra("userId"));
            activity.prevActivity.set((Class)intent.getExtras().get("prevActivity"));

            Bundle userBundle = new Bundle();
            userBundle.putString("user", activity.userId.get());

            Bundle activityBundle = new Bundle();
            activityBundle.putString("prevActivity", activity.prevActivity.get().toString());

            List<Fragment> fragments = Arrays.asList(
                    new DeviceAddActivity(),
                    new DeviceDeleteFragment());
            fragments.forEach(fragment -> fragment.setArguments(userBundle));

            DeviceHomeFragment deviceHomeFragment = new DeviceHomeFragment();
            deviceHomeFragment.setArguments(activityBundle);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            DeviceSettingsActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_END.label);
        }
    }
}
