package com.ymx.driver.base;

import com.ymx.driver.entity.BaseEntity;

public class PushOrderEntity  extends BaseEntity {
    private String orderNo ;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
