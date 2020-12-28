package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class SiteOrderListItem extends BaseEntity {
    private String siteName ;
    private String orderNumber;
    private int siteId;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }
}
