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

public class SetupDeleteActivity extends AppCompatActivity {
    private final String TAG = "SetupDeleteActivity";

    private List<String> setups;

    private Spinner setupSpinner;

    private FirebaseHandler firebaseHandler;
    private SharedResources resources;

    private ArrayAdapter<String> adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_delete);

        Log.d(TAG, LogMessages.ACTIVITY_START.label);

        setupSpinner = findViewById(R.id.setupDeleteSpinner);

        setups = Collections.synchronizedList(new ArrayList<>());
        adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, setups);

        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();
        setupSpinner.setAdapter(adapter);
        new BackgroundTask(this).execute();
    }

    public void deleteSetup(View view) {
        if(setupSpinner.getSelectedItem() != null) {
            Log.d(TAG, "Deleting setup:" + setupSpinner.getSelectedItem().toString());
            new DeleteAsync(this).execute(String.valueOf(setupSpinner.getSelectedItemId()));
        } else {
            Log.e(TAG, "Device not selected");
            Toast.makeText(getBaseContext(), "Please select a device!", Toast.LENGTH_SHORT).show();
        }
    }

    public void doneDeleteSetup(View view) {
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
            activity.firebaseHandler.getUserSetups(activity.resources.getUserId(), activity.setups, activity.adapter);
            return null;
        }
    }

    private static class DeleteAsync extends AsyncTask<String, Void, Void> {
        WeakReference<SetupDeleteActivity> weakReference;

        DeleteAsync(SetupDeleteActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... strings) {
            SetupDeleteActivity activity = weakReference.get();
            Log.i(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.firebaseHandler.deleteSetup(strings[0], activity.setups, activity.adapter);
            return null;
        }
    }
}
