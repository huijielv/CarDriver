package com.ymx.driver.ui.test;

import com.ymx.driver.entity.app.NewOrderEntity;

public class TestNewOrderEntity implements NewOrderEntity {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
