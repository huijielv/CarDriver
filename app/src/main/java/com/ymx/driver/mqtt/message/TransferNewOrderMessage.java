package com.ymx.driver.mqtt.message;

import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.entity.BaseGrabOrderEntity;
import com.ymx.driver.entity.BaseMqttEntity;
import com.ymx.driver.entity.app.GrabNewOrderEntity;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.mqtt.inters.SendMessage;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.util.NewOrderFilterUtiles;
import com.ymx.driver.util.NewOrderTTSController;
import java.util.List;

public class TransferNewOrderMessage implements SendMessage {
    @Override
    public void sendMessage(String data) {
        BaseMqttEntity<GrabNewOrderEntity> transferNewOrderEntity = BaseMqttEntity.fromJson(data, GrabNewOrderEntity.class);
        if ((YmxCache.getTranferCarState() == 1 || YmxCache.getTranferCarState() == 2) && transferNewOrderEntity != null && transferNewOrderEntity.getData() != null) {
            GrabNewOrderEntity transferNewOrder = transferNewOrderEntity.getData();
            if (!NewOrderFilterUtiles.hasOrder(transferNewOrder.getMessageNo())) {
                return;
            }
            List<String> transferList = transferNewOrder.getShieldingDriverList();
            UserEntity userEntity = LoginHelper.getUserEntity();

            boolean orderFinish = false;

            if (transferList.size() > 0) {
                for (String s : transferList) {
                    if (s.equals(userEntity.getIdNo())) {
                        orderFinish = true;
                        break;
                    }

                }
            }

            if (orderFinish) {
                return;
            }

            BaseGrabOrderEntity baseGrabOrderEntity = new BaseGrabOrderEntity.
                    Builder().
                    setTtsMsg(transferNewOrderEntity.getData().getTips()).
                    setNewOrder(transferNewOrderEntity.getData()).
                    setOrderNo(transferNewOrderEntity.getData().getOrderNo()).
                    setOrderType(1).
                    build();
            NewOrderTTSController.getInstance(YmxApp.getInstance()).onGetText(baseGrabOrderEntity);
        }
    }
}
