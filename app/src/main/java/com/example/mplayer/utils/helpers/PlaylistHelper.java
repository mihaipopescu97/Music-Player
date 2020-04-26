package com.example.mplayer.utils.helpers;

import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.mplayer.entities.Playlist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
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

    public void addPlaylist(final Playlist playlist) {
        playlistRef.child(playlist.getId()).setValue(playlist)
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Post playlist successful"))
                .addOnFailureListener(e -> Log.e(TAG, "Post playlist failed"));
    }

    public void getPlaylists(List<Playlist> playlists) {
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
    }

    public void getRoomPlaylist(final String roomId, final AtomicReference<Playlist> playlists) {
        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Playlist playlist = keyNode.getValue(Playlist.class);
                    if(playlist.getRoomId().equals(roomId)) {
                        playlists.set(playlist);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        if(playlists.get().equals(null)) {
            Log.w(TAG, "No playlists for room:" + roomId);
        }
    }

    public void getUserPlaylist(final String userId, final List<Playlist> playlists) {
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
    }

    public void getUserPlaylist(final String userId, final List<String> playlists, final ArrayAdapter<String> adapter) {
        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Playlist playlist = keyNode.getValue(Playlist.class);
                    if(userId.equals(playlist.getUserId())) {
                        playlists.add(playlist.getId());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        if(playlists.isEmpty()) {
            Log.w(TAG, "No playlists for user:" + userId);
        }
    }

    public void getPlaylist(final String id, final AtomicReference<Playlist> searchedPlaylist) {
        playlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Playlist playlist = keyNode.getValue(Playlist.class);
                    if (playlist.getId().equals(id)) {
                        searchedPlaylist.set(playlist);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getPlaylistSongsNames(final String id, final List<String> songs) {
        AtomicReference<Playlist> playlist = new AtomicReference<>();
        getPlaylist(id, playlist);
        songs.addAll(playlist.get().getSongs());
    }

    public void updatePlaylist(final String id, Playlist playlist) {
        playlistRef.child(id).setValue(playlist)
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Playlist :" + id + " updated"))
                .addOnFailureListener(e -> Log.e(TAG, "Update playlist failed"));
    }

    public void deletePlaylist(final String id) {
        playlistRef.child(id).removeValue()
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Playlist :" + id + " deleted"))
                .addOnFailureListener(e -> Log.e(TAG, "Delete playlist failed"));
    }

    public void deletePlaylist(final String id, final List<String> playlist, final ArrayAdapter<String> adapter) {
        playlistRef.child(id).removeValue()
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Playlist :" + id + " deleted"))
                .addOnFailureListener(e -> Log.e(TAG, "Delete playlist failed"));
        playlist.remove(id);
        adapter.notifyDataSetChanged();
    }
}
