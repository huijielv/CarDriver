package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class CharteredOrderListItem extends BaseEntity {
    private String feeName;
    private String fee ;

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
