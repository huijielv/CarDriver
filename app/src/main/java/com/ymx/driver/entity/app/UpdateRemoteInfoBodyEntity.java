package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;
import com.ymx.driver.ui.mine.activity.PasswordResetActivity;

public class UpdateRemoteInfoBodyEntity extends BaseEntity {
    private int opType;
    private int day;
    private String startTime;
    private String endTime;
    private int state;

    public UpdateRemoteInfoBodyEntity(int opType, int day, String startTime, String endTime, int state) {
        this.opType = opType;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
