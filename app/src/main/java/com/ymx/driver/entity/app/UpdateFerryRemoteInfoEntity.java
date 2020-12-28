package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class UpdateFerryRemoteInfoEntity extends BaseEntity {
    private int lockState;
    private int remoteState;
    private String lockTips;

    public String getLockTips() {
        return lockTips;
    }

    public void setLockTips(String lockTips) {
        this.lockTips = lockTips;
    }

    private List<OperationLineListItem> operationLineList;

    public List<OperationLineListItem> getOperationLineList() {
        return operationLineList;
    }

    public void setOperationLineList(List<OperationLineListItem> operationLineList) {
        this.operationLineList = operationLineList;
    }

    public int getLockState() {
        return lockState;
    }

    public void setLockState(int lockState) {
        this.lockState = lockState;
    }

    public int getRemoteState() {
        return remoteState;
    }

    public void setRemoteState(int remoteState) {
        this.remoteState = remoteState;
    }
}
