package com.ymx.driver.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPoolUtils {
    /**
     * 播放音频文件
     *
     * @param context
     * @param resId   音频文件 R.raw.xxx
     */
    public static boolean isPlay =false;

    public static void play(Context context, int resId) {
        isPlay = true;
        SoundPool soundPool;
        // 版本兼容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        } else {
            //第一个参数是可以支持的声音数量，第二个是声音类型，第三个是声音品质
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        int soundID = soundPool.load(context, resId, 1);
        // 该方法防止sample not ready错误
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

                soundPool.play(
                        soundID,  //声音id
                        1, //左声道
                        1, //右声道
                        1, //播放优先级【0表示最低优先级】
                        0, //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                        1);//播放速度【1是正常，范围从0~2】一般为1


            }
        });



    }

}
