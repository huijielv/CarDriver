package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class TransferCancalOrderEntity extends BaseEntity {
    private String orderNo;
    private int  driverState;

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
