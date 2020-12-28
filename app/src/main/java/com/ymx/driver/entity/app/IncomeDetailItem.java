package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class IncomeDetailItem extends BaseEntity {
    private String time;
    private String typeName;
    private String monery;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getMonery() {
        return monery;
    }

    public void setMonery(String monery) {
        this.monery = monery;
    }
}
