package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class  MonthIncomeDayItemEntity extends BaseEntity {
    private String time;
    private String week;
    private String income;
    private String dayTime;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }
}
