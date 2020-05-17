package com.example.mplayer.utils.helpers;

import com.example.mplayer.utils.BluetoothConnectionService;
import com.example.mplayer.utils.SharedResources;

public class BluetoothMessage {
    private static String message = "";

    private static SharedResources resources = SharedResources.getInstance();
    private static BluetoothConnectionService bluetoothConnectionService = new BluetoothConnectionService(resources.getBluetoothAdapter());
    private BluetoothMessage() {

    }

    public static void  changeProgress(final int progress) {
        message = "{command:changeProgress,progress:" + progress + "}";
        bluetoothConnectionService.write(message.getBytes());
    }

    public static void  changeTrack(final boolean direction) {
        if (direction) {
            message = "{command:next}";
            bluetoothConnectionService.write(message.getBytes());
        }

        message = "{command:prev}";
        bluetoothConnectionService.write(message.getBytes());

    }

    public static void changeVolume(final float volume) {
        message = "{command:volume,volume:" + volume + "}";
        bluetoothConnectionService.write(message.getBytes());
    }

    public static void play(final boolean status) {
        if(status) {
            message = "{command:play}";
            bluetoothConnectionService.write(message.getBytes());
        }

        message = "stop";
        bluetoothConnectionService.write(message.getBytes());

    }
}
