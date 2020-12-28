package com.ymx.driver.entity;

public class RangDriverPassgerFinishOederDetailsListItem extends BaseEntity {
    private String headimg;
    private String phone;
    private String orderNo;
    private String payDes;
    private String otherPayDes;
    private int stationType;


    private int payState;

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayDes() {
        return payDes;
    }

    public void setPayDes(String payDes) {
        this.payDes = payDes;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public String getOtherPayDes() {
        return otherPayDes;
    }

    public void setOtherPayDes(String otherPayDes) {
        this.otherPayDes = otherPayDes;
    }

    public int getStationType() {
        return stationType;
    }

    public void setStationType(int stationType) {
        this.stationType = stationType;
    }

}
