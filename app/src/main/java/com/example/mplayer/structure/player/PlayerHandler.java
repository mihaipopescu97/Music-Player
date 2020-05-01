package com.example.mplayer.structure.player;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class PlayerHandler {
    final MediaPlayer mp;
    final List<String> urls;

    PlayerHandler(MediaPlayer mp, List<String> urls) {
        this.mp = mp;
        this.urls = urls;
    }

    void init(final int ps) {
        try {
            mp.setDataSource(urls.get(ps));
            mp.setAudioAttributes(
                    new AudioAttributes
                            .Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());
            mp.prepare();
            mp.seekTo(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ps != 0) {
            mp.start();
        }
    }
}
