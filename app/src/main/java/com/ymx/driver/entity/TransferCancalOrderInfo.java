package com.ymx.driver.entity;

public class TransferCancalOrderInfo  {
    private String orderNo;
    private String phoneInfo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPhoneInfo() {
        return phoneInfo;
    }

    public void setPhoneInfo(String phoneInfo) {
        this.phoneInfo = phoneInfo;
    }

    public TransferCancalOrderInfo(String orderNo, String phoneInfo) {
        this.orderNo = orderNo;
        this.phoneInfo = phoneInfo;
    }
}
