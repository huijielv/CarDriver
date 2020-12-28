package com.ymx.driver.entity;

public class SelectTimeEntity extends BaseEntity {
    private String time;
    private int SelectTimeType;

    public SelectTimeEntity(String time, int selectTimeType) {
        this.time = time;
        SelectTimeType = selectTimeType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSelectTimeType() {
        return SelectTimeType;
    }

    public void setSelectTimeType(int selectTimeType) {
        SelectTimeType = selectTimeType;
    }
}
