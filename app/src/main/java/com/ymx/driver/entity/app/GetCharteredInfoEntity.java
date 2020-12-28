package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class GetCharteredInfoEntity extends BaseEntity {
    private String drivingCity;
    private int carState;
    private String orderNo;



    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDrivingCity() {
        return drivingCity;
    }

    public void setDrivingCity(String drivingCity) {
        this.drivingCity = drivingCity;
    }

    public int getCarState() {
        return carState;
    }

    public void setCarState(int carState) {
        this.carState = carState;
    }

    public String getOrderNo() {
        return orderNo;
    }
}
