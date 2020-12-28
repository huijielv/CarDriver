package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class TripOrderList extends BaseEntity {
    private String dayTime;
    private String week;
    private List<TripOrderListItem> orderList;

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<TripOrderListItem> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<TripOrderListItem> orderList) {
        this.orderList = orderList;
    }
}
