package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class SiteOrderList extends BaseEntity {
    private String orderNo;
    private String headimg;
    private String phone;
    private String appointmentTime;
    private String srcName;
    private String desName;
    private String estimateAmount;
    private int isCarpool;
    private int isHotel;
    private int isMonthPay;

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

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
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

    public String getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(String estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public int getIsCarpool() {
        return isCarpool;
    }

    public void setIsCarpool(int isCarpool) {
        this.isCarpool = isCarpool;
    }

    public int getIsHotel() {
        return isHotel;
    }

    public void setIsHotel(int isHotel) {
        this.isHotel = isHotel;
    }

    public int getIsMonthPay() {
        return isMonthPay;
    }

    public void setIsMonthPay(int isMonthPay) {
        this.isMonthPay = isMonthPay;
    }
}
