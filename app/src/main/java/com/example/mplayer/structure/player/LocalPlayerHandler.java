package com.example.mplayer.structure.player;

import android.media.MediaPlayer;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class LocalPlayerHandler extends PlayerHandler{
    private int pos;

    LocalPlayerHandler(final MediaPlayer mp, final List<String> urls) {
        super(mp, urls);
        pos = 0;
        init(pos);
    }

    boolean play() {
        if(!mp.isPlaying()) {
            mp.start();
            return true;
        } else {
            mp.pause();
            return false;
        }
    }

    void next() {
        pos += 1;
        if(pos == urls.size()) {
            pos = 0;
        }
        init(pos);
    }

    void prev() {
        if(pos == 0) {
            pos = urls.size() - 1;
        } else {
            pos -= 1;
        }
        init(pos);
    }
}
