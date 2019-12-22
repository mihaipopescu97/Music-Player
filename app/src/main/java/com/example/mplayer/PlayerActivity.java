package com.example.mplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class PlayerActivity extends AppCompatActivity {


    private Button playBtn;
    private SeekBar positionBar;
    private TextView elapsedTimeLabel;
    private TextView remainingTimeLabel;
    private MediaPlayer mp;
    private int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //Set elements
        playBtn = findViewById(R.id.playButton);
        elapsedTimeLabel = findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = findViewById(R.id.remainingTimeLabel);

        //Media Player
        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

        //Position Bar
        positionBar = findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        //Volume Bar
        SeekBar volumeBar = findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        //Update position bar & time labels
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message message = new Message();
                        message.what = mp.getCurrentPosition();
                        handler.sendMessage(message);

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            int currentPosition = msg.what;
            //Update position bar
            positionBar.setProgress(currentPosition);

            //Update labels
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText(remainingTime);

        }
    };

    public String createTimeLabel(int time) {
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        String timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    //Play button functionality
    public void playBtnClick(View view) {
        if (!mp.isPlaying()) {
            //Play
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop_button);
        } else {
            //Stop
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play_button);
        }
    }

    //Next button functionality
    public void nextBtnClick(View view) {


    }

    //Previous button functionality
    public void prevBtnClick(View view) {

    }
}