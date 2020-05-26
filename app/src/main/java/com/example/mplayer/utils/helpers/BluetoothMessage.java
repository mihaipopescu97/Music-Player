package com.example.mplayer.utils.helpers;

import com.example.mplayer.utils.BluetoothConnectionService;

public class BluetoothMessage {
    private StringBuilder message;
    private BluetoothConnectionService bluetoothConnectionService;

    public BluetoothMessage(final BluetoothConnectionService bluetoothConnectionService) {
        this.bluetoothConnectionService = bluetoothConnectionService;
    }

    public void  changeProgress(final float progress) {
        message = new StringBuilder();
        message.append("progress:");
        message.append(progress);
        bluetoothConnectionService.write(message.toString().getBytes());
    }

    public void  changeTrack(final boolean direction) {
        message = new StringBuilder();
        message.append("change:");
        if (direction) {
            message.append("next");
            bluetoothConnectionService.write(message.toString().getBytes());
        } else {
            message.append("prev");
            bluetoothConnectionService.write(message.toString().getBytes());
        }

    }

    public void changeVolume(final float volume) {
        message = new StringBuilder();
        message.append("sound:");
        message.append(volume);
        bluetoothConnectionService.write(message.toString().getBytes());
    }

    public void play(final boolean status) {
        message = new StringBuilder();
        message.append("play:");
        if(status) {
            message.append("true");
            bluetoothConnectionService.write(message.toString().getBytes());
        } else {
            message.append("false");
            bluetoothConnectionService.write(message.toString().getBytes());
        }
    }
}
