package com.example.mplayer.utils;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SharedResources {

    private static SharedResources instance = null;
    private static AtomicReference<String> userId;
    private static AtomicReference<String> setupId;
    private static AtomicReference<String> deviceId;
    private static AtomicReference<String> playlistId;
    private static AtomicReference<String> roomId;
    private static AtomicReference<String> playType;
    private static AtomicReference<BluetoothAdapter> bluetoothAdapter;

    private SharedResources(){
        userId = new AtomicReference<>();
        setupId = new AtomicReference<>();
        deviceId = new AtomicReference<>();
        playlistId = new AtomicReference<>();
        roomId = new AtomicReference<>();
        playType = new AtomicReference<>();
        bluetoothAdapter = new AtomicReference<>();
    }

    public static SharedResources getInstance() {
        if(instance == null) {
            instance = new SharedResources();
        }

        return instance;
    }

    public void setUserId(String user) {
        userId.set(user);
    }

    public String getUserId() {
        return userId.get();
    }

    public void setSetupId(String setup) {
        setupId.set(setup);
    }

    public String getSetupId() {
        return setupId.get();
    }


    public void setDeviceId(String device) {
        deviceId.set(device);
    }

    public String getDeviceId() {
        return deviceId.get();
    }

    public void setPlaylistId(String playlist) {
        playlistId.set(playlist);
    }

    public String getPlaylistId() {
        return playlistId.get();
    }

    public void setRoomId(String room) {
        roomId.set(room);
    }

    public String getRoomID() {
        return roomId.get();
    }

    public void setPlayType(String playT) {
        playType.set(playT);
    }

    public String getPlayType() {
        return playType.get();
    }

    public void resetPlayType() {
        playType.set(null);
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter.get();
    }

    public void setBluetoothAdapter(final BluetoothAdapter ba) {
        bluetoothAdapter.set(ba);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void resetAll() {
        List<AtomicReference> lst = Arrays.asList(userId, deviceId, playlistId, setupId, playType, roomId);
        lst.forEach(el -> el.set(null));
    }

}
