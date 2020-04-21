package com.example.mplayer.utils.helpers;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.mplayer.entities.Setup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
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
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Post setup successful"))
                .addOnFailureListener(e -> Log.e(TAG, "Post setup failed"));
    }

    public void getSetups(List<Setup> setups) {
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
    }

    public void getUserSetups(final String userId, final List<Setup> setups) {
        setupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Setup setup = keyNode.getValue(Setup.class);
                    if(setup.getUserId().equals(userId)) {
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
            Log.w(TAG, "No setups for user:" + userId);
        }
    }

    public void getUserSetups(final String userId, final List<String> setups, final ArrayAdapter<String> adapter) {
        setupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Setup setup = keyNode.getValue(Setup.class);
                    if(setup.getUserId().equals(userId)) {
                        setups.add(setup.getId());
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        if(setups.isEmpty()) {
            Log.w(TAG, "No setups for user:" + userId);
        }
    }

    public void getSetup(final String id, final AtomicReference<Setup> searchedSetup) {
        setupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Setup setup = keyNode.getValue(Setup.class);
                    if (setup.getId().equals(id)) {
                        searchedSetup.set(setup);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });
    }

    public void updateSetup(final String id, Setup setup) {
        setupRef.child(id).setValue(setup)
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Setup :" + id + " updated"))
                .addOnFailureListener(e -> Log.e(TAG, "Update setup failed"));
    }

    public void deleteSetup(final String id) {
        setupRef.child(id).removeValue()
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Setup :" + id + " deleted"))
                .addOnFailureListener(e -> Log.e(TAG, "Delete setup failed"));
    }

    public void deleteSetup(final String id, final List<String> setups, final ArrayAdapter<String> adapter) {
        setupRef.child(id).removeValue()
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Setup :" + id + " deleted"))
                .addOnFailureListener(e -> Log.e(TAG, "Delete setup failed"));
        setups.remove(id);
        adapter.notifyDataSetChanged();
    }
}
