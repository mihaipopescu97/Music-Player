package com.example.mplayer.utils.helpers;

public class BluetoothMessage {
    private static String message = "";
    private BluetoothMessage() {

    }

    public static byte[] changeProgress(final int progress) {
        message = "{command:changeProgress,progress:" + progress + "}";
        return message.getBytes();
    }

    public static byte[] changeTrack(final boolean direction) {
        if (direction) {
            message = "{command:next}";
            return message.getBytes();
        }

        message = "{command:prev}";
        return message.getBytes();

    }

    public static byte[] changeVolume(final float volume) {
        message = "{command:volume,volume:" + volume + "}";
        return message.getBytes();
    }

    public static byte[] play(final boolean status) {
        if(status) {
            message = "{command:play}";
            return message.getBytes();
        }

        message = "stop";
        return message.getBytes();

    }
}
