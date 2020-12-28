package com.ymx.driver.entity.http;


import com.ymx.driver.entity.BaseEntity;

/**
 * Created by wuwei
 * 2018/1/18
 * 佛祖保佑       永无BUG
 */
public class HttpErrResult extends BaseEntity {
    /**
     * 0 成功
     */
    private String code;
    /**
     * 给用户的提示信息
     */
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
