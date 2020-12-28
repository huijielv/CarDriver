package com.ymx.driver.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class PlayMediaPlayerManager {
    private static String TAG = "PlayMediaPlayerManager";
    private MediaPlayer mAudio;
    private int mStreamType = AudioManager.STREAM_MUSIC;
    private AudioManager mAudioManager;
    private Context mContext;
    public static PlayMediaPlayerManager vicePlayManager;

    public PlayMediaPlayerManager(Context context) {
        this.mContext = context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public static PlayMediaPlayerManager getInstance(Context context) {
        if (vicePlayManager == null) {
            vicePlayManager = new PlayMediaPlayerManager(context);
        }
        return vicePlayManager;
    }

    private void openMediaPlayer(int ids) throws IOException {
        if (mAudio != null) {
            mAudio.release();
        }
        try {
            mAudio = new MediaPlayer();
            AssetFileDescriptor afd = mContext.getResources()
                    .openRawResourceFd(ids);
            mAudio.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());

            mAudio.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {


                    stop();


                    return true;
                }
            });

            mAudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {


                }
            });

            mAudio.setAudioStreamType(mStreamType);
            mAudio.prepare();
        } catch (Exception exception) {
            exception.printStackTrace();
            LogUtil.d(TAG, " @@@ ");


        }
    }

    public void play(int ids) {

        try {
            openMediaPlayer(ids);
        } catch (Exception ex) {

            mAudio = null;
        }
        if (mAudio != null) {

            mAudio.start();
            // }
        }
    }

    /**
     * Stops a playing ringtone.
     */
    public void stop() {
        if (mAudio != null) {
            mAudio.reset();
            mAudio.release();
            mAudio = null;
        }
    }


    public boolean isPlaying() {
        return mAudio != null && mAudio.isPlaying();
    }

}
