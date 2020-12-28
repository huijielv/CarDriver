package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;

import java.util.List;

public class HeadDetailInfoEntity extends BaseEntity {
    private String todayIncome;
    private int todayOrderNumber;
    private String onlineTime;
    private int carState;
    private boolean pendingOrder;
    private int lockState;
    private int driverType;
    private String integral;


    public int getDriverType() {
        return driverType;
    }

    public void setDriverType(int driverType) {
        this.driverType = driverType;
    }

    public int getLockState() {
        return lockState;
    }

    public void setLockState(int lockState) {
        this.lockState = lockState;
    }

    public boolean isPendingOrder() {
        return pendingOrder;
    }

    public void setPendingOrder(boolean pendingOrder) {
        this.pendingOrder = pendingOrder;
    }

    public int getCarState() {
        return carState;
    }

    public void setCarState(int carState) {
        this.carState = carState;
    }

    private List<PassengerInfoEntity> orderInfoList;

    public String getTodayIncome() {
        return todayIncome;
    }

    public void setTodayIncome(String todayIncome) {
        this.todayIncome = todayIncome;
    }

    public int getTodayOrderNumber() {
        return todayOrderNumber;
    }

    public void setTodayOrderNumber(int todayOrderNumber) {
        this.todayOrderNumber = todayOrderNumber;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public List<PassengerInfoEntity> getOrderInfoList() {
        return orderInfoList;
    }

    public void setOrderInfoList(List<PassengerInfoEntity> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}
