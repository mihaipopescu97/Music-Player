package com.example.mplayer.structure.player;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mplayer.R;

import java.util.List;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private LayoutInflater layoutInflater;
    private List<BluetoothDevice> devices;
    private int viewResId;

    DeviceListAdapter(final Context context, final int tvResId, final List<BluetoothDevice> devices) {
        super(context, tvResId, devices);
        this.devices = devices;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.viewResId = tvResId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View contextView = layoutInflater.inflate(viewResId, null);

        BluetoothDevice device = devices.get(position);

        if(device != null) {
            TextView deviceName = convertView.findViewById(R.id.tvDeviceName);
            TextView deviceAddress = convertView.findViewById(R.id.tvDeviceAddress);

            if(deviceName != null) {
                deviceName.setText(device.getName());
            }
            if(deviceAddress != null) {
                deviceAddress.setText(device.getAddress());
            }
        }
        return contextView;
    }
}
