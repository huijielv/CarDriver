package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class TravelListEntity extends BaseEntity {
    private String travelTime;
    private List<CharterOrderDataListEntity> dataList;

    public List<CharterOrderDataListEntity> getDataList() {
        return dataList;
    }

    public void setDataList(List<CharterOrderDataListEntity> dataList) {
        this.dataList = dataList;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }
}
