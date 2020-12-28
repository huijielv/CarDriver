package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class IncomeDetailEntity extends BaseEntity {
    private String dayTime;
    private List<IncomeDetailItem> list;

    public List<IncomeDetailItem> getList() {
        return list;
    }

    public void setList(List<IncomeDetailItem> list) {
        this.list = list;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }
}
