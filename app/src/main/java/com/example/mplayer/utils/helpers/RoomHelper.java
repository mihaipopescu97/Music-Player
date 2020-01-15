package com.example.mplayer.utils.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mplayer.entities.Playlist;
import com.example.mplayer.entities.Room;
import com.example.mplayer.entities.Setup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomHelper {

    private final static String TAG = "RoomHelper";

    private static RoomHelper instance = null;
    private DatabaseReference roomRef;

    private RoomHelper(DatabaseReference roomRef) {
        this.roomRef = roomRef;
    }

    public static RoomHelper getInstance(DatabaseReference ref) {
        if(instance == null) {
            instance = new RoomHelper(ref);
        }

        return instance;
    }

    public void addRoom(Room room) {
        roomRef.child(room.getId()).setValue(room)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Post room successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Post room failed");
                    }
                });
    }

    public List<Room> getRooms() {

        final List<Room> rooms = new ArrayList<>();

        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Room room = keyNode.getValue(Room.class);
                    rooms.add(room);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return rooms;
    }

    public List<Room> getSetupRooms(final String setupId) {
        final List<Room> rooms = new ArrayList<>();

        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Room room = keyNode.getValue(Room.class);
                    if(room.getPlaylistId().equals(setupId)) {
                        rooms.add(room);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        if(rooms.isEmpty()) {
            Log.e(TAG, "No rooms for playlist:" + setupId);
        }

        return rooms;
    }

    public Room getRoom(final String id) {

        final Room[] searchedRoom = new Room[1];
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Room room = keyNode.getValue(Room.class);
                    if (room.getId().equals(id)) {
                        searchedRoom[0] = room;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return searchedRoom[0];
    }

    public void updateRoom(final String id, Room room) {
        roomRef.child(id).setValue(room)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Room :" + id + " updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Update room failed");
                    }
                });

    }

    public void deleteRoom(final String id) {
        roomRef.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Room :" + id + " deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Delete room failed");
                    }
                });
    }


}
