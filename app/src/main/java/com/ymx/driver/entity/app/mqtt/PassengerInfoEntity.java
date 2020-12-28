package com.ymx.driver.entity.app.mqtt;

import com.ymx.driver.entity.BaseEntity;
import com.ymx.driver.entity.app.PassengerItemInfo;

import java.util.List;

public class PassengerInfoEntity extends BaseEntity {
    private String orderNo;
    private String headimg;
    private String phone;
    private double lat;
    private double lng;
    private String srcName;
    private String desName;
    private int driverState;
    private String buttonText;
    private int orderType;
    private int channel;
    private int businessType;
    private String orderTypeName;
    private int circulationState;
    private String circulationTips;
    private String circulationDriverName;
    private String circulationDriverNo;
    private long circulationTimeout;
    private int driverType;
    private String timeDescribe;
    private int transferOrderState;
    private String transferOrderTips;


    public String getTransferOrderTips() {
        return transferOrderTips;
    }

    public void setTransferOrderTips(String transferOrderTips) {
        this.transferOrderTips = transferOrderTips;
    }

    public String getCirculationTips() {
        return circulationTips;
    }

    public void setCirculationTips(String circulationTips) {
        this.circulationTips = circulationTips;
    }

    public String getCirculationDriverName() {
        return circulationDriverName;
    }

    public void setCirculationDriverName(String circulationDriverName) {
        this.circulationDriverName = circulationDriverName;
    }

    public String getCirculationDriverNo() {
        return circulationDriverNo;
    }

    public void setCirculationDriverNo(String circulationDriverNo) {
        this.circulationDriverNo = circulationDriverNo;
    }

    private List<PassengerItemInfo> passengerList;

    public int getCirculationState() {
        return circulationState;
    }

    public void setCirculationState(int circulationState) {
        this.circulationState = circulationState;
    }


    public List<PassengerItemInfo> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<PassengerItemInfo> passengerList) {
        this.passengerList = passengerList;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    private String appointmentTime;

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getDriverState() {
        return driverState;
    }

    public void setDriverState(int driverState) {
        this.driverState = driverState;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
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

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public long getCirculationTimeout() {
        return circulationTimeout;
    }

    public void setCirculationTimeout(long circulationTimeout) {
        this.circulationTimeout = circulationTimeout;
    }

    public int getDriverType() {
        return driverType;
    }

    public void setDriverType(int driverType) {
        this.driverType = driverType;
    }


    public String getTimeDescribe() {
        return timeDescribe;
    }

    public void setTimeDescribe(String timeDescribe) {
        this.timeDescribe = timeDescribe;
    }

    public int getTransferOrderState() {
        return transferOrderState;
    }

    public void setTransferOrderState(int transferOrderState) {
        this.transferOrderState = transferOrderState;
    }


}
