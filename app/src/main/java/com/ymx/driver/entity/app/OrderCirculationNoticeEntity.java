package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class OrderCirculationNoticeEntity extends BaseEntity {

    private String orderNo;
    private String lineName;
    private String rideNumber;
    private String travelMileage;
    private String voiceTips ;

    public String getVoiceTips() {
        return voiceTips;
    }

    public void setVoiceTips(String voiceTips) {
        this.voiceTips = voiceTips;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getRideNumber() {
        return rideNumber;
    }

    public void setRideNumber(String rideNumber) {
        this.rideNumber = rideNumber;
    }

    public String getTravelMileage() {
        return travelMileage;
    }

    public void setTravelMileage(String travelMileage) {
        this.travelMileage = travelMileage;
    }
}
