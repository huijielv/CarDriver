package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class InterralCommodityEntity extends BaseEntity {
    private int integral;
    private List<InterralCommodityListItem> exchangeList;

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public List<InterralCommodityListItem> getExchangeList() {
        return exchangeList;
    }

    public void setExchangeList(List<InterralCommodityListItem> exchangeList) {
        this.exchangeList = exchangeList;
    }
}
