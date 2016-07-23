package com.gypsophila.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Gypsophila on 2016/6/30.
 */
public class HelloMoonFragment extends Fragment {

    private Button mPlayButton;
    private Button mStopButton;
    private AudioPlayer mPlayer = new AudioPlayer();
    private Button mPauseButton;
    private int currentPos;
    private Button mContinueButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hello_moon, null);
        mPlayButton = (Button) view.findViewById(R.id.hellomoon_playButton);
        mStopButton = (Button) view.findViewById(R.id.hellomoon_stopButton);
        mPauseButton = (Button) view.findViewById(R.id.hellomoon_pauseButton);
        mContinueButton = (Button) view.findViewById(R.id.hellomoon_continueButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.play(getActivity());
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
            }
        });
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer.mediaPlayer != null && mPlayer.mediaPlayer.isPlaying()) {
                    currentPos = mPlayer.mediaPlayer.getCurrentPosition();
                    mPlayer.mediaPlayer.pause();
                }
            }
        });
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer.mediaPlayer != null) {
                    mPlayer.mediaPlayer.seekTo(currentPos);
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }
}
