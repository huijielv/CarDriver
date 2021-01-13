package com.ymx.driver.mqtt.message;

import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.BaseMqttEntity;
import com.ymx.driver.entity.app.CarPoolCancalOrderEntity;
import com.ymx.driver.entity.app.TransferOrderUpdateTime;
import com.ymx.driver.mqtt.inters.SendMessage;

import org.greenrobot.eventbus.EventBus;

public class CarPoolCancalOrderMessage implements SendMessage {
    @Override
    public void sendMessage(String data) {
        BaseMqttEntity<CarPoolCancalOrderEntity> info = BaseMqttEntity.fromJson(data, CarPoolCancalOrderEntity.class);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_CAR_POOL_CANCAL_ORDER_CODE, info.getData()));
    }
}
