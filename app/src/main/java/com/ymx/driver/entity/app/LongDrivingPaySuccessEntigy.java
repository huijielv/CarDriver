package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class LongDrivingPaySuccessEntigy extends BaseEntity {
    private String driverOrderNo;
    private String paaaengerOrderNo;
    private int payMode;
    private String passengerOrderNo;

    public String getPassengerOrderNo() {
        return passengerOrderNo;
    }

    public void setPassengerOrderNo(String passengerOrderNo) {
        this.passengerOrderNo = passengerOrderNo;
    }

    public int getPayMode() {
        return payMode;
    }

    public void setPayMode(int payMode) {
        this.payMode = payMode;
    }

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
}
