package com.example.mplayer.utils.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mplayer.entities.Device;
import com.example.mplayer.entities.Setup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SetupHelper {

    private final static String TAG = "SetupHelper";

    private static SetupHelper instance = null;
    private DatabaseReference setupRef;

    private SetupHelper(DatabaseReference setupRef) {
        this.setupRef = setupRef;
    }

    public static SetupHelper getInstance(DatabaseReference ref) {
        if(instance == null) {
            instance = new SetupHelper(ref);
        }

        return instance;
    }

    public void addSetup(Setup setup) {
        setupRef.child(setup.getId()).setValue(setup)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Post setup successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Post setup failed");
                    }
                });
    }

    public List<Setup> getSetups() {

        final List<Setup> setups = new ArrayList<>();

        setupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Setup setup = keyNode.getValue(Setup.class);
                    setups.add(setup);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return setups;
    }

    public List<Setup> getDeviceSetups(final String deviceId) {
        final List<Setup> setups = new ArrayList<>();

        setupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Setup setup = keyNode.getValue(Setup.class);
                    if(setup.getDeviceId().equals(deviceId)) {
                        setups.add(setup);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        if(setups.isEmpty()) {
            Log.w(TAG, "No setups for device:" + deviceId);
        }

        return setups;
    }

    public Setup getSetup(final String id) {

        final Setup[] searchedSetup = new Setup[1];
        setupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Setup setup = keyNode.getValue(Setup.class);
                    if (setup.getId().equals(id)) {
                        searchedSetup[0] = setup;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return searchedSetup[0];
    }

    public void updateSetup(final String id, Setup setup) {
        setupRef.child(id).setValue(setup)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Setup :" + id + " updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Update setup failed");
                    }
                });

    }

    public void deleteSetup(final String id) {
        setupRef.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Setup :" + id + " deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Delete setup failed");
                    }
                });
    }

}
