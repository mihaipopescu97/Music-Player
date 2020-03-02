package com.example.mplayer.utils;

import java.util.logging.Handler;

public class BluetoothService {
    private static final String TAG = "BluetoothService";
    private Handler handler;

    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;
    }

    private class ConnectThread extends Thread {

    }
}
