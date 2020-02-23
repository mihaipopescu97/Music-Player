package com.example.mplayer.structure.body.management.activities.setups;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mplayer.R;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SetupDeleteActivity extends AppCompatActivity {
    private final String TAG = "SetupDeleteActivity";

    private List<Setup> setups;

    private Spinner setupSpinner;

    private FirebaseHandler firebaseHandler;
    private SharedResources resources;

    private AtomicReference<ArrayAdapter<String>> adapter;
    private Thread thread;
    private UpdateSpinner updateSpinner;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_delete);

        Log.d(TAG, LogMessages.ACTIVITY_START.label);

        setupSpinner = findViewById(R.id.setupDeleteSpinner);

        setups = Collections.synchronizedList(new ArrayList<>());
        adapter = new AtomicReference<>();

        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();

        new BackgroundTask(this).execute();

        updateSpinner = new UpdateSpinner(this);
        updateSpinner.execute();

        thread = new Thread(() -> {
            setupSpinner.setAdapter(adapter.get());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void deleteSetup(View view) {
        if(setupSpinner.getSelectedItem() != null) {
            Log.d(TAG, "Deleting setup:" + setupSpinner.getSelectedItem().toString());
            new DeleteAsync(this, String.valueOf(setupSpinner.getSelectedItemId())).execute();
        } else {
            Log.e(TAG, "Device not selected");
            Toast.makeText(getBaseContext(), "Please select a device!", Toast.LENGTH_SHORT).show();
        }
    }

    public void doneDeleteSetup(View view) {
        thread.interrupt();
        updateSpinner.cancel(true);
        startActivity(new Intent(getBaseContext(), SetupHomeActivity.class));
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<SetupDeleteActivity> weakReference;

        BackgroundTask(SetupDeleteActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SetupDeleteActivity activity =weakReference.get();
            Log.i(activity.TAG, LogMessages.ASYNC_WORKING.label);
            activity.firebaseHandler.getUserSetups(activity.resources.getUserId(), activity.setups);
            return null;
        }
    }

    private static class UpdateSpinner extends AsyncTask<Void, Void, Void> {
        WeakReference<SetupDeleteActivity> weakReference;

        UpdateSpinner(SetupDeleteActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            SetupDeleteActivity activity =weakReference.get();
            Log.i(activity.TAG, LogMessages.ASYNC_WORKING.label);

            List<String> setupsId = new ArrayList<>();
            //noinspection InfiniteLoopStatement
            while (true) {
                setupsId.clear();
                activity.setups.forEach(setup -> setupsId.add(setup.getId()));
                activity.adapter.set(new ArrayAdapter<>(activity.getBaseContext(), android.R.layout.simple_spinner_dropdown_item, setupsId));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class DeleteAsync extends AsyncTask<Void, Void, Void> {
        WeakReference<SetupDeleteActivity> weakReference;
        String id;

        DeleteAsync(SetupDeleteActivity activity, String id) {
            weakReference = new WeakReference<>(activity);
            this.id = id;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            SetupDeleteActivity activity = weakReference.get();
            Log.i(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.firebaseHandler.deleteSetup(id);
            return null;
        }
    }
}
