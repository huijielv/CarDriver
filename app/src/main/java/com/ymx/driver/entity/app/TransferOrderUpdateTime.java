package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class TransferOrderUpdateTime extends BaseEntity {
    private String orderNodeNo ;
    private String tips;
    private int state ;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
}
