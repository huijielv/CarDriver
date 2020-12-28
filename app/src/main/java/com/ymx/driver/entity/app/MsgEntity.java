package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

/**
 * Created by xuweihua
 * 2020/6/14
 */
public class MsgEntity extends BaseEntity {
    private int id;
    private String describe;
    private String userType;
    private String messageIcon;
    private int num;
    private String title;
    private String timeDescribe;

    public String getTimeDescribe() {
        return timeDescribe;
    }

    public void setTimeDescribe(String timeDescribe) {
        this.timeDescribe = timeDescribe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMessageIcon() {
        return messageIcon;
    }

    public void setMessageIcon(String messageIcon) {
        this.messageIcon = messageIcon;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
