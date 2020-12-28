package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class RemoteIncomeListItem extends BaseEntity {
    private String phone;
    private String income;
    private String stateName;
    private int payState;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }
}
