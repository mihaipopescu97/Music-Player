package com.example.mplayer.structure.body.management.activities.setups;

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
import com.example.mplayer.utils.FirebaseHandler;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.LogMessages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SetupAddActivity extends AppCompatActivity {

    private final String TAG = "SetupAddActivity";

    private FirebaseHandler firebaseHandler;
    private SharedResources resources;

    private List<Room> rooms;

    private EditText nrOfRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_add);

        Log.i(TAG, LogMessages.ACTIVITY_START.label);

        nrOfRooms = findViewById(R.id.setupAddId);

        firebaseHandler = FirebaseHandler.getInstance();
        resources = SharedResources.getInstance();
        rooms = new ArrayList<>();


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
            new AddSetupAsync(this, setup).execute();
            Log.i(TAG, "Changing to new build activity");
            startActivity(new Intent(getBaseContext(), SetupHomeActivity.class));
        } catch (NumberFormatException e) {
            Log.w(TAG, "Invalid number");
            e.printStackTrace();
        }
    }

    public void backNewSetup(View view) {
        startActivity(new Intent(getBaseContext(), SetupHomeActivity.class));
    }

    private static class AddSetupAsync extends AsyncTask<Void, Void, Void> {
        WeakReference<SetupAddActivity> weakReference;
        Setup setup;

        AddSetupAsync(SetupAddActivity activity, Setup setup) {
            weakReference = new WeakReference<>(activity);
            this.setup = setup;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            SetupAddActivity activity = weakReference.get();

            Log.d(activity.TAG, LogMessages.ASYNC_WORKING.label);

            activity.firebaseHandler.addSetup(setup);
            return null;
        }
    }
}
