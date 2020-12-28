package com.ymx.driver.entity.app;


import com.ymx.driver.entity.BaseEntity;

public class UpdateStartAddressEntity extends BaseEntity {
    private String orderNo;
    private String tips;
    private String voiceText;

    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
