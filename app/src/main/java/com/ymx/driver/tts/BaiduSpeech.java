package com.ymx.driver.tts;


import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;
import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.config.AppConfig;
import com.ymx.driver.util.LogUtil;
import java.io.IOException;

/*
 * 百度语音
 * */
public class BaiduSpeech {
    private static final String TAG = "baidutts";
    private static final String APPID = AppConfig.BAIDU_TTS_ID;
    private static final String APPKEY = AppConfig.BAIDU_TTS_KEY ;
    private static final String SECRETKEY = AppConfig.BAIDU_TTS_SELECT_KEY;


    private SpeechSynthesizer mSpeechSynthesizer;
    private Context context;
    private String content = "欢迎使用专车";   //要合成的文本
    private SpeechSynthesizerListener mListener;

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    private TtsMode ttsMode = TtsMode.ONLINE;

    // 设置声音类型
    private String offlineVoice = OfflineResource.VOICE_FEMALE;

    private static volatile BaiduSpeech instance;

    private BaiduSpeech(Context context) {
        this.context = context;
        init();
    }

    public static BaiduSpeech getInstance(Context context) {
        if (instance == null) {
            synchronized (BaiduSpeech.class) {
                if (instance == null) {
                    instance = new BaiduSpeech(context);
                }
            }
        }
        return instance;
    }


    private void init() {

//        LoggerProxy.printable(true); // 日志打印在logcat中
        boolean isMix = ttsMode.equals(TtsMode.MIX);
        boolean isSuccess;

        // 可以换成MessageListener，在logcat中查看日志
        if(mListener == null) {
            mListener = new MessageListener();
        }

        // 1. 获取实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(context);


        // 2. 设置listener
        mSpeechSynthesizer.setSpeechSynthesizerListener(mListener);

        // 3. 设置APPID，APPKEY.secretKey
        int result = mSpeechSynthesizer.setAppId(APPID);
        checkResult(result, "setAppId");
        result = mSpeechSynthesizer.setApiKey(APPKEY, SECRETKEY);
        checkResult(result, "setApiKey");

        // 4. 支持离线的话，需要设置离线模型
//        if (isMix) {
//
//            isSuccess = checkAuth();
//        //            Log.d(TAG, "isSuccess :  "+isSuccess);
//            if (!isSuccess) {
//                return;
//            }

//            OfflineResource offlineResource = createOfflineResource(offlineVoice);
//
//            // 文本模型文件路径 (离线引擎使用)， 注意TEXT_FILENAME必须存在并且可读
//            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
//            // 声学模型文件路径 (离线引擎使用)， 注意TEXT_FILENAME必须存在并且可读
//            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, offlineResource.getModelFilename());
//        }



        // 5. 以下setParam 参数选填。不填写则默认值生效
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-15 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-15 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "6");
        // 设置合成的语调，0-15 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");

        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_NETWORK);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

//        AudioManager.STREAM_MUSIC
        mSpeechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // x. 额外 ： 自动so文件是否复制正确及上面设置的参数
//        Map<String, String> params = new HashMap<>();
//        // 复制下上面的 mSpeechSynthesizer.setParam参数
//        // 上线时请删除AutoCheck的调用
//        if (isMix) {
//            params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
//            params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, offlineResource.getModelFilename());
//        }
//        InitConfig initConfig = new InitConfig(APPID, APPKEY, SECRETKEY, ttsMode, params, listener);
//
//        //  上线时请删除AutoCheck的调用
//        AutoCheck.getInstance(context.getApplicationContext()).check(initConfig, new Handler() {
//            @Override
//            /**
//             * 开新线程检查，成功后回调
//             */
//            public void handleMessage(Message msg) {
//                if (msg.what == 100) {
//                    AutoCheck autoCheck = (AutoCheck) msg.obj;
//                    synchronized (autoCheck) {
//                        String message = autoCheck.obtainDebugMessage();
//                        Log.d(TAG, message); // 可以用下面一行替代，在logcat中查看代码
//                        // Log.w("AutoCheckMessage", message);
//                    }
//                }
//            }
//
//        });

        // 6. 初始化
        result = mSpeechSynthesizer.initTts(ttsMode);
        checkResult(result, "initTts");

    }


    private boolean checkAuth() {
        AuthInfo authInfo = mSpeechSynthesizer.auth(ttsMode);
        if (!authInfo.isSuccess()) {
            // 离线授权需要网站上的应用填写包名。本demo的包名是com.baidu.tts.sample，定义在build.gradle中
            String errorMsg = authInfo.getTtsError().getDetailMessage();
//            Log.d(TAG, "【error】鉴权失败 errorMsg=" + errorMsg);
            return false;
        } else {
//            Log.d(TAG, "验证通过，离线正式授权文件存在。");
            return true;
        }
    }



    private OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(context, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
//            Log.e(TAG, "【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }


    public void playText(String str) {
        this.content = str;
//        if(mListener == null) {
//            mListener = new MessageListener();
//        }
//        mSpeechSynthesizer.setSpeechSynthesizerListener(mListener);
        int result = mSpeechSynthesizer.speak(content);
         Log.d(TAG, " content :  " +  content +  "   result : " + result);
        if (result != 0) {
            Toast.makeText(context, "语音播放失败 Error code : " + result, Toast.LENGTH_SHORT).show();
        }
    }


    public void playText(String str, SpeechSynthesizerListener listener) {
        this.content = str;
        mSpeechSynthesizer.setSpeechSynthesizerListener(listener);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "15");
        int result = mSpeechSynthesizer.speak(content);
        Log.d(TAG, " content :  " +  content +  "   result : " + result);
        LogUtil.d(TAG,  AutoCheck.getInstance(YmxApp.getInstance()).obtainAllMessage());

        if (result != 0) {
            Toast.makeText(context, "语音播放失败 Error code : " + result, Toast.LENGTH_SHORT).show();
        }
    }


    public void setMaxVolume() {
        if(mSpeechSynthesizer != null) {
            mSpeechSynthesizer.setStereoVolume(1.0f, 1.0f);
        }
    }


    private void checkResult(int result, String method) {
        if (result != 0) {
            Log.d(TAG, "error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    /*
     * 释放语音资源
     */
    public void release() {
        this.mSpeechSynthesizer.release();
    }

    public void pause() {
        this.mSpeechSynthesizer.pause();
    }

    public void resume() {
        this.mSpeechSynthesizer.resume();
    }

    public void stop() {
        this.mSpeechSynthesizer.stop();
    }
}

