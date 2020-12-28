package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;
import com.ymx.driver.entity.MonthIncomeDetailItemEntity;

import java.util.List;

public class MonthIncomeDetailEntity extends BaseEntity {
    private String totalIncome;
    private String month;
    private List<MonthIncomeDetailItemEntity> list;

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<MonthIncomeDetailItemEntity> getList() {
        return list;
    }

    public void setList(List<MonthIncomeDetailItemEntity> list) {
        this.list = list;
    }
}
