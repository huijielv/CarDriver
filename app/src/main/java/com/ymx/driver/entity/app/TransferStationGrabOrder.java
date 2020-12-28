package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class TransferStationGrabOrder extends BaseEntity {
    private  int isChooseTrip ;
    private String orderNodeNo ;
    private String orderNo;

    public int getIsChooseTrip() {
        return isChooseTrip;
    }

    public void setIsChooseTrip(int isChooseTrip) {
        this.isChooseTrip = isChooseTrip;
    }

    public String getOrderNodeNo() {
        return orderNodeNo;
    }

    public void setOrderNodeNo(String orderNodeNo) {
        this.orderNodeNo = orderNodeNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
