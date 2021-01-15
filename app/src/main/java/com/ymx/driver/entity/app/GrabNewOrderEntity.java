package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class GrabNewOrderEntity extends BaseEntity implements  NewOrderEntity {
    private String messageNo;
    private String orderNo;
    private String tips ;
    private List<String> noShowDialogDriverList;
    private List<String> shieldingDriverList;



    private String orderTypeDescription;



    private int rideNumber;
    private double markupPrice;
    private double price;

    public double getMarkupPrice() {
        return markupPrice;
    }

    public void setMarkupPrice(double markupPrice) {
        this.markupPrice = markupPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private String startAddress;
    private String endAddress;





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



    public String getMessageNo() {
        return messageNo;
    }

    public void setMessageNo(String messageNo) {
        this.messageNo = messageNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public List<String> getNoShowDialogDriverList() {
        return noShowDialogDriverList;
    }

    public void setNoShowDialogDriverList(List<String> noShowDialogDriverList) {
        this.noShowDialogDriverList = noShowDialogDriverList;
    }

    public List<String> getShieldingDriverList() {
        return shieldingDriverList;
    }

    public void setShieldingDriverList(List<String> shieldingDriverList) {
        this.shieldingDriverList = shieldingDriverList;
    }



    public String getOrderTypeDescription() {
        return orderTypeDescription;
    }

    public void setOrderTypeDescription(String orderTypeDescription) {
        this.orderTypeDescription = orderTypeDescription;
    }


    public int getRideNumber() {
        return rideNumber;
    }

    public void setRideNumber(int rideNumber) {
        this.rideNumber = rideNumber;
    }

}
