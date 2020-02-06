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
import com.example.mplayer.structure.body.management.fragments.setups.SetupSelectFragment;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

//TODO needs testing before freeze
public class SelectBuildActivity extends AppCompatActivity {

    private final String TAG = "SelectBuildActivity";

    private ViewPager viewPager;
    private AtomicReference<String> userId;
    private AtomicReference<String> prevActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_setup);

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

        Log.d(TAG, "Setup select -> 0");
        adapter.addFragment(new SetupSelectFragment(), "DeviceAddFragment");

        viewPager.setAdapter(adapter);
    }

    public synchronized void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<SelectBuildActivity> weakReference;

        BackgroundTask(SelectBuildActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SelectBuildActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_START.label);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {

            SelectBuildActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);
            Intent intent = activity.getIntent();
            activity.userId.set(intent.getStringExtra("userId"));
            activity.prevActivity.set(intent.getStringExtra("prevActivity"));

            Bundle bundle = new Bundle();
            bundle.putString("userId", activity.userId.get());
            bundle.putString("prevActivity", activity.prevActivity.get());

            List<Fragment> list = Collections.singletonList(
                    new SetupSelectFragment());
            list.forEach(fragment -> fragment.setArguments(bundle));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            SelectBuildActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_END.label);
        }
    }
}
