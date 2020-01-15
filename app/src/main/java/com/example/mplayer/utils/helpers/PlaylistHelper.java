package com.example.mplayer.utils.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mplayer.entities.Playlist;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlaylistHelper {

    private final static String TAG = "PlaylistHelper";

    private static PlaylistHelper instance = null;
    private DatabaseReference playlistRef;

    private PlaylistHelper(DatabaseReference playlistRef) {
        this.playlistRef = playlistRef;
    }

    public static PlaylistHelper getInstance(DatabaseReference ref) {
        if(instance == null) {
            instance = new PlaylistHelper(ref);
        }

        return instance;
    }

    public void addPlaylist(Playlist playlist) {
        playlistRef.child(playlist.getId()).setValue(playlist)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Post playlist successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Post playlist failed");
                    }
                });
    }

    public List<Playlist> getPlaylists() {

        final List<Playlist> playlists = new ArrayList<>();

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Playlist playlist = keyNode.getValue(Playlist.class);
                    playlists.add(playlist);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return playlists;
    }

    public List<Playlist> getUserPlaylist(final String userId) {

        final List<Playlist> playlists = new ArrayList<>();

        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Playlist playlist = keyNode.getValue(Playlist.class);
                    if(playlist.getUserId().equals(userId)) {
                        playlists.add(playlist);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        if(playlists.isEmpty()) {
            Log.w(TAG, "No playlists for user:" + userId);
        }

        return playlists;
    }

    public Playlist getPlaylist(final String id) {

        final Playlist[] searchedPlaylist = new Playlist[1];
        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Playlist playlist = keyNode.getValue(Playlist.class);
                    if (playlist.getId().equals(id)) {
                        searchedPlaylist[0] = playlist;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return searchedPlaylist[0];
    }

    public void updatePlaylist(final String id, Playlist playlist) {
        playlistRef.child(id).setValue(playlist)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Playlist :" + id + " updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Update playlist failed");
                    }
                });

    }

    public void deletePlaylist(final String id) {
        playlistRef.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Playlist :" + id + " deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Delete playlist failed");
                    }
                });
    }


}
