package com.example.mplayer.utils.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mplayer.entities.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class UserHelper {

    private final static String TAG = "UserHelper";

    private static UserHelper instance = null;
    private DatabaseReference userRef;

    private UserHelper(DatabaseReference userRef) {
        this.userRef = userRef;
    }

    public static UserHelper getInstance(DatabaseReference ref) {
        if(instance == null) {
            instance = new UserHelper(ref);
        }

        return instance;
    }

    public void addUser(final User user) {
        userRef.child(user.getId()).setValue(user)
                .addOnSuccessListener(aVoid -> Log.i(TAG, "User post successful"))
                .addOnFailureListener(e -> Log.e(TAG, "User post failed"));
    }

    public void getUsers(List<User> users) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    User user = keyNode.getValue(User.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });
    }

    public void getUser(final String id,final AtomicReference<User> searchedUser) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    User user = keyNode.getValue(User.class);
                    if (user.getId().equals(id)) {
                        searchedUser.set(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });
    }

    public synchronized void getUserIdFromEmail(final String email, final AtomicReference<String> userId) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    User user = keyNode.getValue(User.class);
                    final String userEmail = user.getEmail();
                    if (userEmail.equals(email)) {
                        userId.set(user.getId());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });
    }

    public void updateUser(final String id, final User user) {
        userRef.child(id).setValue(user)
                .addOnSuccessListener(aVoid -> Log.i(TAG, "User :" + id + " updated"))
                .addOnFailureListener(e -> Log.e(TAG, "Update user failed"));
    }

    public void deleteUser(final String id) {
        userRef.child(id).removeValue()
                .addOnSuccessListener(aVoid -> Log.i(TAG, "User :" + id + " deleted"))
                .addOnFailureListener(e -> Log.e(TAG, "Delete user failed"));
    }
}
