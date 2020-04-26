package com.example.mplayer.structure.player;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mplayer.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothActivity";

    private CheckBox enableBt;
    private CheckBox visibleBt;
    private ListView listView;
    private TextView btName;

    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private List<String> deviceNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        enableBt = findViewById(R.id.btEnableCheckBox);
        visibleBt = findViewById(R.id.btVisibleCheckBox);
        listView = findViewById(R.id.btListView);
        btName = findViewById(R.id.btTextView);

        pairedDevices = new HashSet<>();
        deviceNames = new ArrayList<>();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btName.setText(getLocalBluetoothName());

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
            } else {
                Intent intentOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentOn, 0);
                Toast.makeText(BluetoothActivity.this, "Turned on", Toast.LENGTH_SHORT).show();
            }
        });

        visibleBt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(getVisible, 0);
                Toast.makeText(BluetoothActivity.this, "Visible for 2 min", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void searchBtn(View view) {
        list();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void list() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        deviceNames.clear();

        pairedDevices.forEach(device  -> deviceNames.add(device.getName()));
        Toast.makeText(BluetoothActivity.this, "Showing devices", Toast.LENGTH_SHORT).show();
        ArrayAdapter adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, deviceNames);
        listView.setAdapter(adapter);
    }

    public String getLocalBluetoothName() {
        if(bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        String name = bluetoothAdapter.getName();
        if(name == null) {
            name = bluetoothAdapter.getName();
        }

        return name;
    }

}
