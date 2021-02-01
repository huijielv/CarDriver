package com.ymx.driver.mqtt;

import com.ymx.driver.config.MqttConfig;
import com.ymx.driver.mqtt.Factory.CarPoolCancanFactory;
import com.ymx.driver.mqtt.Factory.CarPoolNewOrderFactoty;
import com.ymx.driver.mqtt.Factory.RecoverOrderFactory;
import com.ymx.driver.mqtt.Factory.TransferNewOrderFactory;
import com.ymx.driver.mqtt.inters.MqttConfigFactory;

import java.util.HashMap;
import java.util.Map;

public class MqttConfigFactoryMap {

    private static final Map<String, MqttConfigFactory> cachedFactories = new HashMap<>();

    static {
        //打车拼车取消操作
        cachedFactories.put(MqttConfig.MQTT_CODE_CAR_POOP_CANCAL_ORDER_TYPE, new CarPoolCancanFactory());
        //接送站拼车处理
        cachedFactories.put(MqttConfig.MQTT_CODE_TRANSFER_NEW_ORDER_TYPE, new TransferNewOrderFactory());
        //打车新订单处理逻辑
        cachedFactories.put(MqttConfig.MQTT_CODE_CAR_POOL_NEW_ORDER_TYPE, new CarPoolNewOrderFactoty());
        //打车扫码下车恢复订单处理
        cachedFactories.put(MqttConfig.MQTT_CODE_RECOVER_ORDER_TYPE, new RecoverOrderFactory());

    }

    public static MqttConfigFactory getParserFactory(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }
        MqttConfigFactory parserFactory = cachedFactories.get(type);
        return parserFactory;
    }
}
