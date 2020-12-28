package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class WechatPayEntity  extends BaseEntity {


    /**
     * appid : wx54216dcae5e1b464
     * noncestr : 1931d65998a64833bd648063a53b6466
     * package : Sign=WXPay
     * partnerid : 1491045482
     * prepayid : wx201423159772602cf09671a81632475600
     * sign : 90B3E958FC3E889293692564A10C57DC
     * timestamp : 1589955795
     */

    private String appid;
    private String noncestr;
    private String pkg;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}


