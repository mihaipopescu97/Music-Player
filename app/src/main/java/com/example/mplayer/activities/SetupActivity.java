package com.example.mplayer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.mplayer.R;
import com.example.mplayer.entities.Room;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.utils.FirebaseHandler;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SetupActivity extends AppCompatActivity {

    private EditText nrOfRoomsId;
    private static String devId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            devId = bundle.getString("devId");
        } else {
            String TAG = "SetupActivity";
            Log.e(TAG, "No data send between activities");
        }

        nrOfRoomsId = findViewById(R.id.nrOfRooms);
        FirebaseHandler.setInstance(FirebaseDatabase.getInstance());
    }

    public void create(View view) {

        //Removes current setup if there is one on the current device;
        if(FirebaseHandler.checkChild("Setups", devId))
            FirebaseHandler.removeSetup(devId);

        //Creates a new setup
        Setup setup = new Setup(devId);
        List<Room> rooms = new ArrayList<>();
        int nrOfRooms = Integer.parseInt(nrOfRoomsId.toString().trim());

        for(int i = 0; i < nrOfRooms; i++) {
            rooms.add(new Room(setup.getId()));
        }

        setup.setRooms(rooms);
        FirebaseHandler.addSetup(setup);

        //Back to home
        startActivity(new Intent(SetupActivity.this, HomeActivity.class));
    }
}
