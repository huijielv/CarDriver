package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class TransferStationRecoverLiteItemEntity extends BaseEntity {
    private String headimg;
    private String phone;
    private String passengerUpTime;
    private String rideNumber;
    private String afterDrivingTime;
    private String srcName;
    private String desName;
    private Double lat;
    private Double lng ;
    private String orderNo;
    private String appointmentTime;

    public int getPassengerState() {
        return passengerState;
    }

    public void setPassengerState(int passengerState) {
        this.passengerState = passengerState;
    }

    private int passengerState;
    private String passengerStateName ;
    private int  payState;



    private String payStateName;


    private int afterDrivingTimeModifyNumber;




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

    public String getAfterDrivingTime() {
        return afterDrivingTime;
    }

    public void setAfterDrivingTime(String afterDrivingTime) {
        this.afterDrivingTime = afterDrivingTime;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getDesName() {
        return desName;
    }

    public void setDesName(String desName) {
        this.desName = desName;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }





    public String getPassengerStateName() {
        return passengerStateName;
    }

    public void setPassengerStateName(String passengerStateName) {
        this.passengerStateName = passengerStateName;
    }





    public String getPayStateName() {
        return payStateName;
    }

    public void setPayStateName(String payStateName) {
        this.payStateName = payStateName;
    }
    public int getAfterDrivingTimeModifyNumber() {
        return afterDrivingTimeModifyNumber;
    }
    public String getPassengerUpTime() {
        return passengerUpTime;
    }

    public void setPassengerUpTime(String passengerUpTime) {
        this.passengerUpTime = passengerUpTime;
    }

    public void setAfterDrivingTimeModifyNumber(int afterDrivingTimeModifyNumber) {
        this.afterDrivingTimeModifyNumber = afterDrivingTimeModifyNumber;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }
}
