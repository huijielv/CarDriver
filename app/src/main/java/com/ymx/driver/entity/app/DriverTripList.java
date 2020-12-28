package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class DriverTripList extends BaseEntity {
    List<TripOrderList> content;

    public List<TripOrderList> getContent() {
        return content;
    }

    public void setContent(List<TripOrderList> content) {
        this.content = content;
    }
}
