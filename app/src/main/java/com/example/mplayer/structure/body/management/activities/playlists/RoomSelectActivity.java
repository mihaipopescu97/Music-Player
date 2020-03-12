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
import com.example.mplayer.entities.Room;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomSelectActivity extends AppCompatActivity {
    private final String TAG = "RoomSelectActivity";

    private Spinner roomSpinner;

    private List<Room> rooms;
    private List<String> roomsId;

    private FirebaseHandler firebaseHandler;
    private SharedResources resources;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_select);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        roomSpinner = findViewById(R.id.roomSpinner);

        rooms = Collections.synchronizedList(new ArrayList<>());
        roomsId = new ArrayList<>();

        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();
        Log.w(TAG, resources.getSetupId());
        new BackgroundTask(this).execute();
//            roomsId.clear();
//            rooms.forEach(room -> roomsId.add(room.getId()));
//            adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, roomsId);

        roomSpinner.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, roomsId));
    }

    public void selectRoom(View view) {
        if(roomSpinner.getSelectedItem() != null) {
            resources.setRoomId(String.valueOf(roomSpinner.getSelectedItemId()));
            startActivity(new Intent(getBaseContext(), PlaylistAddActivity.class));
        } else {
            Log.e(TAG, "Room not selected");
            Toast.makeText(getBaseContext(), "Please select a room!", Toast.LENGTH_SHORT).show();
        }
    }

    public void roomSelectBack(View view) {
        startActivity(new Intent(getBaseContext(), PlaylistHomeActivity.class));
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

            activity.firebaseHandler.getSetupRooms(activity.resources.getSetupId(), activity.rooms, activity.roomsId);
            return null;
        }
    }
}
