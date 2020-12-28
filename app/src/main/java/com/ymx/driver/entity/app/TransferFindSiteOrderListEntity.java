package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class TransferFindSiteOrderListEntity extends BaseEntity {
    private String siteName;
    private String titleText;
    private String orderStateName;
    private List<SiteOrderList> siteOrderList;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getOrderStateName() {
        return orderStateName;
    }

    public void setOrderStateName(String orderStateName) {
        this.orderStateName = orderStateName;
    }

    public List<SiteOrderList> getSiteOrderList() {
        return siteOrderList;
    }

    public void setSiteOrderList(List<SiteOrderList> siteOrderList) {
        this.siteOrderList = siteOrderList;
    }
}
