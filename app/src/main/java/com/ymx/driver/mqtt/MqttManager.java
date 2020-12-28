package com.ymx.driver.mqtt;

import android.content.Context;


/**
 * Created by wuwei
 * 2019/10/28
 * 佛祖保佑       永无BUG
 */
public class MqttManager {
    private static volatile MqttManager sMqttManager;
    private final Context mContext;

    private MqttManager(Context context) {
        mContext = context;
    }

    public static MqttManager getInstance(Context context) {
        if (sMqttManager == null) {
            synchronized (MqttManager.class) {
                if (sMqttManager == null) {
                    sMqttManager = new MqttManager(context);
                }
            }
        }
        return sMqttManager;
    }

    public void startMqttService() {
        MQTTService.start(mContext.getApplicationContext(),MQTTService.initService);
    }
}
