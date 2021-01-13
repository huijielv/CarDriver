package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class CarPoolCancalOrderEntity extends BaseEntity {
    private String orderNo;
    private int driverState;
    private String driverOrderNo;
    private String tips;
    boolean isIntegralCompensate;

    public String getDriverOrderNo() {
        return driverOrderNo;
    }

    public void setDriverOrderNo(String driverOrderNo) {
        this.driverOrderNo = driverOrderNo;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public boolean isIntegralCompensate() {
        return isIntegralCompensate;
    }

    public void setIntegralCompensate(boolean integralCompensate) {
        isIntegralCompensate = integralCompensate;
    }

    public int getDriverState() {
        return driverState;
    }

    public void setDriverState(int driverState) {
        this.driverState = driverState;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }


}
