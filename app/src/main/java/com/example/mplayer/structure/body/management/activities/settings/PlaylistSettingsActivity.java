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
import com.example.mplayer.structure.body.management.activities.playlists.PlaylistAddActivity;
import com.example.mplayer.structure.body.management.fragments.playlists.PlaylistDeleteFragment;
import com.example.mplayer.structure.body.management.fragments.playlists.PlaylistHomeFragment;
import com.example.mplayer.structure.body.management.fragments.playlists.PlaylistSelectFragment;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

//TODO test then freeze
public class PlaylistSettingsActivity extends AppCompatActivity {

    private final String TAG = "PlaylistSettingsAct";

    private ViewPager viewPager;

    private AtomicReference<String> userId;
    private AtomicReference<String> prevActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_playlist);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        viewPager = findViewById(R.id.playlistContainer);
        setupViewPage(viewPager);
        viewPager.setCurrentItem(0);

        userId = new AtomicReference<>();
        prevActivity = new AtomicReference<>();

        final String roomId = getIntent().getStringExtra("roomId");
        Bundle bundle = new Bundle();
        bundle.putString("roomId", roomId);

        new BackgroundTasks(this).execute();
    }

    private void setupViewPage(ViewPager viewPager) {
        FragmentSectionAdapter adapter = new FragmentSectionAdapter(getSupportFragmentManager());

        Log.d(TAG, "Playlist home -> 0");
        adapter.addFragment(new PlaylistHomeFragment(), "PlaylistHomeFragment");

        Log.d(TAG, "Playlist select -> 1");
        adapter.addFragment(new PlaylistSelectFragment(), "PlaylistSelectFragment");

        Log.d(TAG, "Playlist add -> 2");
        adapter.addFragment(new PlaylistAddActivity(), "PlaylistAddActivity");

        Log.d(TAG, "Playlist delete -> 3");
        adapter.addFragment(new PlaylistDeleteFragment(), "PlaylistDeleteFragment");

        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }

    private static class BackgroundTasks extends AsyncTask<Void, Void, Void> {

        private WeakReference<PlaylistSettingsActivity> weakReference;

        BackgroundTasks(PlaylistSettingsActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PlaylistSettingsActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_START.label);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            PlaylistSettingsActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);
            Intent intent = activity.getIntent();
            activity.userId.set(intent.getStringExtra("userId"));
            activity.prevActivity.set(intent.getStringExtra("prevActivity"));

            Bundle bundle = new Bundle();
            bundle.putString("user", activity.userId.get());
            bundle.putString("prevActivity", activity.prevActivity.get());

            List<Fragment> fragments = Arrays.asList(
                    new PlaylistHomeFragment(),
                    new PlaylistAddActivity(),
                    new PlaylistDeleteFragment());
            fragments.forEach(fragment -> fragment.setArguments(bundle));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            PlaylistSettingsActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_END.label);
        }
    }
}
