package com.example.mplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Spinner roomsId;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private List<String> rooms = new ArrayList<>();
    private DatabaseReference usersRef = rootRef.child("Rooms");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        roomsId = findViewById(R.id.rooms);
        //Button logOutBtn = findViewById(R.id.logOutBtn);

        //Fetch from db
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    rooms.add(ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
        usersRef.addListenerForSingleValueEvent(valueEventListener);

        //Set the rooms
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rooms);
        roomsId.setAdapter(adapter);
    }

    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

    public void createPlaylist(View view) {
        Intent i = new Intent(HomeActivity.this, UpdatePlaylistActivity.class);
        i.putExtra("roomId", roomsId.getSelectedItem().toString());
        startActivity(i);
    }
}
