package com.ymx.driver.entity;

import com.ymx.driver.entity.app.NewOrderEntity;

public class BaseGrabOrderEntity {
    private int orderType;
    private NewOrderEntity newOrder;
    private String ttsMsg;
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    private BaseGrabOrderEntity(Builder builder) {
        orderType = builder.orderType;
        newOrder = builder.newOrder;
        ttsMsg = builder.ttsMsg;
        orderNo = builder.orderNo;
    }

    public NewOrderEntity getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(NewOrderEntity newOrder) {
        this.newOrder = newOrder;
    }


    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getTtsMsg() {
        return ttsMsg;
    }

    public void setTtsMsg(String ttsMsg) {
        this.ttsMsg = ttsMsg;
    }


    public static final class Builder {
        private int orderType;
        private NewOrderEntity newOrder;
        private String ttsMsg;
        private String orderNo;

        public Builder() {
        }

        public Builder setOrderType(int orderType) {
            this.orderType = orderType;
            return this;
        }

        public Builder setNewOrder(NewOrderEntity newOrder) {
            this.newOrder = newOrder;
            return this;
        }

        public Builder setTtsMsg(String ttsMsg) {
            this.ttsMsg = ttsMsg;
            return this;
        }

        public Builder setOrderNo(String orderNo) {
            this.orderNo = orderNo;
            return this;
        }

        public BaseGrabOrderEntity build() {
            return new BaseGrabOrderEntity(this);
        }
    }
}




