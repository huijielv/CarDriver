package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class PassengerItemInfo extends BaseEntity {
    private String headimg;
    private String phone;
    private String rideNumber;
    private String orderNo;
    private double lng;
    private double lat;
    private String address;
    private String drivingTime ;
    private int payState;
    private int priceMarkupState;
    private String priceMarkup;

    public String getPriceMarkup() {
        return priceMarkup;
    }

    public void setPriceMarkup(String priceMarkup) {
        this.priceMarkup = priceMarkup;
    }

    private String startAddress;
    private String endAddress ;
    private String priceMarkupTitle;

    public String getPriceMarkupTitle() {
        return priceMarkupTitle;
    }

    public void setPriceMarkupTitle(String priceMarkupTitle) {
        this.priceMarkupTitle = priceMarkupTitle;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public String getDrivingTime() {
        return drivingTime;
    }

    public void setDrivingTime(String drivingTime) {
        this.drivingTime = drivingTime;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRideNumber() {
        return rideNumber;
    }

    public void setRideNumber(String rideNumber) {
        this.rideNumber = rideNumber;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPriceMarkupState() {
        return priceMarkupState;
    }

    public void setPriceMarkupState(int priceMarkupState) {
        this.priceMarkupState = priceMarkupState;
    }


}
