package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class RemoteInfoEntity extends BaseEntity {
    private String startSite;
    private String endSite;
    private int remoteState;
    private int day;
    private String startTime;
    private String endTime;
    private String departureTime;
    private String todayStartTime;
    private int stationType;
    private int driverType;
    private int lockState;
    private String lockTips;


    public String getLockTips() {
        return lockTips;
    }

    public void setLockTips(String lockTips) {
        this.lockTips = lockTips;
    }

    private List<OperationLineListItem> operationLineList;


    public int getStationType() {
        return stationType;
    }

    public void setStationType(int stationType) {
        this.stationType = stationType;
    }

    public String getTodayStartTime() {
        return todayStartTime;
    }

    public void setTodayStartTime(String todayStartTime) {
        this.todayStartTime = todayStartTime;
    }

    public String getStartSite() {
        return startSite;
    }

    public void setStartSite(String startSite) {
        this.startSite = startSite;
    }

    public String getEndSite() {
        return endSite;
    }

    public void setEndSite(String endSite) {
        this.endSite = endSite;
    }

    public int getRemoteState() {
        return remoteState;
    }

    public void setRemoteState(int remoteState) {
        this.remoteState = remoteState;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }


    public int getDriverType() {
        return driverType;
    }

    public void setDriverType(int driverType) {
        this.driverType = driverType;
    }

    public int getLockState() {
        return lockState;
    }

    public void setLockState(int lockState) {
        this.lockState = lockState;
    }

    public List<OperationLineListItem> getOperationLineList() {
        return operationLineList;
    }

    public void setOperationLineList(List<OperationLineListItem> operationLineList) {
        this.operationLineList = operationLineList;
    }


}
