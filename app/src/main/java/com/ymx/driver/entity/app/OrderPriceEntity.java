package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class OrderPriceEntity extends BaseEntity {
    private String orderNo ;
    private String amount ;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
