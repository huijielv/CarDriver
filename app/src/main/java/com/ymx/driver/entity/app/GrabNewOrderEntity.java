package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class GrabNewOrderEntity extends BaseEntity implements  NewOrderEntity {
    private String messageNo;
    private String orderNo;
    private String tips ;
    private List<String> noShowDialogDriverList;
    private List<String> shieldingDriverList;
    private String carPoolDescription;
    private String markupPriceDescription ;
    private String priceDescription;



    public void setMarkupPriceDescription(String markupPriceDescription) {
        this.markupPriceDescription = markupPriceDescription;
    }

    public String getPriceDescription() {
        return priceDescription;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }

    public String getMessageNo() {
        return messageNo;
    }

    public void setMessageNo(String messageNo) {
        this.messageNo = messageNo;
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

    public List<String> getNoShowDialogDriverList() {
        return noShowDialogDriverList;
    }

    public void setNoShowDialogDriverList(List<String> noShowDialogDriverList) {
        this.noShowDialogDriverList = noShowDialogDriverList;
    }

    public List<String> getShieldingDriverList() {
        return shieldingDriverList;
    }

    public void setShieldingDriverList(List<String> shieldingDriverList) {
        this.shieldingDriverList = shieldingDriverList;
    }

    public String getCarPoolDescription() {
        return carPoolDescription;
    }

    public void setCarPoolDescription(String carPoolDescription) {
        this.carPoolDescription = carPoolDescription;
    }

    public String getMarkupPriceDescription() {
        return markupPriceDescription;
    }
}
