package com.example.mplayer.structure.body.management.activities.playlists;

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
import com.example.mplayer.structure.body.management.activities.NewBuildActivity;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class RoomSelectActivity extends AppCompatActivity {
    private final String TAG = "RoomSelectActivity";

    private Spinner roomSpinner;
    private List<String> roomsId;

    private FirebaseHandler firebaseHandler;
    private SharedResources resources;

    private ArrayAdapter<String> adapter;
    private AtomicReference<Class> prevActivity;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_select);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        roomSpinner = findViewById(R.id.roomSpinner);
        prevActivity = new AtomicReference<>();
        roomsId = new ArrayList<>();

        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();
        Log.w(TAG, resources.getSetupId());
        adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, roomsId);
        roomSpinner.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        prevActivity.set((Class) getIntent().getExtras().get("prevActivity"));
        new BackgroundTask(this).execute();
    }

    public void selectRoom(View view) {
        if(roomSpinner.getSelectedItem() != null) {
            resources.setRoomId(String.valueOf(roomSpinner.getSelectedItemId()));
            Intent intent = new Intent(getBaseContext(), PlaylistAddActivity.class);
            intent.putExtra("prevActivity", prevActivity.get());
            startActivity(intent);
        } else {
            Log.e(TAG, "Room not selected");
            Toast.makeText(getBaseContext(), "Please select a room!", Toast.LENGTH_SHORT).show();
        }
    }

    public void roomSelectBack(View view) {
        Intent intent = new Intent(getBaseContext(), NewBuildActivity.class);
        intent.putExtra("prevActivity", prevActivity.get());
        startActivity(intent);
    }

    private static class BackgroundTask extends AsyncTask<Void, Void, Void> {
        WeakReference<RoomSelectActivity> weakReference;

        BackgroundTask(RoomSelectActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RoomSelectActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.firebaseHandler.getSetupRooms(activity.resources.getSetupId(), activity.roomsId, activity.adapter);
            return null;
        }
    }
}
