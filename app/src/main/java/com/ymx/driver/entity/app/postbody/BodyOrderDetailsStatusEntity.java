package com.ymx.driver.entity.app.postbody;

import com.ymx.driver.entity.BaseEntity;

public class BodyOrderDetailsStatusEntity extends BaseEntity {
    private String orderNo;
    private String currentCoord;
    private String actionType;

    public BodyOrderDetailsStatusEntity() {

    }

    public BodyOrderDetailsStatusEntity(String orderNo, String currentCoord, String actionType) {
        this.orderNo = orderNo;
        this.currentCoord = currentCoord;
        this.actionType = actionType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCurrentCoord() {
        return currentCoord;
    }

    public void setCurrentCoord(String currentCoord) {
        this.currentCoord = currentCoord;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
