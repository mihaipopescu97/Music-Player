package com.example.mplayer.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnectionServ";

    private static final String appName = "MPlayer";

    private final UUID UUID_INSECURE;

    private final Context context;
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;
    private UUID deviceUUId;

    private AcceptThread insecureAcceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    public BluetoothConnectionService(final Context context, final BluetoothAdapter bluetoothAdapter,
                                      final String uuid) {
        this.context = context;
        this.bluetoothAdapter = bluetoothAdapter;
        this.UUID_INSECURE = UUID.fromString(uuid);
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        //Local server socket
        private final BluetoothServerSocket serverSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            //Create listener
            try {
                tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, UUID_INSECURE);
                Log.d(TAG, "Accept thread: Setting up Server using:" + UUID_INSECURE);
            } catch (IOException e) {
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage());
            }
            serverSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "run: AcceptThread Running");

            BluetoothSocket socket = null;

            try{
                Log.d(TAG, "run: RFCOM server socket start.....");
                socket = serverSocket.accept();
                Log.d(TAG, "run: RFCOM server socket accepted connection");
            } catch (IOException e) {
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage());
            }

            if(socket != null) {
                connected(socket, device);
            }

            Log.i(TAG, "AcceptThread: END");
        }

        public void cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread");
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage());
            }
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket socket;

        public ConnectThread(BluetoothDevice deviceRef, UUID uuid) {
            Log.d(TAG, "ConnectThread: started");
            device = deviceRef;
            deviceUUId = uuid;
        }

        public void run() {
            BluetoothSocket tmp = null;
            Log.i(TAG, "ConnectThread: RUN");

            //Get a BluetoothSocket for a connection with the
            //given BluetoothDevice
            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRFcommSocket using UUID: "
                        + UUID_INSECURE);
                tmp = device.createInsecureRfcommSocketToServiceRecord(deviceUUId);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: " + e.getMessage());
            }

            socket = tmp;

            //Always cancel discovery because it will slow down a connection
            bluetoothAdapter.cancelDiscovery();

            //Make a connection to the BluetoothSocket

            try {
                //This is a blocking call and will only return on a
                //successful connection or an exception
                socket.connect();

                Log.d(TAG, "ConnectThread: connected");
            } catch (IOException e) {
                //Close the socket
                try {
                    socket.close();
                    Log.d(TAG, "ConnectThread: socket closed");
                } catch (IOException e1) {
                    Log.e(TAG, "ConnectThread: Unable to close connection in socket: " + e1.getMessage());
                }
                Log.e(TAG, "ConnectThread: could not connect to UUID" + e.getMessage());
            }

            connected(socket, device);
        }

        public void cancel() {
            try {
                Log.d(TAG, "ConnectThread: Closing client socket");
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: failed to close socket: " + e.getMessage());
            }
        }
    }

    /**
     * Start the communication service. Specifically start AcceptThread to begin a session in
     * listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        //Cancel any thread attempting to make connection
        if(connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if(insecureAcceptThread == null) {
            insecureAcceptThread = new AcceptThread();
            insecureAcceptThread.start();
        }
    }

    /**
     * AcceptThread starts and sits waiting for a connection.
     * Then ConnectThread starts and attempts to make a connection with the other devices.
     * @param device
     *          The paired device.
     * @param uuid
     *          The paired device UUID.
     */
    public void startClient(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startClient: started");

        //TODO ADD NOTIFICATION

        connectThread = new ConnectThread(device, uuid);
        connectThread.start();
    }

    /**
     * The ConnectedThread which is responsible for maintaining the BTConnection,
     * Sending the data and receiving incoming data through input/output streams respectively.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: start");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //TODO new notification here

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
            byte[] buffer = new byte[1024];

            //bytes returned from read()
            int bytes;

            //keep listening to the InputStream until an exception occurs
            while (true) {
                //Read from the InputStream
                try {
                    bytes = inputStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "InputStream: " + incomingMessage);
                } catch (IOException e) {
                    Log.e(TAG, "write: Error reading input: " + e.getMessage());
                    break;
                }
            }
        }

        //Call from the activity to send data to the remote service
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to output stream: " + text);
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to output: " + e.getMessage());
            }
        }

        //Call from activity to shutdown the connection
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "ConnectedThread: Failed to close socket: " + e.getMessage());
            }
        }
    }

    private void connected(final BluetoothSocket mmSocket, final BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: starting");

        //Start the thread to manage the connection and perform transmission
        connectedThread = new ConnectedThread(mmSocket);
        connectThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param bytes
     *          The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] bytes) {
        //Create temporary object
        ConnectedThread r;

        //Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: write called");
        //perform the write
        connectedThread.write(bytes);
    }
}
