package com.example.mplayer.structure.body.management.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.BaseActivity;
import com.example.mplayer.structure.body.management.fragments.devices.DeviceAddFragment;
import com.example.mplayer.structure.body.management.fragments.devices.DeviceDeleteFragment;
import com.example.mplayer.structure.body.management.fragments.devices.DeviceHomeFragment;
import com.example.mplayer.structure.body.management.fragments.devices.DeviceSelectFragment;
import com.example.mplayer.adapters.fragments.DeviceSectionAdapter;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

public class ManageDeviceActivity extends AppCompatActivity {

    private static final String TAG = "ManageDeviceActivity";

    private ViewPager viewPager;
    private Thread userIdThread, uiThread;
    private final AtomicReference<String> userId = new AtomicReference<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_device);

        Log.i(TAG, "Manage device activity started");

//        userIdThread = new Thread(() -> {
//            Intent intent = getIntent();
//            userId.set(intent.getStringExtra("userId"));
//        });
//
//        uiThread = new Thread(() -> {
//            viewPager = findViewById(R.id.deviceContainer);
//            setupViewPage(viewPager);
//            viewPager.setCurrentItem(0);
//        });
        new BackgroundTasks(this).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        userIdThread.start();
//        uiThread.start();
//
//        while(userIdThread.isAlive()) {
//            Log.d(TAG, "Waiting for thread to read");
//        }
//
//        Log.i(TAG, "Got user:" + userId.get());
//        Bundle bundle = new Bundle();
//        bundle.putString("user", userId.get());
//
//        //TODO may need check
//        DeviceHomeFragment deviceHomeFragment = new DeviceHomeFragment();
//        deviceHomeFragment.setArguments(bundle);
    }

    private synchronized void setupViewPage(ViewPager viewPager) {
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

    public synchronized void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }

    private static class BackgroundTasks extends AsyncTask<Void, Void, Void> {

        private WeakReference<ManageDeviceActivity> weakReference;

        public BackgroundTasks(ManageDeviceActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }



        @Override
        protected Void doInBackground(Void... voids) {
            ManageDeviceActivity activity = weakReference.get();
            Intent intent = activity.getIntent();
            activity.userId.set(intent.getStringExtra("userId"));

            activity.setupViewPage(activity.viewPager);

            Bundle bundle = new Bundle();
            bundle.putString("user", activity.userId.get());

            DeviceHomeFragment deviceHomeFragment = new DeviceHomeFragment();
            deviceHomeFragment.setArguments(bundle);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
