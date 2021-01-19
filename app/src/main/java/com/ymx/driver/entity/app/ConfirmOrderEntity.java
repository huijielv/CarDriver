package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class ConfirmOrderEntity extends BaseEntity {
    private String orderNo;
    private int categoryType;

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
