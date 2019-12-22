package com.example.mplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdatePlaylistActivity extends AppCompatActivity {

    private String roomId;
    private final String TAG = "UpdatePlaylistActivity";


    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    //TODO Make hierarchy
    private DatabaseReference roomRef = xd.child("RoomPlaylist");
    private DatabaseReference playlistRef;

    private DatabaseReference songsRef = xd.child();

    private Spinner songs;
    private Spinner playlistSongs;

    private List<String> playlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_playlist);

        songs = findViewById(R.id.songs);
        playlistSongs = findViewById(R.id.playlist);

        //Get room id
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            roomId = extras.getString("roomId");
        }

        playlistRef = roomRef.child(roomId);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    playlist.add(ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
    }

    public void delete(View view) {
        Query deleteSong = playlistRef.orderByChild("name").equalTo(playlistSongs.getSelectedItem().toString());
        deleteSong.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.i(TAG, "Delete successful on :" + ds.getRef().toString());
                    ds.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });
    }

    public void add(View view) {
        Query addSong = songsRef.orderByChild("name").equalTo(songs.getSelectedItem().toString());
        addSong.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    playlistRef =

                    Log.i(TAG, "Added song :" + ds.getRef().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
