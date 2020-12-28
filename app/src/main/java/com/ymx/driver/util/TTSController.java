package com.ymx.driver.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.amap.api.navi.AMapNavi;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.ymx.driver.tts.BaiduSpeech;


import java.util.LinkedList;

public class TTSController {

    public static TTSController ttsManager;
    private Context mContext;
    private BaiduSpeech mTts;
    public  boolean  isPlaying = false;

    private LinkedList<String> wordList = new LinkedList();
    private final int TTS_PLAY = 1;
    private final int CHECK_TTS_PLAY = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TTS_PLAY:
                    synchronized (mTts) {
                        if (!isPlaying && mTts != null && wordList.size() > 0) {
                            isPlaying = true;
                            String playtts = wordList.removeFirst();
                            if (mTts == null) {
                                mTts = BaiduSpeech.getInstance(mContext);
                            }

                            mTts.playText(playtts, new SpeechSynthesizerListener() {
                                @Override
                                public void onSynthesizeStart(String s) {

                                }

                                @Override
                                public void onSynthesizeDataArrived(String s, byte[] bytes, int i, int i1) {

                                }

                                @Override
                                public void onSynthesizeFinish(String s) {

                                }

                                @Override
                                public void onSpeechStart(String s) {
                                    AMapNavi.setTtsPlaying(isPlaying = true);
                                }

                                @Override
                                public void onSpeechProgressChanged(String s, int i) {
                                    isPlaying = true;
                                }

                                @Override
                                public void onSpeechFinish(String s) {
                                    AMapNavi.setTtsPlaying(isPlaying = false);
                                    handler.obtainMessage(1).sendToTarget();
                                }

                                @Override
                                public void onError(String s, SpeechError speechError) {

                                }
                            });
                        }
                    }
                    break;
                case CHECK_TTS_PLAY:
                    if (!isPlaying) {
                        handler.obtainMessage(1).sendToTarget();
                    }
                    break;
                default:
            }

        }
    };

    private TTSController(Context context) {
        mContext = context.getApplicationContext();

        if (mTts == null) {
            createSynthesizer();
        }
    }

    private void createSynthesizer() {
        mTts = BaiduSpeech.getInstance(mContext);
    }



    public static TTSController getInstance(Context context) {
        if (ttsManager == null) {
            ttsManager = new TTSController(context);
        }
        return ttsManager;
    }

    public void stopSpeaking() {
        if (wordList != null) {
            wordList.clear();
        }
        if (mTts != null) {

            mTts.stop();
        }
        isPlaying = false;
    }

    public void destroy() {
        if (wordList != null) {
            wordList.clear();
        }
        if (mTts != null) {
            mTts.release();
        }
    }

    public void onGetText(String arg1) {
        if (wordList != null){
            wordList.addLast(arg1);
        }
        handler.obtainMessage(CHECK_TTS_PLAY).sendToTarget();
    }
}
