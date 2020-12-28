package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class CharterCancalOrder extends BaseEntity {
    private String driverOrderNo ;
    private String tips;

    public boolean isIntegralCompensate() {
        return isIntegralCompensate;
    }

    public void setIntegralCompensate(boolean integralCompensate) {
        isIntegralCompensate = integralCompensate;
    }

    private boolean isIntegralCompensate ;

    public String getDriverOrderNo() {
        return driverOrderNo;
    }

    public void setDriverOrderNo(String driverOrderNo) {
        this.driverOrderNo = driverOrderNo;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
