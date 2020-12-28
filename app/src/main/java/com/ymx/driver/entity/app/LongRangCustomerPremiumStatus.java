package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class LongRangCustomerPremiumStatus extends BaseEntity {
    private int state;
    private String orderNo;
    private String msg ;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
