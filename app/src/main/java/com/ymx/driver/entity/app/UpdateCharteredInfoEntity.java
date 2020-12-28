package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class UpdateCharteredInfoEntity extends BaseEntity {
    private String drivingCity;
    private int carState ;

    public int getCarState() {
        return carState;
    }

    public void setCarState(int carState) {
        this.carState = carState;
    }

    public String getDrivingCity() {
        return drivingCity;
    }

    public void setDrivingCity(String drivingCity) {
        this.drivingCity = drivingCity;
    }

}
