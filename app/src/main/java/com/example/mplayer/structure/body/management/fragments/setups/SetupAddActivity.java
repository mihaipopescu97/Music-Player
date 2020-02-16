package com.example.mplayer.structure.body.management.fragments.setups;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mplayer.R;
import com.example.mplayer.entities.Room;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.structure.body.management.activities.BaseActivity;
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

    private EditText nrOfRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setup_add);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        nrOfRooms = findViewById(R.id.setupAddId);

        firebaseHandler = FirebaseHandler.getInstance();
        prevActivity = new AtomicReference<>();

        resources = SharedResources.getInstance();
        rooms = new ArrayList<>();

        new BackgroundTasks(this).execute();
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
            }

            setup.setRooms(rooms);
            Log.d(TAG, "Setup added with id:" + setup.getId());
            firebaseHandler.addSetup(setup);
            Log.i(TAG, "Changing to new build activity");
            startActivity(new Intent(getBaseContext(), BaseActivity.class));
        } catch (NumberFormatException e) {
            Log.w(TAG, "Invalid number");
            e.printStackTrace();
        }
    }

    public void backNewSetup(View view) {
        Class<?> cls = prevActivity.get();
        startActivity(new Intent(getBaseContext(), cls));
    }

    private static class BackgroundTasks extends AsyncTask<Void, Void, Void> {
        WeakReference<SetupAddActivity> weakReference;

        BackgroundTasks(SetupAddActivity fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SetupAddActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_START.label);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            SetupAddActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);

            Intent intent = activity.getIntent();
            activity.prevActivity.set((Class) intent.getExtras().get("prevActivity"));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SetupAddActivity activity = weakReference.get();
            Log.d(activity.TAG, LogMessages.ASYNC_END.label);
        }
    }
}
