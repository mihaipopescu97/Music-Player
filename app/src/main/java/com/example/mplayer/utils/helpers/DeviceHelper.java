package com.example.mplayer.utils.helpers;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.mplayer.entities.Device;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class DeviceHelper {

    private final static String TAG = "DeviceHelper";

    private static DeviceHelper instance = null;
    private final DatabaseReference deviceRef;

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
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Post device successful"))
                .addOnFailureListener(e -> Log.e(TAG, "Post device failed"));
    }

    public void getDevices(final List<Device> devices) {
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
    }

    public void getUserDevices(final String userId, final List<Device> devices) {
        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Device device = keyNode.getValue(Device.class);
                    if(userId.equals(device.getUserId())) {
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
    }

    public void getUserDevices(final String userId, final List<String> devices, final ArrayAdapter<String> adapter) {
        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Device device = keyNode.getValue(Device.class);
                    if(userId.equals(device.getUserId())) {
                        devices.add(device.getId());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });

        if(devices.isEmpty()) {
            Log.w(TAG, "No devices for user:" + userId);
        }
    }

    public void getDevice(final String id, final AtomicReference<Device> searchedDevice) {
        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Device device = keyNode.getValue(Device.class);
                    if (id.equals(device.getId())) {
                         searchedDevice.set(device);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCanceled", databaseError.toException());
            }
        });
    }

    public void updateDevice(final String id, final Device device) {
        deviceRef.child(id).setValue(device)
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Device :" + id + " updated"))
                .addOnFailureListener(e -> Log.e(TAG, "Update device failed"));

    }

    public void deleteDevice(final String id) {
        deviceRef.child(id).removeValue()
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Device :" + id + " deleted"))
                .addOnFailureListener(e -> Log.e(TAG, "Delete device failed"));
    }

    public void deleteDevice(final String id, final List<String> devices, final ArrayAdapter<String> adapter) {
        deviceRef.child(id).removeValue()
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Device :" + id + " deleted"))
                .addOnFailureListener(e -> Log.e(TAG, "Delete device failed"));
        devices.remove(id);
        adapter.notifyDataSetChanged();
    }
}
