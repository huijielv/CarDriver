package com.ymx.driver.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.NewOrderEntity;
import com.ymx.driver.entity.app.TransferNewOrderEntity;
import com.ymx.driver.tts.BaiduSpeech;
import com.ymx.driver.ui.main.activity.MainActivity;
import com.ymx.driver.ui.test.TestNewOrderEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;

public class NewOrderTTSController {

    public static NewOrderTTSController ttsManager;
    private Context mContext;
    private BaiduSpeech mTts;
    public boolean isPlaying = false;

    private LinkedList<NewOrderEntity> wordList = new LinkedList();
    private final int TTS_PLAY = 1;
    private final int CHECK_TTS_PLAY = 2;

    Runnable checkAutoTttPlayrunnable = new Runnable() {
        @Override
        public void run() {


            handler.obtainMessage(CHECK_TTS_PLAY).sendToTarget();
            handler.postDelayed(this, 3000);
        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TTS_PLAY:
                    synchronized (mTts) {

                        if (!isPlaying && mTts != null && wordList.size() > 0 && (GrapNewTransferManager.getInstance().grapNewTransferDialog == null || !GrapNewTransferManager.getInstance().isShow())) {
                            isPlaying = true;
                            NewOrderEntity newOrderEntity = wordList.removeFirst();
                            TransferNewOrderEntity transferNewOrderEntity = (TransferNewOrderEntity) newOrderEntity;
                            String playtts = transferNewOrderEntity.getTips();

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
                                    isPlaying = true;

                                    LogUtil.d("test","2222222222222222222");
                                    if (MyLifecycleHandler.isApplicationInForeground()) {
                                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_TRANSFER_NEW_ORDER_CODE, transferNewOrderEntity));
                                    }


                                }

                                @Override
                                public void onSpeechProgressChanged(String s, int i) {
                                    isPlaying = true;
                                }

                                @Override
                                public void onSpeechFinish(String s) {
                                    isPlaying = false;
                                    handler.obtainMessage(1).sendToTarget();
                                }

                                @Override
                                public void onError(String s, SpeechError speechError) {
                                    isPlaying = false;
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

    private NewOrderTTSController(Context context) {
        mContext = context.getApplicationContext();

        if (mTts == null) {
            createSynthesizer();
        }
        handler.postDelayed(checkAutoTttPlayrunnable, 3000);
    }

    private void createSynthesizer() {
        mTts = BaiduSpeech.getInstance(mContext);
    }


    public static NewOrderTTSController getInstance(Context context) {
        if (ttsManager == null) {
            ttsManager = new NewOrderTTSController(context);
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
        if (handler != null) {
            handler.removeCallbacksAndMessages(CHECK_TTS_PLAY);
            handler.removeCallbacksAndMessages(TTS_PLAY);
            handler.removeCallbacks(checkAutoTttPlayrunnable);

        }
    }

    public void onGetText(NewOrderEntity arg1) {
        if (wordList != null) {
            wordList.addLast(arg1);

        }
        handler.obtainMessage(CHECK_TTS_PLAY).sendToTarget();
    }
}
