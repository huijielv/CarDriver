package com.ymx.driver.mqtt.message;

import com.ymx.driver.base.YmxApp;
import com.ymx.driver.entity.BaseGrabOrderEntity;
import com.ymx.driver.entity.BaseMqttEntity;
import com.ymx.driver.entity.app.GrabNewOrderEntity;
import com.ymx.driver.mqtt.inters.SendMessage;
import com.ymx.driver.util.NewOrderFilterUtiles;
import com.ymx.driver.util.NewOrderTTSController;


public class CarPoolNewOrderMessage implements SendMessage {
    @Override
    public void sendMessage(String data) {
        BaseMqttEntity<GrabNewOrderEntity> baseMqttEntity = BaseMqttEntity.fromJson(data, GrabNewOrderEntity.class);
        GrabNewOrderEntity transferNewOrder = baseMqttEntity.getData();
        if (!NewOrderFilterUtiles.hasOrder(transferNewOrder.getMessageNo())) {
            return;
        }
        BaseGrabOrderEntity baseGrabOrderEntity = new BaseGrabOrderEntity.
                Builder().
                setTtsMsg(baseMqttEntity.getData().getTips()).
                setNewOrder(baseMqttEntity.getData()).
                setOrderType(2).
                build();
        NewOrderTTSController.getInstance(YmxApp.getInstance()).onGetText(baseGrabOrderEntity);
    }
}
