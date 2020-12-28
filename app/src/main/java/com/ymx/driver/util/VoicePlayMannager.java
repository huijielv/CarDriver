package com.ymx.driver.util;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class VoicePlayMannager {

    public static final String TAG = VoicePlayMannager.class.getSimpleName();


    private Context mContext;

    private LinkedBlockingDeque<Integer> mPlayModelQueue;

    private ExecutorService mSingleExecutor = null;


    public static VoicePlayMannager vicePlayManager;

    VoicePlayMannager(Context context) {
        this.mContext = context;
    }

    public static VoicePlayMannager getInstance(Context context) {
        if (vicePlayManager == null) {
            vicePlayManager = new VoicePlayMannager(context);
        }
        return vicePlayManager;
    }


    public void play(final int playIds) {


        if (mPlayModelQueue == null) {
            mPlayModelQueue = new LinkedBlockingDeque<Integer>();

        }
        if (mPlayModelQueue.isEmpty()) {

        }

        mPlayModelQueue.add(playIds);

        startPlayWork();
    }


    public void stopSpeaking() {

        if (mSingleExecutor != null) {
            mSingleExecutor.shutdown();
        }
        if (mPlayModelQueue != null) {
            mPlayModelQueue.clear();
        }

    }


    private synchronized void startPlayWork() {


        if (mSingleExecutor == null) {
            mSingleExecutor = Executors.newSingleThreadExecutor();
            mSingleExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            int rawId = mPlayModelQueue.take();
//                            SoundPoolUtils.play(mContext,rawId);

                            while (PlayMediaPlayerManager.getInstance(mContext).isPlaying()) {
                                Thread.sleep(1000);
                            }

                            PlayMediaPlayerManager.getInstance(mContext).play(rawId);


                        } catch (Exception e) {
                            e.printStackTrace();


                        }

                    }

                }

            });
        }
    }


    public synchronized void putTtsModelAtHead(int playText) {


        if (mPlayModelQueue == null) {
            mPlayModelQueue = new LinkedBlockingDeque<Integer>();

        }
        if (mPlayModelQueue.isEmpty()) {

        }

        try {
            mPlayModelQueue.putFirst(playText);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startPlayWork();

    }

}
