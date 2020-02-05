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

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class NewSetupActivity extends AppCompatActivity {

    private final String TAG = "NewSetupActivity";

    private ViewPager viewPager;
    private AtomicReference<String> userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_setup);

        Log.i(TAG, "Activity started");

        userId = new AtomicReference<>();

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
        private WeakReference<NewSetupActivity> weakReference;

        BackgroundTask(NewSetupActivity activity) {
            weakReference = new WeakReference<>(activity);
        }


        @Override
        protected Void doInBackground(Void... voids) {

            NewSetupActivity activity = weakReference.get();
            Intent intent = activity.getIntent();
            activity.userId.set(intent.getStringExtra("userId"));

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            NewSetupActivity activity = weakReference.get();

            Bundle bundle = new Bundle();
            bundle.putString("userId", activity.userId.get());

            List<Fragment> list = Arrays.asList(
                    new DeviceAddFragment(),
                    new SetupAddFragment(),
                    new PlaylistAddFragment());
            list.forEach(fragment -> fragment.setArguments(bundle));
        }
    }
}
