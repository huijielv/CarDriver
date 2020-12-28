package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class TransferStationRecoverEntity extends BaseEntity {
    private String orderNo;
    private int businessType;
    private String buttonText;
    private String titleText;

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    private int orderType ;
    private int driverState ;
    private int grabOrderShow;
    private int siteId ;

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    private List <TransferStationRecoverLiteItemEntity> passengerList;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public int getDriverState() {
        return driverState;
    }

    public void setDriverState(int driverState) {
        this.driverState = driverState;
    }

    public int getGrabOrderShow() {
        return grabOrderShow;
    }

    public void setGrabOrderShow(int grabOrderShow) {
        this.grabOrderShow = grabOrderShow;
    }

    public List<TransferStationRecoverLiteItemEntity> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<TransferStationRecoverLiteItemEntity> passengerList) {
        this.passengerList = passengerList;
    }
}
