package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class QcodeEntity extends BaseEntity {
    private String driverHeadPicUrl;
    private String qrcode;
    private String driverName;
    private String belongCompany;
    private String driverNo;

    public String getDriverHeadPicUrl() {
        return driverHeadPicUrl;
    }

    public void setDriverHeadPicUrl(String driverHeadPicUrl) {
        this.driverHeadPicUrl = driverHeadPicUrl;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getBelongCompany() {
        return belongCompany;
    }

    public void setBelongCompany(String belongCompany) {
        this.belongCompany = belongCompany;
    }

    public String getDriverNo() {
        return driverNo;
    }

    public void setDriverNo(String driverNo) {
        this.driverNo = driverNo;
    }
}
