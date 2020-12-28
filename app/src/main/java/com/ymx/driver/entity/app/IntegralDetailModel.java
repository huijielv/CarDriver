package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class IntegralDetailModel extends BaseEntity {
    private int integral ;
    private List<IntegralDetailListItem> integralList;

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public List<IntegralDetailListItem> getIntegralList() {
        return integralList;
    }

    public void setIntegralList(List<IntegralDetailListItem> integralList) {
        this.integralList = integralList;
    }
}
