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
import com.example.mplayer.structure.body.management.fragments.setups.SetupAddFragment;
import com.example.mplayer.structure.body.management.fragments.setups.SetupDeleteFragment;
import com.example.mplayer.structure.body.management.fragments.setups.SetupHomeFragment;
import com.example.mplayer.structure.body.management.fragments.setups.SetupSelectFragment;
import com.example.mplayer.adapters.fragments.FragmentSectionAdapter;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

//TODO test then freeze
public class SetupSettingsActivity extends AppCompatActivity {

    private final String TAG = "SetupSettingsActivity";


    private ViewPager viewPager;

    private AtomicReference<String> userId;
    private AtomicReference<String> prevActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_setups);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        viewPager = findViewById(R.id.setupsContainer);
        setupViewPage(viewPager);
        viewPager.setCurrentItem(0);

        userId = new AtomicReference<>();
        prevActivity = new AtomicReference<>();

       new BackgroundTask(this).execute();
    }

    private void setupViewPage(ViewPager viewPager) {
        FragmentSectionAdapter adapter = new FragmentSectionAdapter(getSupportFragmentManager());

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

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<SetupSettingsActivity> weakReference;

        BackgroundTask(SetupSettingsActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SetupSettingsActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_START.label);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {

            SetupSettingsActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);
            Intent intent = activity.getIntent();
            activity.userId.set(intent.getStringExtra("userId"));
            activity.prevActivity.set(intent.getStringExtra("prevActivity"));

            Bundle bundle = new Bundle();
            bundle.putString("userId", activity.userId.get());
            bundle.putString("prevActivity", activity.prevActivity.get());

            List<Fragment> list = Arrays.asList(
                    new SetupAddFragment(),
                    new SetupDeleteFragment(),
                    new SetupHomeFragment());
            list.forEach(fragment -> fragment.setArguments(bundle));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            SetupSettingsActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_END.label);
        }
    }
}
