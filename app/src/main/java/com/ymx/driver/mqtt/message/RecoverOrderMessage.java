package com.ymx.driver.mqtt.message;

import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.BaseMqttEntity;
import com.ymx.driver.entity.app.RecoverOrderEntity;
import com.ymx.driver.mqtt.inters.SendMessage;

import org.greenrobot.eventbus.EventBus;

public class RecoverOrderMessage implements SendMessage {
    @Override
    public void sendMessage(String data) {
        BaseMqttEntity<RecoverOrderEntity> info = BaseMqttEntity.fromJson(data, RecoverOrderEntity.class);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_CAR_POOL_RECOVER_ORDER_CODE, info.getData()));
    }
}
