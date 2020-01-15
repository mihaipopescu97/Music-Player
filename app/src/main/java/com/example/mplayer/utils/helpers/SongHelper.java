package com.example.mplayer.utils.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mplayer.entities.Song;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SongHelper {

    private final static String TAG = "SongHelper";

    private static SongHelper instance = null;
    private DatabaseReference songRef;

    private SongHelper(DatabaseReference songRef) {
        this.songRef = songRef;
    }

    public static SongHelper getInstance(DatabaseReference ref) {
        if(instance == null) {
            instance = new SongHelper(ref);
        }

        return instance;
    }

    public void addSong(Song song) {
        songRef.child(song.getId()).setValue(song)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Post song successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Post song failed");
                    }
                });
    }

    public List<Song> getSongs() {

        final List<Song> songs = new ArrayList<>();

        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Song song = keyNode.getValue(Song.class);
                    songs.add(song);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return songs;
    }

    public Song getSong(final String id) {

        final Song[] searchedSong = new Song[1];
        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Song song = keyNode.getValue(Song.class);
                    if (song.getId().equals(id)) {
                        searchedSong[0] = song;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return searchedSong[0];
    }

    public void updateSong(final String id, Song song) {
        songRef.child(id).setValue(song)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Song :" + id + " updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Update song failed");
                    }
                });

    }

    public void deleteSong(final String id) {
        songRef.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Song :" + id + " deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Delete song failed");
                    }
                });
    }
}
