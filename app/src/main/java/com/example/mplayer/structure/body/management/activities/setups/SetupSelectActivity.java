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
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SetupSelectActivity extends AppCompatActivity {
    private final String TAG = "SetupSelectActivity";

    private List<String> setups;

    private  FirebaseHandler firebaseHandler;
    private SharedResources resources;

    private AtomicReference<Class> prevActivity;
    private Spinner setupSpinner;
    private ArrayAdapter<String> adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_select);
        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        setupSpinner = findViewById(R.id.setupSelectSpinner);

        setups = Collections.synchronizedList(new ArrayList<>());
        prevActivity = new AtomicReference<>();
        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();
        adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, setups);
        setupSpinner.setAdapter(adapter);

        new BackgroundTask(this).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        prevActivity.set((Class) getIntent().getExtras().get("prevActivity"));
    }

    public void deviceSelect(View view) {
        if(setupSpinner.getSelectedItem() != null) {
            resources.setSetupId(setupSpinner.getSelectedItem().toString());
            Log.d(TAG, LogMessages.CHANGE_HOME.label);
            Class<?> cls = prevActivity.get();
            startActivity(new Intent(getBaseContext(), cls));
        } else {
            Log.e(TAG, "Device not selected");
            Toast.makeText(getBaseContext(), "Please select a device!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backSetupSelect(View view) {
        Class<?> cls = prevActivity.get();
        startActivity(new Intent(getBaseContext(), cls));
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<SetupSelectActivity> weakReference;

        BackgroundTask(SetupSelectActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SetupSelectActivity activity = weakReference.get();
            Log.i(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.firebaseHandler.getUserSetups(activity.resources.getUserId(), activity.setups, activity.adapter);
            return null;
        }
    }
}
