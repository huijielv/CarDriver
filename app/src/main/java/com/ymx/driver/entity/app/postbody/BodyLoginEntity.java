package com.ymx.driver.entity.app.postbody;


import com.ymx.driver.entity.BaseEntity;

/**
 * Created by wuwei
 * 2020/5/12
 * 佛祖保佑       永无BUG
 */
public class BodyLoginEntity extends BaseEntity {
    private String mobile;
    private String password;
    private String deviceToken;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public BodyLoginEntity() {
    }

    public BodyLoginEntity(String mobile, String password, String deviceToken) {
        this.mobile = mobile;
        this.password = password;
        this.deviceToken = deviceToken;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
