package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class DayIncomItemEntity extends BaseEntity {
    private String content;
    private String income;
    private String stateName;
    private int orderState;
    private int businessType;
    private int driverType;
    private int categoryType ;

    public int getDriverType() {
        return driverType;
    }

    public void setDriverType(int driverType) {
        this.driverType = driverType;
    }

    private List<RemoteIncomeListItem> remoteIncomeList;

    public List<RemoteIncomeListItem> getRemoteIncomeList() {
        return remoteIncomeList;
    }

    public void setRemoteIncomeList(List<RemoteIncomeListItem> remoteIncomeList) {
        this.remoteIncomeList = remoteIncomeList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

}
