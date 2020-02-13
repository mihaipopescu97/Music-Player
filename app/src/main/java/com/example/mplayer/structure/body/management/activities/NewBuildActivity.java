package com.example.mplayer.structure.body.management.activities;

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
import com.example.mplayer.structure.body.management.fragments.devices.DeviceAddFragment;
import com.example.mplayer.structure.body.management.fragments.playlists.PlaylistAddFragment;
import com.example.mplayer.structure.body.management.fragments.setups.SetupAddFragment;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

//TODO needs testing before freeze
public class NewBuildActivity extends AppCompatActivity {

    private final String TAG = "NewBuildActivity";

    private ViewPager viewPager;

    private AtomicReference<String> userId;
    private AtomicReference<Class> prevActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_build);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        userId = new AtomicReference<>();
        prevActivity = new AtomicReference<>();

        viewPager = findViewById(R.id.setupsContainer);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(0);

        new BackgroundTask(this).execute();
    }

    private  void setupViewPager(ViewPager viewPager) {
        FragmentSectionAdapter adapter = new FragmentSectionAdapter(getSupportFragmentManager());

        Log.d(TAG, "Device create -> 0");
        adapter.addFragment(new DeviceAddFragment(), "DeviceAddFragment");

        Log.d(TAG, "Setup create -> 1");
        adapter.addFragment(new SetupAddFragment(), "DeviceSelectFragment");

        Log.d(TAG, "Playlist create -> 2");
        adapter.addFragment(new PlaylistAddFragment(), "DeviceAddFragment");

        viewPager.setAdapter(adapter);
    }

    public synchronized void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }


    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<NewBuildActivity> weakReference;

        BackgroundTask(NewBuildActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            NewBuildActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_START.label);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {

            NewBuildActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);
            Intent intent = activity.getIntent();
            activity.userId.set(intent.getStringExtra("userId"));
            activity.prevActivity.set((Class) intent.getExtras().get("prevActivity"));

            Bundle bundle = new Bundle();
            bundle.putString("userId", activity.userId.get());
            bundle.putString("prevActivity", activity.prevActivity.get().toString());

            List<Fragment> list = Arrays.asList(
                    new DeviceAddFragment(),
                    new SetupAddFragment(),
                    new PlaylistAddFragment());
            list.forEach(fragment -> fragment.setArguments(bundle));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            NewBuildActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_END.label);
        }
    }
}
