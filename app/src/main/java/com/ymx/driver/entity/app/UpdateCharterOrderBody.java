package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class UpdateCharterOrderBody extends BaseEntity {
    private String orderNo;
    private String lng;
    private String lat;
    private String actionType;
    private String currentAddress;
    private String appointmentTime;

    public UpdateCharterOrderBody(String orderNo, String lng, String lat, String actionType, String currentAddress, String appointmentTime) {
        this.orderNo = orderNo;
        this.lng = lng;
        this.lat = lat;
        this.actionType = actionType;
        this.currentAddress = currentAddress;
        this.appointmentTime = appointmentTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
