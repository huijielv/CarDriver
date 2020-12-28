package com.ymx.driver.entity;

import com.ymx.driver.entity.app.MonthIncomeDayItemEntity;

import java.util.List;

public class MonthIncomeDetailItemEntity {
    private String week;
    private String totalIncome;
    private List<MonthIncomeDayItemEntity> dayList ;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public List<MonthIncomeDayItemEntity> getDayList() {
        return dayList;
    }

    public void setDayList(List<MonthIncomeDayItemEntity> dayList) {
        this.dayList = dayList;
    }
}
