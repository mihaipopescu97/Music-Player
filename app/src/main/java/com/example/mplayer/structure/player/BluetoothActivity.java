package com.example.mplayer.structure.player;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mplayer.R;

import java.util.ArrayList;
import java.util.List;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "BluetoothActivity";


    private BluetoothAdapter bluetoothAdapter;
    public List<BluetoothDevice> bluetoothDevices;
    public DeviceListAdapter deviceListAdapter;
    private ListView listView;


    //Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            //When discovery find a device
            if(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "broadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "broadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire
     */
    private final BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
               final int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

               switch (mode) {
                   //Device is in Discoverable Mode
                   case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                       Log.d(TAG, "broadcastReceiver2: Discoverability Enabled");
                       break;
                   case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                       Log.d(TAG, "broadcastReceiver2: Discoverability Enabled. Able to receive connections");
                       break;
                   case BluetoothAdapter.SCAN_MODE_NONE:
                       Log.d(TAG, "broadcastReceiver2: Discoverability Disabled. Not able to receive connections");
                       break;
                   case BluetoothAdapter.STATE_CONNECTING:
                       Log.d(TAG, "broadcastReceiver2: Connecting...");
                       break;
                   case BluetoothAdapter.STATE_CONNECTED:
                       Log.d(TAG, "broadcastReceiver2: Connected");
                       break;
               }
            }
        }
    };

    /**
     * Broadcast Receiver for listing devices that are not yet paired
     */
    private BroadcastReceiver broadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                deviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, bluetoothDevices);
                listView.setAdapter(deviceListAdapter);
            }
        }
    };

    /**
     *Broadcast Receiver for checking the bond status
     */
    private BroadcastReceiver broadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if(device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED");
                }
                //case2: creating bond
                if(device.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING");
                }
                //case3: breaking bond
                if(device.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        super.onDestroy();
        unregisterReceiver(broadcastReceiver1);
        unregisterReceiver(broadcastReceiver2);
        unregisterReceiver(broadcastReceiver3);
        unregisterReceiver(broadcastReceiver4);
        bluetoothAdapter.cancelDiscovery();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        CheckBox enableBt = findViewById(R.id.btEnableCheckBox);
        CheckBox visibleBt = findViewById(R.id.btVisibleCheckBox);
        Button discoverButton = findViewById(R.id.findUnpairedDevicesBtn);
        listView = findViewById(R.id.lvNewDevices);

        bluetoothDevices = new ArrayList<>();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listView.setOnItemClickListener(BluetoothActivity.this);

        if(bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported!", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(bluetoothAdapter.isEnabled()) {
            enableBt.setChecked(true);
        }

        enableBt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked) {
                bluetoothAdapter.disable();
                Toast.makeText(BluetoothActivity.this, "Turned off", Toast.LENGTH_SHORT).show();

                IntentFilter btIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(broadcastReceiver1, btIntent);
            } else {
                Intent intentOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentOn, 0);
                Toast.makeText(BluetoothActivity.this, "Turned on", Toast.LENGTH_SHORT).show();

                IntentFilter btIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(broadcastReceiver1, btIntent);
            }
        });

        visibleBt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                getVisible.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivityForResult(getVisible, 0);
                Toast.makeText(BluetoothActivity.this, "Visible for 2 min", Toast.LENGTH_SHORT).show();

                IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                registerReceiver(broadcastReceiver2, intentFilter);
            }
        });

        discoverButton.setOnClickListener(v -> {
            if(bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
                Log.d(TAG, "btnDiscover: canceling discovery");

                //Check bluetooth permissions in manifest
                checkBluetoothPermissions();
                Log.d(TAG, "btnDiscover: starting discovery");
                bluetoothAdapter.startDiscovery();
                IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(broadcastReceiver3, discoverDevicesIntent);
            }

            if(!bluetoothAdapter.isDiscovering()) {
                //Check bluetooth permissions in manifest
                checkBluetoothPermissions();
                Log.d(TAG, "btnDiscover: starting discovery");
                bluetoothAdapter.startDiscovery();
                IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(broadcastReceiver3, discoverDevicesIntent);
            }
        });

        //Broadcast when bond state changes (ie: pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(broadcastReceiver4, filter);
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * int the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBluetoothPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permissions.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permissions.ACCESS_COARSE_LOCATION");
            if(permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
        } else {
            Log.d(TAG, "checkBluetoothPermissions: No need to check permissions. SDK version < LOLLIPOP");
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //first cancel discovery
        bluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You clicked on a device");
        String deviceName = bluetoothDevices.get(position).getName();
        String deviceAddress = bluetoothDevices.get(position).getAddress();

        Log.d(TAG, "onItemClick: device name: " + deviceName);
        Log.d(TAG, "onItemClick: device address: " + deviceAddress);

        //create bond
        //NOTE: Requires API 17+
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.d(TAG, "Trying to pair with " + deviceName);
            bluetoothDevices.get(position).createBond();
        }
    }
}
