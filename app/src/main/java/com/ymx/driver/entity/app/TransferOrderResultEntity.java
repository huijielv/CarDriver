package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class TransferOrderResultEntity extends BaseEntity {
    private int transferOrderState;
    private String transferOrderTips;
    private String tips ;


    public int getTransferOrderState() {
        return transferOrderState;
    }

    public void setTransferOrderState(int transferOrderState) {
        this.transferOrderState = transferOrderState;
    }

    public String getTransferOrderTips() {
        return transferOrderTips;
    }

    public void setTransferOrderTips(String transferOrderTips) {
        this.transferOrderTips = transferOrderTips;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

}
