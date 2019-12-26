package com.example.mplayer.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mplayer.entities.Device;
import com.example.mplayer.entities.Playlist;
import com.example.mplayer.entities.Room;
import com.example.mplayer.entities.Setup;
import com.example.mplayer.entities.Song;
import com.example.mplayer.entities.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHandler {

    private static final String TAG = "FirebaseHandler";
    private static Boolean check;

    private static FirebaseHandler instance = null;
    private static DatabaseReference reff;

    private FirebaseHandler() {

    }

    public static synchronized FirebaseHandler getInstance() {
        if(instance == null) {
            instance = new FirebaseHandler();
        }
        return instance;
    }

    public static void setInstance(FirebaseDatabase firebaseDatabase) {
        reff = firebaseDatabase.getReference();
    }


    public static DatabaseReference getUserRef() {
        return reff.child("Users");
    }

    public static void addUser(User user) {
        reff.child("Users").child(user.getId()).setValue(user)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "Write successful");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Write failed");
            }
        });
    }


    public static DatabaseReference getRoomRef() {
        return reff.child("Rooms");
    }

    public static void addRoom(Room room) {
        reff.child("Rooms").child(room.getId()).setValue(room)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Write successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Write failed");
                    }
                });
    }


    public static DatabaseReference getDeviceRef() {
        return reff.child("Devices");
    }

    public static void addDevice(Device device) {
        reff.child("Devices").child(device.getId()).setValue(device)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Write successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Write failed");
                    }
                });
    }


    public static DatabaseReference getPlaylistRef() {
        return reff.child("Playlist");
    }

    public static void addPlaylist(Playlist playlist) {
        reff.child("Playlist").child(playlist.getId()).setValue(playlist)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Write successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Write failed");
                    }
                });
    }


    public static DatabaseReference getSetupRef() {
        return reff.child("Setups");
    }

    public static void addSetup(Setup setup) {
        reff.child("Setups").child(setup.getId()).setValue(setup)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Write successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Write failed");
                    }
                });
    }

    public static void removeSetup(String id) {
        reff.child("Setups").child(id).removeValue();
    }


    public static DatabaseReference getSongRef() {
        return reff.child("Songs");
    }

    public static void addSong(Song song) {
        reff.child("Songs").child(song.getId()).setValue(song)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Write successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Write failed");
                    }
                });
    }

    public static boolean checkChild(String baseReff, final String id) {
        check = false;
        reff.child(baseReff).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    check = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e( TAG, String.valueOf(databaseError));
            }
        });

        return check;
    }



    public static Boolean checkDevId(User user, String devId) {
        String dbDevId = reff.child("Devices").orderByChild("id").equalTo(user.getDeviceId()).toString();


        return devId.equals(dbDevId);
    }


}
