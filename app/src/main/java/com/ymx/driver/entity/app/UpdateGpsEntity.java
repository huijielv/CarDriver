package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class UpdateGpsEntity extends BaseEntity {
    private String orderNo;
    private int isRemoveOrderNo;

    public int getIsRemoveOrderNo() {
        return isRemoveOrderNo;
    }

    public void setIsRemoveOrderNo(int isRemoveOrderNo) {
        this.isRemoveOrderNo = isRemoveOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }


}
