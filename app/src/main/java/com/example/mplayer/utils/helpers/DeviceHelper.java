package com.example.mplayer.utils.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mplayer.entities.Device;
import com.example.mplayer.entities.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeviceHelper {

    private final static String TAG = "DeviceHelper";

    private static DeviceHelper instance = null;
    private DatabaseReference deviceRef;

    private DeviceHelper(DatabaseReference deviceRef) {
        this.deviceRef = deviceRef;
    }

    public static DeviceHelper getInstance(DatabaseReference ref) {
        if(instance == null) {
            instance = new DeviceHelper(ref);
        }

        return instance;
    }

    public void addDevice(final Device device) {
        deviceRef.child(device.getId()).setValue(device)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Post device successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Post device failed");
                    }
                });
    }

    public List<Device> getDevices() {

        final List<Device> devices = new ArrayList<>();

        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Device device = keyNode.getValue(Device.class);
                    devices.add(device);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return devices;
    }

    public List<Device> getUserDevices(final String userId) {
        final List<Device> devices = new ArrayList<>();

        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Device device = keyNode.getValue(Device.class);
                    if(device.getUserId().equals(userId)) {
                        devices.add(device);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        if(devices.isEmpty()) {
            Log.w(TAG, "No devices for user:" + userId);
        }
        return devices;
    }

    public Device getDevice(final String id) {

        final Device[] searchedDevice = new Device[1];
        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Device device = keyNode.getValue(Device.class);
                    if (device.getId().equals(id)) {
                        searchedDevice[0] = device;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        return searchedDevice[0];
    }

    public void updateDevice(final String id, final Device device) {
        deviceRef.child(id).setValue(device)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Device :" + id + " updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Update device failed");
                    }
                });

    }

    public void deleteDevice(final String id) {
        deviceRef.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Device :" + id + " deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Delete device failed");
                    }
                });
    }
}
