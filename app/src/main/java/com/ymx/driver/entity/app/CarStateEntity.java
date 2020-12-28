package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class CarStateEntity extends BaseEntity {
    private int carState;
    private int lockState;

    public int getLockState() {
        return lockState;
    }

    public void setLockState(int lockState) {
        this.lockState = lockState;
    }

    public int getCarState() {
        return carState;
    }

    public void setCarState(int carState) {
        this.carState = carState;
    }
}
