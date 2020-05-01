package com.example.mplayer.utils.helpers;

public class BluetoothMessage {
    private static String message = "";

    private BluetoothMessage() {

    }

    public static byte[] changeProgress(final int progress) {
        message = "progress:" + progress;
        return message.getBytes();
    }

    public static byte[] changeTrack(final boolean direction) {
        if (direction) {
            message = "next";
            return message.getBytes();
        }

        message = "prev";
        return message.getBytes();

    }

    public static byte[] changeVolume(final float volume) {
        message = "volume:" + volume;
        return message.getBytes();
    }

    public static byte[] play(final boolean status) {
        if(status) {
            message = "play";
            return message.getBytes();
        }

        message = "stop";
        return message.getBytes();

    }
}
