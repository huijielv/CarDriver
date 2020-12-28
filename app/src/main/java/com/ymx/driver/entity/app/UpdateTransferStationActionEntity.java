package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class UpdateTransferStationActionEntity extends BaseEntity {
    private String orderNo ;
    private Double lng;
    private Double lat;
    private int actionType ;
    private  String currentAddress ;

    public UpdateTransferStationActionEntity(String orderNo, Double lng, Double lat, int actionType, String currentAddress) {
        this.orderNo = orderNo;
        this.lng = lng;
        this.lat = lat;
        this.actionType = actionType;
        this.currentAddress = currentAddress;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }
}
