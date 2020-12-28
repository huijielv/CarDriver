package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class DriverLockEntity extends BaseEntity {
    private int lockState;
    private String lockTips;
    private int lockType;
    private String tips;

    public int getLockType() {
        return lockType;
    }

    public void setLockType(int lockType) {
        this.lockType = lockType;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getLockState() {
        return lockState;
    }

    public void setLockState(int lockState) {
        this.lockState = lockState;
    }

    public String getLockTips() {
        return lockTips;
    }

    public void setLockTips(String lockTips) {
        this.lockTips = lockTips;
    }
}
