package com.ymx.driver.entity.app;

import androidx.databinding.ObservableField;

import com.ymx.driver.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

public class OrderDetailsEntity extends BaseEntity {

    /**
     * orderState : 0
     * stateName : 待确认
     * orderNo : 26188849021583368
     * headPicUrl :
     * startAddress : 深圳北站
     * endAddress : 东莞市市政府
     * costList : [{"feeName":"里程费","feeDesc":"（11）公里","fee":"10.00"},{"feeName":"总计","feeDesc":"","fee":"120.00"}]
     */

    private int orderState;
    private String stateName;
    private String orderNo;
    private String headPicUrl;
    private String startAddress;
    private String endAddress;
    private String mobile;
    private int channel;
    private String time;
    private String areaCode;
    private int businessType;
    private int isVip;
    private String unPayFee;
    private int  driverType;
    private int categoryType;
    private int chargeRuleId;

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public int getChargeRuleId() {
        return chargeRuleId;
    }

    public void setChargeRuleId(int chargeRuleId) {
        this.chargeRuleId = chargeRuleId;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    private String totalFee;

    private List<DetailsCostListEntity> costList;

    public List<DetailsCostListEntity> getCostList() {
        return costList;
    }

    public void setCostList(List<DetailsCostListEntity> costList) {
        this.costList = costList;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUnPayFee() {
        return unPayFee;
    }

    public void setUnPayFee(String unPayFee) {
        this.unPayFee = unPayFee;
    }

    public int getDriverType() {
        return driverType;
    }

    public void setDriverType(int driverType) {
        this.driverType = driverType;
    }


}
