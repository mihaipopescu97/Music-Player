package com.example.mplayer.utils;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

public class BluetoothSender {

    private static BluetoothSender instance = null;

    private BluetoothAdapter bluetoothAdapter;

    private OutputStream outputStream;

    //TODO make connection to RPI based on name of device?
    private BluetoothSender() throws IOException {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null) {
            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

            if(bondedDevices.size() > 0) {
                Object[] devices = bondedDevices.toArray();

                //add verification?
                BluetoothDevice device = (BluetoothDevice) devices[0];
                ParcelUuid[] uuids = device.getUuids();
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                try {
                    socket.connect();

                    outputStream = socket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                Log.e("error", "No appropriate paired devices.");
            }
        } else {
            Log.e("error", "Bluetooth is disabled. Please turn it on!");
        }
    }

    public static BluetoothSender getInstance() throws IOException {
        if(instance == null) {
            try {
                instance = new BluetoothSender();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    public synchronized void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

    public synchronized Boolean check() {
        if (bluetoothAdapter.isEnabled()) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            return pairedDevices.size() > 0;
        }

        return false;
    }


}
