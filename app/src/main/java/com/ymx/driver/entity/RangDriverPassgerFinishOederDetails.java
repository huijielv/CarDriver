package com.ymx.driver.entity;

import java.util.List;

public class RangDriverPassgerFinishOederDetails extends BaseEntity {
    private String orderNo;
    private String startAddress;
    private String endAddress;
    private String payDsc;
    private int driverState;
    private String travelMileage ;
    private String rideNumber ;
    private int  businessType ;

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getDriverState() {
        return driverState;
    }

    public void setDriverState(int driverState) {
        this.driverState = driverState;
    }

    private List<RangDriverPassgerFinishOederDetailsListItem> passengerList;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getPayDsc() {
        return payDsc;
    }

    public void setPayDsc(String payDsc) {
        this.payDsc = payDsc;
    }

    public List<RangDriverPassgerFinishOederDetailsListItem> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<RangDriverPassgerFinishOederDetailsListItem> passengerList) {
        this.passengerList = passengerList;
    }
}
