package com.ymx.driver.entity.app.mqtt;

import com.ymx.driver.entity.BaseEntity;

public class LongDriverSysTemCancalOrder extends BaseEntity {
    private String driverOrderNo;
    private String paaaengerOrderNo;
    private boolean isIntegralCompensate ;

    public String getDriverOrderNo() {
        return driverOrderNo;
    }

    public void setDriverOrderNo(String driverOrderNo) {
        this.driverOrderNo = driverOrderNo;
    }

    public String getPaaaengerOrderNo() {
        return paaaengerOrderNo;
    }

    public void setPaaaengerOrderNo(String paaaengerOrderNo) {
        this.paaaengerOrderNo = paaaengerOrderNo;
    }

    private String tips;
    private int driverState;


    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getDriverState() {
        return driverState;
    }

    public void setDriverState(int driverState) {
        this.driverState = driverState;
    }

    public boolean isIntegralCompensate() {
        return isIntegralCompensate;
    }

    public void setIntegralCompensate(boolean integralCompensate) {
        isIntegralCompensate = integralCompensate;
    }
}
