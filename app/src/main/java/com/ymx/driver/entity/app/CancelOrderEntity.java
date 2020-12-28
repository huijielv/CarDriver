package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class CancelOrderEntity extends BaseEntity {
    private String message;
    private String orderNo;
    private boolean isIntegralCompensate;
    private String tips;

    public boolean isIntegralCompensate() {
        return isIntegralCompensate;
    }

    public void setIntegralCompensate(boolean integralCompensate) {
        isIntegralCompensate = integralCompensate;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
