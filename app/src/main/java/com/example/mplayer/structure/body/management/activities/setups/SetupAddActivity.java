package com.example.mplayer.structure.body.management.activities.setups;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mplayer.R;
import com.example.mplayer.entities.Room;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SetupAddActivity extends AppCompatActivity {

    private final String TAG = "SetupAddActivity";

    private FirebaseHandler firebaseHandler;
    private SharedResources resources;

    private AtomicReference<Class> prevActivity;
    private List<Room> rooms;
    private List<String> roomsIds;

    private EditText nrOfRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_add);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        nrOfRooms = findViewById(R.id.setupAddId);

        prevActivity = new AtomicReference<>();
        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();
        rooms = new ArrayList<>();
        roomsIds = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        prevActivity.set((Class) getIntent().getExtras().get("prevActivity"));
    }

    public void addSetup(View view) {
        Setup setup = new Setup(resources.getUserId());
        String text = nrOfRooms.getText().toString();

        setup.setType(resources.getPlayType());
        try {
            int nr = Integer.parseInt(text);
            for(int i = 0; i < nr; i++) {
                Room room = new Room(setup.getId());
                rooms.add(room);
                roomsIds.add(room.getId());
            }
            setup.setRooms(roomsIds);
            resources.setSetupId(setup.getId());
            Log.d(TAG, "Setup added with id:" + setup.getId());
            new AddSetupAndRoomsAsync(this, setup, rooms).execute();
            Class<?> cls = prevActivity.get();
            Intent intent = new Intent(getBaseContext(), cls);
            startActivity(intent);
        } catch (NumberFormatException e) {
            Log.w(TAG, "Invalid number");
            e.printStackTrace();
        }
    }

    public void backNewSetup(View view) {
        Class<?> cls = prevActivity.get();
        Intent intent = new Intent(getBaseContext(), cls);
        startActivity(intent);
    }

    private static class AddSetupAndRoomsAsync extends AsyncTask<Void, Void, Void> {
        WeakReference<SetupAddActivity> weakReference;
        Setup setup;
        List<Room> rooms;

        AddSetupAndRoomsAsync(SetupAddActivity activity, Setup setup, List<Room> rooms) {
            weakReference = new WeakReference<>(activity);
            this.setup = setup;
            this.rooms = rooms;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {

            SetupAddActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.firebaseHandler.addSetup(setup);
            rooms.forEach(room -> activity.firebaseHandler.addRoom(room));

            return null;
        }
    }
}
