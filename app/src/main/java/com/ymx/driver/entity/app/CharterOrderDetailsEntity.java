package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class CharterOrderDetailsEntity extends BaseEntity {


    /**
     * orderNo : 275822283809095680
     * headimg :
     * phone : 15816860922
     * srcLat : 22.53332
     * srcLng : 113.93041
     * srcName : 南头深圳市南山区人民政府(桃园路北)
     * desName : 深圳北站
     * payState : 1
     * isNeedBack : 1
     * orderType : 1
     * numberDay : 1天
     * charteredCity : 深圳市
     * charteredDurationMileage : 8小时200公里
     * driverState : 2
     * titleText : 去接乘客
     * isLastDay : 1
     * businessType : 9
     */

    private String orderNo;
    private String headimg;
    private String phone;
    private double srcLat;
    private double srcLng;
    private String srcName;
    private String desName;
    private int payState;
    private int isNeedBack;
    private int orderType;
    private String numberDay;
    private String charteredCity;
    private String charteredDurationMileage;
    private int driverState;
    private String titleText;
    private int isLastDay;
    private int businessType;


    private String startSite;
    private String endStie;
    private int isTimeout;
    private int time;
    private String timeText;
    private String feeText;
    private String appointmentTime;
    private String startAddress;

    private List<TravelListEntity> travelList;
    private List<CharteredOrderListItem> costList;
    private String nextStartTime;
    private int nextDay;

    public String getNextStartTime() {
        return nextStartTime;
    }

    public void setNextStartTime(String nextStartTime) {
        this.nextStartTime = nextStartTime;
    }

    public int getNextDay() {
        return nextDay;
    }

    public void setNextDay(int nextDay) {
        this.nextDay = nextDay;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
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

    public double getSrcLat() {
        return srcLat;
    }

    public void setSrcLat(double srcLat) {
        this.srcLat = srcLat;
    }

    public double getSrcLng() {
        return srcLng;
    }

    public void setSrcLng(double srcLng) {
        this.srcLng = srcLng;
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

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public int getIsNeedBack() {
        return isNeedBack;
    }

    public void setIsNeedBack(int isNeedBack) {
        this.isNeedBack = isNeedBack;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getNumberDay() {
        return numberDay;
    }

    public void setNumberDay(String numberDay) {
        this.numberDay = numberDay;
    }

    public String getCharteredCity() {
        return charteredCity;
    }

    public void setCharteredCity(String charteredCity) {
        this.charteredCity = charteredCity;
    }

    public String getCharteredDurationMileage() {
        return charteredDurationMileage;
    }

    public void setCharteredDurationMileage(String charteredDurationMileage) {
        this.charteredDurationMileage = charteredDurationMileage;
    }

    public int getDriverState() {
        return driverState;
    }

    public void setDriverState(int driverState) {
        this.driverState = driverState;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public int getIsLastDay() {
        return isLastDay;
    }

    public void setIsLastDay(int isLastDay) {
        this.isLastDay = isLastDay;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public String getStartSite() {
        return startSite;
    }

    public void setStartSite(String startSite) {
        this.startSite = startSite;
    }

    public String getEndStie() {
        return endStie;
    }

    public void setEndStie(String endStie) {
        this.endStie = endStie;
    }

    public int getIsTimeout() {
        return isTimeout;
    }

    public void setIsTimeout(int isTimeout) {
        this.isTimeout = isTimeout;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public String getFeeText() {
        return feeText;
    }

    public void setFeeText(String feeText) {
        this.feeText = feeText;
    }


    public List<TravelListEntity> getTravelList() {
        return travelList;
    }

    public void setTravelList(List<TravelListEntity> travelList) {
        this.travelList = travelList;
    }

    public List<CharteredOrderListItem> getCostList() {
        return costList;
    }

    public void setCostList(List<CharteredOrderListItem> costList) {
        this.costList = costList;
    }

}


