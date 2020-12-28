package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class OrderCirculationEntity extends BaseEntity {
    private String circulationTips;
    private String circulationDriverName;
    private String circulationDriverNo;
    private String orderNo;
    private long circulationTimeout;

    public long getCirculationTimeout() {
        return circulationTimeout;
    }

    public void setCirculationTimeout(long circulationTimeout) {
        this.circulationTimeout = circulationTimeout;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCirculationTips() {
        return circulationTips;
    }

    public void setCirculationTips(String circulationTips) {
        this.circulationTips = circulationTips;
    }

    public String getCirculationDriverName() {
        return circulationDriverName;
    }

    public void setCirculationDriverName(String circulationDriverName) {
        this.circulationDriverName = circulationDriverName;
    }

    public String getCirculationDriverNo() {
        return circulationDriverNo;
    }

    public void setCirculationDriverNo(String circulationDriverNo) {
        this.circulationDriverNo = circulationDriverNo;
    }
}
