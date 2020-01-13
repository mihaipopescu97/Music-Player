package com.example.mplayer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mplayer.adapters.fragments.DeviceSectionAdapter;
import com.example.mplayer.R;
import com.example.mplayer.activities.login.MainActivity;
import com.example.mplayer.fragments.SetupFragment;
import com.example.mplayer.entities.Room;
import com.example.mplayer.utils.FirebaseHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private static final String TAG = "HomeActivity";

    private DeviceSectionAdapter homeSectionAdapter;
    private ViewPager viewPager;
    private static String devId;

    //UI
    private Spinner roomsId;

    //Database
    //TODO DON'T FORGET THE BLUETOOTH
    private final List<String> rooms = new ArrayList<>();
    //private BluetoothSender bluetoothSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        roomsId = findViewById(R.id.setups);

        //Setup fragments
        homeSectionAdapter = new DeviceSectionAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);
        setupViewPage(viewPager);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            devId = bundle.getString("devId");
        } else {
            Log.e(TAG, "No data send between activities");
        }

        //Instantiate the bluetooth
//        try {
//            bluetoothSender = BluetoothSender.getInstance();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //Fetch the rooms
        FirebaseHandler firebaseHandler = FirebaseHandler.getInstance();
        DatabaseReference setupRef = FirebaseHandler.getSetupRef();
        setupRef.orderByChild("rooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rooms.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Room room = ds.getValue(Room.class);
                    if(room != null)
                        rooms.add(room.getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
            }
        });

        //Set the rooms
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rooms);
        roomsId.setAdapter(adapter);
    }

    private void setupViewPage(ViewPager viewPager) {
        DeviceSectionAdapter adapter = new DeviceSectionAdapter(getSupportFragmentManager());
        adapter.addFragment(new SetupFragment(), "SetupFragment");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }



    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

    public void updatePlaylist(View view) {
        Intent i = new Intent(HomeActivity.this, UpdatePlaylistActivity.class);

        Bundle bundle = new Bundle();

        bundle.putString("roomId", roomsId.getSelectedItem().toString());
        startActivity(i);

    }

    public void play(View view) {
//        if(bluetoothSender.check()) {
//            //TODO SEND ROOM ID
//            startActivity(new Intent(HomeActivity.this, PlayerActivity.class));
//        } else {
//            Toast.makeText(HomeActivity.this, "Bluetooth off",Toast.LENGTH_SHORT).show();
//        }
    }

    public void setup(View view) {
        Intent i = new Intent(HomeActivity.this, SetupActivity.class);

        Bundle bundle = new Bundle();

        bundle.putString("devId", devId);
        startActivity(i);
    }
}
