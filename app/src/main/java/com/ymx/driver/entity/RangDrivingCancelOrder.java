package com.ymx.driver.entity;

public class RangDrivingCancelOrder extends BaseEntity {

    private String orderNo;

    public int getDriverState() {
        return driverState;
    }

    public void setDriverState(int driverState) {
        this.driverState = driverState;
    }

    private int driverState;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }


}
