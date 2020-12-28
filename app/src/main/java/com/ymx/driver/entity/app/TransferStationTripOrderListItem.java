package com.ymx.driver.entity.app;

public class TransferStationTripOrderListItem {
    private int id ;
    private String orderNo;
    private String siteName;
    private String siteTypeName ;
    private String rideNumber ;
    private String afterDrivingTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteTypeName() {
        return siteTypeName;
    }

    public void setSiteTypeName(String siteTypeName) {
        this.siteTypeName = siteTypeName;
    }

    public String getRideNumber() {
        return rideNumber;
    }

    public void setRideNumber(String rideNumber) {
        this.rideNumber = rideNumber;
    }

    public String getAfterDrivingTime() {
        return afterDrivingTime;
    }

    public void setAfterDrivingTime(String afterDrivingTime) {
        this.afterDrivingTime = afterDrivingTime;
    }
}
