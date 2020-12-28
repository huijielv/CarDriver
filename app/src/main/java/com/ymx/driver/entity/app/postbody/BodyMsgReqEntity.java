package com.ymx.driver.entity.app.postbody;

import com.ymx.driver.entity.BaseEntity;

/**
 * Created by xuweihua
 * 2020/5/26
 * 获取用户消息列表的请求体
 */
public class BodyMsgReqEntity extends BaseEntity {

    private String driverId;
    private int currentPage;
    private int pageSize;

    public BodyMsgReqEntity() {
    }

    public BodyMsgReqEntity(String driverId, int currentPage, int pageSize) {
        this.driverId = driverId;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
