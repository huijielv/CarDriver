package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class CarpoolGrabOrderEntity extends BaseEntity {
    private  String orderNodeNo ;
    private int categoryType;



    public String getOrderNodeNo() {
        return orderNodeNo;
    }

    public void setOrderNodeNo(String orderNodeNo) {
        this.orderNodeNo = orderNodeNo;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }
}
