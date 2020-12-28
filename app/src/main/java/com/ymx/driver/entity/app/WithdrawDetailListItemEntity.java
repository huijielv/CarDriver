package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class WithdrawDetailListItemEntity extends BaseEntity {
    private String stateName;
    private String applyTime;
    private String content;
    private int state;

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

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
