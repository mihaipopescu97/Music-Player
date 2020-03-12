package com.example.mplayer.utils.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mplayer.entities.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
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
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Post room successful"))
                .addOnFailureListener(e -> Log.e(TAG, "Post room failed"));
    }

    public void getRooms(final List<Room> rooms) {
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
    }

    public void getSetupRooms(final String setupId, List<Room> rooms, final List<String> roomsId) {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Room room = keyNode.getValue(Room.class);
                    if(room.getPlaylistId().equals(setupId)) {
                        rooms.add(room);
                        roomsId.add(room.getId());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        if(rooms.isEmpty()) {
            Log.w(TAG, "No rooms for playlist:" + setupId);
        }
    }

    public void getRoom(final String id, final AtomicReference<Room> searchedRoom) {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Room room = keyNode.getValue(Room.class);
                    if (room.getId().equals(id)) {
                        searchedRoom.set(room);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });
    }

    public void updateRoom(final String id, Room room) {
        roomRef.child(id).setValue(room)
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Room :" + id + " updated"))
                .addOnFailureListener(e -> Log.e(TAG, "Update room failed"));

    }

    public void deleteRoom(final String id) {
        roomRef.child(id).removeValue()
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Room :" + id + " deleted"))
                .addOnFailureListener(e -> Log.e(TAG, "Delete room failed"));
    }
}
