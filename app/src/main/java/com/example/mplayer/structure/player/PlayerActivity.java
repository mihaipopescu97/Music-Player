package com.example.mplayer.structure.player;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mplayer.R;
import com.example.mplayer.structure.body.management.activities.BaseActivity;
import com.example.mplayer.utils.SharedResources;
import com.example.mplayer.utils.enums.PlayType;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PlayerActivity extends AppCompatActivity {


    private Button playBtn;
    private SeekBar positionBar;
    private TextView elapsedTimeLabel;
    private TextView remainingTimeLabel;
    private MediaPlayer mp;
    private int totalTime;

    private LocalPlayerHandler localPlayerHandler;
    private BluetoothPlayerHandler bluetoothPlayerHandler;

    private SharedResources resources;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //Set elements
        playBtn = findViewById(R.id.playButton);
        elapsedTimeLabel = findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = findViewById(R.id.remainingTimeLabel);

        totalTime = 0;

        final List<String> urls = (ArrayList<String>) getIntent().getSerializableExtra("playlist");
        resources = SharedResources.getInstance();

        //Media Player
        mp = new MediaPlayer();

        if(PlayType.FAMILY.label.equals(resources.getPlayType())) {
            //Bluetooth player
            bluetoothPlayerHandler = new BluetoothPlayerHandler(mp, urls, getBaseContext(),
                    BluetoothAdapter.getDefaultAdapter(), resources.getDeviceId());
            mp.setVolume(0f, 0f);
        } else {
            localPlayerHandler = new LocalPlayerHandler(mp, urls);
            mp.setVolume(0.5f, 0.5f);
        }

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
                        //TODO check here
                        if(PlayType.FAMILY.label.equals(resources.getPlayType())) {
                            bluetoothPlayerHandler.changeProgress(seekBar.getProgress());
                        }
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
                        if(PlayType.FAMILY.label.equals(resources.getPlayType())) {
                            bluetoothPlayerHandler.changeVol(volumeNum);
                        } else {
                            mp.setVolume(volumeNum, volumeNum);
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

        //Update position bar & time labels
        new Thread(() -> {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp.isPlaying()) {
            mp.stop();
            mp.release();
        }
    }

    //Play button functionality
    public void playBtnClick(View view) {
        if(PlayType.FAMILY.label.equals(resources.getPlayType())) {
            if(bluetoothPlayerHandler.play()) {
                //Play
                playBtn.setBackgroundResource(R.drawable.stop_button);
            } else {
                //Stop
                playBtn.setBackgroundResource(R.drawable.play_button);
            }
        } else {
            if (localPlayerHandler.play()) {
                //Play
                playBtn.setBackgroundResource(R.drawable.stop_button);
            } else {
                //Stop
                playBtn.setBackgroundResource(R.drawable.play_button);
            }
        }
    }

    //Next button functionality
    public void nextBtnClick(View view) {
        if(PlayType.FAMILY.label.equals(resources.getPlayType())) {
            bluetoothPlayerHandler.next();
        } else {
            localPlayerHandler.next();
        }

    }

    //Previous button functionality
    public void prevBtnClick(View view) {
        if(PlayType.FAMILY.label.equals(resources.getPlayType())) {
            bluetoothPlayerHandler.prev();
        } else {
            localPlayerHandler.prev();
        }
    }

    public void back(View view) {
        startActivity(new Intent(PlayerActivity.this, BaseActivity.class));
    }
}