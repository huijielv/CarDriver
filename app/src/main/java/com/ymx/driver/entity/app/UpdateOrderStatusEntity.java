package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class UpdateOrderStatusEntity extends BaseEntity {
    private String orderNo;
    private int driverState;
    private String buttonText;
    private int channel;
    private double desLat;
    private double desLng;
    private String desName;
    private int cancalNumber ;

    public double getDesLat() {
        return desLat;
    }

    public void setDesLat(double desLat) {
        this.desLat = desLat;
    }

    public double getDesLng() {
        return desLng;
    }

    public void setDesLng(double desLng) {
        this.desLng = desLng;
    }

    public String getDesName() {
        return desName;
    }

    public void setDesName(String desName) {
        this.desName = desName;
    }


    public UpdateOrderStatusEntity(String orderNo, int driverState, String buttonText) {
        this.orderNo = orderNo;
        this.driverState = driverState;
        this.buttonText = buttonText;
    }

    public UpdateOrderStatusEntity() {

    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getDriverState() {
        return driverState;
    }

    public void setDriverState(int driverState) {
        this.driverState = driverState;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getCancalNumber() {
        return cancalNumber;
    }

    public void setCancalNumber(int cancalNumber) {
        this.cancalNumber = cancalNumber;
    }
}
