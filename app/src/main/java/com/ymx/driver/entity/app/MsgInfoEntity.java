package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

/**
 * Created by xuweihua
 * 2020/5/26
 */
public class MsgInfoEntity extends BaseEntity {
    private int id;
    private String title;
    private String messageInfo;
    private String timeDescribe;
    private int readState;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public String getTimeDescribe() {
        return timeDescribe;
    }

    public void setTimeDescribe(String timeDescribe) {
        this.timeDescribe = timeDescribe;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }
}
