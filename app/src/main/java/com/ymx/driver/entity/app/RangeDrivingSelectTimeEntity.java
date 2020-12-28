package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class RangeDrivingSelectTimeEntity extends BaseEntity {
    private String time;
    private int  selectType;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSelectType() {
        return selectType;
    }

    public void setSelectType(int selectType) {
        this.selectType = selectType;
    }

    public RangeDrivingSelectTimeEntity(String time, int selectType) {
        this.time = time;
        this.selectType = selectType;
    }
}
