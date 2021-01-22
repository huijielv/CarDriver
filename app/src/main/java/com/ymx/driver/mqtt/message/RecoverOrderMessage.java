package com.ymx.driver.mqtt.message;

import android.content.Intent;

import com.ymx.driver.base.YmxApp;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.BaseMqttEntity;
import com.ymx.driver.entity.app.RecoverOrderEntity;
import com.ymx.driver.mqtt.inters.SendMessage;
import com.ymx.driver.ui.travel.activity.CarPoolDetailsActivity;
import com.ymx.driver.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

public class RecoverOrderMessage implements SendMessage {
    @Override
    public void sendMessage(String data) {

        UIUtils.postDelayTask(new Runnable() {
            @Override
            public void run() {
                BaseMqttEntity<RecoverOrderEntity> info = BaseMqttEntity.fromJson(data, RecoverOrderEntity.class);
                Intent intent = new Intent();
                intent.putExtra(CarPoolDetailsActivity.ORDERI_ID, info.getData().getDriverOrderNo());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(YmxApp.getInstance(), CarPoolDetailsActivity.class);
                YmxApp.getInstance().startActivity(intent);
            }
        }, 2000);

    }
}
