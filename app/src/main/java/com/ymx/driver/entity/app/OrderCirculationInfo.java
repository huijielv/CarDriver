package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class OrderCirculationInfo extends BaseEntity {
    private String orderNo;
    private String tips;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
