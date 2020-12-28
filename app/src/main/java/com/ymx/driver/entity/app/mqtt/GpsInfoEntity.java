package com.ymx.driver.entity.app.mqtt;


import com.ymx.driver.entity.BaseEntity;

/**
 * Created by xuweihua
 * 2020/5/25
 */
public class GpsInfoEntity extends BaseEntity {
    private String lat;
    private String lng;
    private String speed;
    private String driverId;
    private String type;

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    private long currentTime ;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    private String orderNo;
    private String status;
    private String bear;

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getBear() {
        return bear;
    }

    public void setBear(String bear) {
        this.bear = bear;
    }


    public GpsInfoEntity() {

    }

    public GpsInfoEntity(String lat, String lng, String speed, String bear, String driverId, String orderNo, String status) {
        this.lat = lat;
        this.lng = lng;
        this.speed = speed;
        this.bear = bear;
        this.driverId = driverId;
        this.status = status;
        this.orderNo = orderNo;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }


    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
