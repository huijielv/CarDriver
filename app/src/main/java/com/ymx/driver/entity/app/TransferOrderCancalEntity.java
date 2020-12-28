package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class TransferOrderCancalEntity extends BaseEntity {
    private String orderNodeNo;
    private String tips;
    private  int  driverState;



    public void setIntegralCompensate(boolean integralCompensate) {
        isIntegralCompensate = integralCompensate;
    }

    private  boolean isIntegralCompensate;
    public int getDriverState() {
        return driverState;
    }

    public void setDriverState(int driverState) {
        this.driverState = driverState;
    }

    private String orderNo;



    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNodeNo() {
        return orderNodeNo;
    }

    public void setOrderNodeNo(String orderNodeNo) {
        this.orderNodeNo = orderNodeNo;
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
}
