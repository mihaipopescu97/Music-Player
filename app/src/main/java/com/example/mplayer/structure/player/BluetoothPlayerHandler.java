package com.example.mplayer.structure.player;

import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.mplayer.utils.BluetoothConnectionService;
import com.example.mplayer.utils.helpers.BluetoothMessage;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class BluetoothPlayerHandler extends PlayerHandler{
    private static final String TAG = "BluetoothPlayerHandler";
    private int pos;
    private final BluetoothMessage bluetoothMessage;

    BluetoothPlayerHandler(final MediaPlayer mp, final List<String> urls, final BluetoothConnectionService bluetoothConnectionService) {
        super(mp, urls);
        bluetoothMessage = new BluetoothMessage(bluetoothConnectionService);
        pos = 0;
        init(0);
    }

    boolean play() {
        if(!mp.isPlaying()) {
            mp.start();
            Log.i(TAG, "play: write start");
            bluetoothMessage.play(true);
            return true;
        } else {
            mp.pause();
            Log.i(TAG, "play: write stop");
            bluetoothMessage.play(false);
            return false;
        }
    }

    void next() {
        pos += 1;
        if(pos == urls.size()) {
            pos = 0;
        }
        Log.i(TAG, "play: write next");
        bluetoothMessage.changeTrack(true);
        init(pos);
    }

    void prev() {
        if(pos == 0) {
            pos = urls.size() - 1;
        } else {
            pos -= 1;
        }
        Log.i(TAG, "play: write prev");
        bluetoothMessage.changeTrack(false);
        init(pos);
    }

    void changeVol(final float vol) {
        Log.i(TAG, "play: write change vol to " + vol);
        bluetoothMessage.changeVolume(vol);
    }

    void changeProgress(final float progress) {
        Log.i(TAG, "play: write change pro to " + progress);
        bluetoothMessage.changeProgress(progress);
    }
}
