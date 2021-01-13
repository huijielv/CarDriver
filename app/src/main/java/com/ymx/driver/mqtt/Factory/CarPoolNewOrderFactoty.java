package com.ymx.driver.mqtt.Factory;

import com.ymx.driver.mqtt.inters.MqttConfigFactory;
import com.ymx.driver.mqtt.inters.SendMessage;
import com.ymx.driver.mqtt.message.CarPoolNewOrderMessage;

public class CarPoolNewOrderFactoty implements MqttConfigFactory {
    @Override
    public SendMessage createMessage() {
        return new CarPoolNewOrderMessage();
    }
}
