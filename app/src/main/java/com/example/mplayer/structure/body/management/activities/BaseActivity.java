package com.example.mplayer.structure.body.management.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.settings.SettingsActivity;
import com.example.mplayer.structure.player.PlayerActivity;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.Actions;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;

//TODO needs testing then can freeze
//Back - OK
public class BaseActivity extends AppCompatActivity {

    private final String TAG = "BaseActivity";

    private SharedResources resources;

    private Button playBtn;

    private BackgroundTasks backgroundTasks;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        playBtn = findViewById(R.id.playBtn);

        resources = SharedResources.getInstance();

         backgroundTasks = new BackgroundTasks(this);
         backgroundTasks.execute();
    }

    public void createNewSetup(View view) {
        backgroundTasks.cancel(true);
        Intent intent = new Intent(BaseActivity.this, NewBuildActivity.class);
        intent.putExtra("type", Actions.CREATE);
        startActivity(intent);
    }

    public void useSetup(View view) {
        backgroundTasks.cancel(true);
        Intent intent = new Intent(BaseActivity.this, SelectBuildActivity.class);
        intent.putExtra("type", Actions.SELECT);
        startActivity(intent);
    }

    public void manageSettings(View view) {
        backgroundTasks.cancel(true);
        startActivity(new Intent(BaseActivity.this, SettingsActivity.class));
    }

    public void play(View view) {
        backgroundTasks.cancel(true);
        startActivity(new Intent(BaseActivity.this, PlayerActivity.class));
    }

    public void backSingle(View view) {
        backgroundTasks.cancel(true);
        startActivity( new Intent(BaseActivity.this, SelectActivity.class));
    }


    private static class BackgroundTasks extends AsyncTask<Void, Void, Void> {
        WeakReference<BaseActivity> weakReference;

        BackgroundTasks(BaseActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            BaseActivity activity = weakReference.get();

            Log.i(activity.TAG, LogMessages.ASYNC_START.label);
            activity.playBtn.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            BaseActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);
            //noinspection StatementWithEmptyBody
            while (activity.resources.getSetupId().equals(null)) {

            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //TODO check if setup is not null
            BaseActivity activity = weakReference.get();
            Log.i(activity.TAG, LogMessages.ASYNC_END.label);
            activity.playBtn.setVisibility(View.VISIBLE);
        }
    }
}
