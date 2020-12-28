package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class FindCarpoolingSiteResultEntity extends BaseEntity {
    private int carState;
    private List<String> siteList;
    private String orderNo;
    private List<SiteOrderListItem> siteOrderList;
    private List<String> siteIdList;

    public int getCarState() {
        return carState;
    }

    public void setCarState(int carState) {
        this.carState = carState;
    }

    public List<String> getSiteList() {
        return siteList;
    }

    public void setSiteList(List<String> siteList) {
        this.siteList = siteList;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<SiteOrderListItem> getSiteOrderList() {
        return siteOrderList;
    }

    public void setSiteOrderList(List<SiteOrderListItem> siteOrderList) {
        this.siteOrderList = siteOrderList;
    }

    public List<String> getSiteIdList() {
        return siteIdList;
    }

    public void setSiteIdList(List<String> siteIdList) {
        this.siteIdList = siteIdList;
    }
}
