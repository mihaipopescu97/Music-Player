//package com.example.mplayer.utils;
//
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.example.mplayer.R;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Set;
//
//import static android.content.ContentValues.TAG;
//
//public class BluetoothActivity extends AppCompatActivity {
//    private final String TAG = "BluetoothActivity";
//    private final static int REQUEST_ENABLE_BT = 1;
//    private BluetoothAdapter bluetoothAdapter;
//    private List<BluetoothDevice> pairedDevices;
//    private HashMap<String, String> deviceDescription;
//
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bluetooth);
//
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        pairedDevices = bluetoothAdapter.getBondedDevices();
//
//        deviceDescription = new HashMap<>();
//
//        if (bluetoothAdapter == null) {
//            Log.e(TAG, "Device doesn't support bluetooth");
//            this.finishAffinity();
//        }
//
//        if(!bluetoothAdapter.isEnabled()) {
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//        }
//
//        if (pairedDevices.size() > 0) {
//            pairedDevices.forEach(device -> {
//                deviceDescription.put(device.getName(), device.getAddress());
//            });
//        }
//
//        //check device
////        if() {
////            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
////            registerReceiver(receiver, filter);
////        }
//
//    }
//
//    // Create a BroadcastReceiver for ACTION_FOUND.
//    private final BroadcastReceiver receiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Discovery has found a device. Get the BluetoothDevice
//                // object and its info from the Intent.
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
//            }
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        unregisterReceiver(receiver);
//    }
//
//    private class ConnectThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final BluetoothDevice mmDevice;
//
//        public ConnectThread(BluetoothDevice device) {
//            // Use a temporary object that is later assigned to mmSocket
//            // because mmSocket is final.
//            BluetoothSocket tmp = null;
//            mmDevice = device;
//
//            try {
//                // Get a BluetoothSocket to connect with the given BluetoothDevice.
//                // MY_UUID is the app's UUID string, also used in the server code.
//                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//            } catch (IOException e) {
//                Log.e(TAG, "Socket's create() method failed", e);
//            }
//            mmSocket = tmp;
//        }
//
//        public void run() {
//            // Cancel discovery because it otherwise slows down the connection.
//            bluetoothAdapter.cancelDiscovery();
//
//            try {
//                // Connect to the remote device through the socket. This call blocks
//                // until it succeeds or throws an exception.
//                mmSocket.connect();
//            } catch (IOException connectException) {
//                // Unable to connect; close the socket and return.
//                try {
//                    mmSocket.close();
//                } catch (IOException closeException) {
//                    Log.e(TAG, "Could not close the client socket", closeException);
//                }
//                return;
//            }
//
//            // The connection attempt succeeded. Perform work associated with
//            // the connection in a separate thread.
//            manageMyConnectedSocket(mmSocket);
//        }
//
//        // Closes the client socket and causes the thread to finish.
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//                Log.e(TAG, "Could not close the client socket", e);
//            }
//        }
//    }
//}
