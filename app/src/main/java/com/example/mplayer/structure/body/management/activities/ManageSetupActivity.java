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
import com.example.mplayer.structure.body.management.fragments.setups.SetupAddFragment;
import com.example.mplayer.structure.body.management.fragments.setups.SetupDeleteFragment;
import com.example.mplayer.structure.body.management.fragments.setups.SetupHomeFragment;
import com.example.mplayer.structure.body.management.fragments.setups.SetupSelectFragment;
import com.example.mplayer.adapters.fragments.SetupSectionAdapter;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ManageSetupActivity extends AppCompatActivity {

    private static final String TAG = "ManageSetupActivity";


    private ViewPager viewPager;
    private AtomicReference<String> userId = new AtomicReference<>();
    private AtomicBoolean select = new AtomicBoolean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_setups);

        Log.i(TAG, "Manage setups activity started");

        viewPager = findViewById(R.id.setupsContainer);
        setupViewPage(viewPager);
        viewPager.setCurrentItem(0);

       new BackgroundTask(this).execute();
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

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<ManageSetupActivity> weakReference;

        BackgroundTask(ManageSetupActivity activity) {
            weakReference = new WeakReference<>(activity);
        }


        @Override
        protected Void doInBackground(Void... voids) {

            ManageSetupActivity activity = weakReference.get();
            Intent intent = activity.getIntent();
            activity.userId.set(intent.getStringExtra("userId"));
            activity.select.set(Objects.requireNonNull(intent.getExtras()).getBoolean("select"));

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ManageSetupActivity activity = weakReference.get();

            if(activity.select.get()) {
               activity.viewPager.setCurrentItem(1);
            }

            Bundle bundle = new Bundle();
            bundle.putString("userId", activity.userId.get());

            List<Fragment> list = Arrays.asList(
                    new SetupAddFragment(),
                    new SetupDeleteFragment(),
                    new SetupSelectFragment(),
                    new SetupHomeFragment());
            list.forEach(fragment -> fragment.setArguments(bundle));
//            SetupAddFragment setupAddFragment = new SetupAddFragment();
//            setupAddFragment.setArguments(bundle);
//
//            SetupDeleteFragment setupDeleteFragment = new SetupDeleteFragment();
//            setupDeleteFragment.setArguments(bundle);
//
//            SetupSelectFragment setupSelectFragment = new SetupSelectFragment();
//            setupSelectFragment.setArguments(bundle);
//
        }
    }
}
