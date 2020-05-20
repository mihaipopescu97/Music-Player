package com.example.mplayer.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnectionServ";

    private final BluetoothAdapter bluetoothAdapter;

    private static BluetoothConnectionService instance = null;
    private static ConnectThread connectThread;
    private static ConnectedThread connectedThread;

    private Handler handler = new Handler();

    private BluetoothConnectionService(final BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public static BluetoothConnectionService getInstance(final BluetoothAdapter bluetoothAdapter) {
        if (instance == null) {
            instance = new BluetoothConnectionService(bluetoothAdapter);
        }

        return instance;
    }

    public static BluetoothConnectionService getInstance() {
        if (instance == null) {
           return null;
        }

        return instance;
    }

    public ConnectThread getConnectThread() {
        return connectThread;
    }

    public ConnectedThread getConnectedThread() {
        return connectedThread;
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started");
            //Use a temporary obj that is later assigned to mmSocket
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                Log.d(TAG, "ConnectThread: Trying to create InsecureRFcommSocket using UUID: " + uuid);
                tmp = device.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "ConnectThread: RUN");

            //Always cancel discovery because it will slow down a connection
            bluetoothAdapter.cancelDiscovery();

            try {
                //This is a blocking call and will only return on a
                //successful connection or an exception
                mmSocket.connect();
                Log.d(TAG, "ConnectThread: connected");
            } catch (IOException e) {
                //Close the socket
                try {
                   mmSocket.close();
                    Log.d(TAG, "ConnectThread: socket closed");
                } catch (IOException e1) {
                    Log.e(TAG, "ConnectThread: Unable to close connection in socket: " + e1.getMessage());
                }
                Log.e(TAG, "ConnectThread: could not connect to UUID" + e.getMessage());
                return;
            }

            connected(mmSocket);
        }

        void cancel() {
            try {
                Log.d(TAG, "ConnectThread: Closing client socket");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: failed to close socket: " + e.getMessage());
            }
        }
    }


    /**
     * AcceptThread starts and sits waiting for a connection.
     * Then ConnectThread starts and attempts to make a connection with the other devices.
     * @param device
     *          The paired device.
     */
    public synchronized void startClient(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startClient: started");
        connectThread = new ConnectThread(device, uuid);
        connectThread.start();

    }

    public void cancel() {
        connectedThread.cancel();
        connectedThread.interrupt();
        connectThread.cancel();
        connectThread.interrupt();
    }


    private interface MessageConstants {
        int MESSAGE_READ = 0;
        int MESSAGE_WRITE = 1;
        int MESSAGE_TOAST = 2;
    }
    /**
     * The ConnectedThread which is responsible for maintaining the BTConnection,
     * Sending the data and receiving incoming data through input/output streams respectively.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;
        private byte[] buffer;

        ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: start");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = mmSocket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "ConnectedThread: input stream error: " + e.getMessage());
            }

            try {
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "ConnectedThread: output stream error: " + e.getMessage());
            }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        public void run() {
            //buffer store for the streams
            buffer = new byte[1024];

            //bytes returned from read()
            int bytes;

            //keep listening to the InputStream until an exception occurs
            while (true) {
                //Read from the InputStream
                try {
                    bytes = inputStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "InputStream: " + incomingMessage);
                    Message readMsg = handler.obtainMessage(
                            MessageConstants.MESSAGE_READ, bytes, -1,
                            buffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.e(TAG, "write: Error reading input: " + e.getMessage());
                    break;
                }
            }
        }

        //Call from the activity to send data to the remote service
        void write(byte[] bytes) {
            try {
                outputStream.write(bytes);

                // Share the sent message with the UI activity.
                Message writtenMsg = handler.obtainMessage(
                        MessageConstants.MESSAGE_WRITE, -1, -1, buffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        handler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                handler.sendMessage(writeErrorMsg);
            }
        }

        //Call from activity to shutdown the connection
        void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "ConnectedThread: Failed to close socket: " + e.getMessage());
            }
        }
    }

    private synchronized void connected(final BluetoothSocket mmSocket) {
        Log.d(TAG, "connected: starting");

        //Start the thread to manage the connection and perform transmission
        connectedThread = new ConnectedThread(mmSocket);
        connectedThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param bytes
     *          The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] bytes) {
        Log.d(TAG, "write: write called");
        //perform the write
        connectedThread.write(bytes);
    }
}
