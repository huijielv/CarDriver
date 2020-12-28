package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class GetCharterSystemTime extends BaseEntity {
    private String nextStartTime;
    private  int nextDay;

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
}
