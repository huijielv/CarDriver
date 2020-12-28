package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class RangDrivingDayChoose extends BaseEntity {
    private String day;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
