package com.example.mplayer.structure.player;

import android.media.MediaPlayer;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mplayer.utils.helpers.BluetoothMessage;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class BluetoothPlayerHandler extends PlayerHandler{
    private int pos;

    BluetoothPlayerHandler(MediaPlayer mp, List<String> urls) {
        super(mp, urls);
        pos = 0;
        init(0);
    }

    boolean play() {
        if(!mp.isPlaying()) {
            mp.start();
            //TODO
            BluetoothMessage.play(true);
            return true;
        } else {
            mp.pause();
            //TODO
            BluetoothMessage.play(false);
            return false;
        }
    }

    void next() {
        pos += 1;
        if(pos == urls.size()) {
            pos = 0;
        }
        BluetoothMessage.changeTrack(true);
        init(pos);
    }

    void prev() {
        if(pos == 0) {
            pos = urls.size() - 1;
        } else {
            pos -= 1;
        }
        BluetoothMessage.changeTrack(false);
        init(pos);
    }

    void changeVol(final float vol) {
        BluetoothMessage.changeVolume(vol);
    }

    void changeProgress(final int progress) {
        BluetoothMessage.changeProgress(progress);
    }
}
