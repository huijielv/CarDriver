package com.ymx.driver.mqtt;

import com.ymx.driver.config.MqttConfig;
import com.ymx.driver.mqtt.Factory.CarPoolCancanFactory;
import com.ymx.driver.mqtt.Factory.CarPoolNewOrderFactoty;
import com.ymx.driver.mqtt.Factory.TransferNewOrderFactory;
import com.ymx.driver.mqtt.inters.MqttConfigFactory;

import java.util.HashMap;
import java.util.Map;

public class MqttConfigFactoryMap {

    private static final Map<String, MqttConfigFactory> cachedFactories = new HashMap<>();

    static {
        cachedFactories.put(MqttConfig.MQTT_CODE_CAR_POOP_CANCAL_ORDER_TYPE, new CarPoolCancanFactory());
        cachedFactories.put(MqttConfig.MQTT_CODE_TRANSFER_NEW_ORDER_TYPE, new TransferNewOrderFactory());
        cachedFactories.put(MqttConfig.MQTT_CODE_CAR_POOL_NEW_ORDER_TYPE, new CarPoolNewOrderFactoty());
    }

    public static MqttConfigFactory getParserFactory(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }
        MqttConfigFactory parserFactory = cachedFactories.get(type);
        return parserFactory;
    }
}
