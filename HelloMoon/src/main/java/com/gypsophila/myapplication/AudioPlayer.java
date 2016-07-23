package com.gypsophila.myapplication;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Gypsophila on 2016/6/30.
 */
public class AudioPlayer {
    MediaPlayer mediaPlayer;

    public void play(Context context) {
        stop(); //保证多次点击播放按钮，仅有一个实例
        mediaPlayer = MediaPlayer.create(context, R.raw.one_small_step);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });
        mediaPlayer.start();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
