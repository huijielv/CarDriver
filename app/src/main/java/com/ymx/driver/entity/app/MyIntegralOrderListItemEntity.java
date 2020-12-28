package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class MyIntegralOrderListItemEntity extends BaseEntity {

    /**
     * orderNo : 619129817843270892487
     * commodityUrl : http:www.baidu.com/upload/img/2734874578349873.png
     * commodityCode : DEHqhqw12134
     * commodityName : 棒棒糖
     * state : 1
     * stateName : 已兑换
     * number : 1
     * useIntegral : 100
     */

    private String orderNo;
    private String commodityUrl;
    private String commodityCode;
    private String commodityName;
    private int state;
    private String stateName;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private String number;
    private String useIntegral;

    public String getUseIntegral() {
        return useIntegral;
    }

    public void setUseIntegral(String useIntegral) {
        this.useIntegral = useIntegral;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCommodityUrl() {
        return commodityUrl;
    }

    public void setCommodityUrl(String commodityUrl) {
        this.commodityUrl = commodityUrl;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }




}
