package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;
import com.ymx.driver.mqtt.inters.SendMessage;

public class RecoverOrderEntity extends BaseEntity {
    private String driverOrderNo ;

    public String getDriverOrderNo() {
        return driverOrderNo;
    }

    public void setDriverOrderNo(String driverOrderNo) {
        this.driverOrderNo = driverOrderNo;
    }
}
