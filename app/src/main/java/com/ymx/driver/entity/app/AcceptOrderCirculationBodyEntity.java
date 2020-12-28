package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class AcceptOrderCirculationBodyEntity extends BaseEntity {
    private String orderNo ;
    private String lng ;
    private String lat;
    private String currentAddress;

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

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public AcceptOrderCirculationBodyEntity(String orderNo, String lng, String lat, String currentAddress) {
        this.orderNo = orderNo;
        this.lng = lng;
        this.lat = lat;
        this.currentAddress = currentAddress;
    }
}
