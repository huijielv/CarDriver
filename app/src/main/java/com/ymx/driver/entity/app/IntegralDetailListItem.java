package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class IntegralDetailListItem extends BaseEntity {
    private String createTime;
    private String integralValue;
    private String content;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIntegralValue() {
        return integralValue;
    }

    public void setIntegralValue(String integralValue) {
        this.integralValue = integralValue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
