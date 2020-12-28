package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class DayIncomeDetailEntity extends BaseEntity {
    private String day ;
    private String totalIncome;
    private List<DayIncomItemEntity> list;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public List<DayIncomItemEntity> getList() {
        return list;
    }

    public void setList(List<DayIncomItemEntity> list) {
        this.list = list;
    }
}
