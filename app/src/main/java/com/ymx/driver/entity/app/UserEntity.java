package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wuwei
 * 2020/4/24
 */
@Entity(nameInDb = "self_table")
public class UserEntity extends BaseEntity {

    /**
     * driverId : 1
     * name : xiaoxc
     * phone : 15816860922
     * picPath : null
     * accountBalance : 0
     * idNo : 1000111
     * firstLoginSign : 0
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzaWduZmxhdCI6IkFwcERyaXZlclRva2VuIiwiZXh0IjoxNTg5NzY2Njk1OTMyLCJtb2JpbGUiOiIxNTgxNjg2MDkyMiIsIm5hbWUiOiJ4aWFveGMiLCJhcHBrZXkiOiIwZmZiZjI5MzY5MTI0NDEyYjI0MDA1MTdjOGI1NWJiYyIsInNlc3Npb25UaW1lb3V0IjoyNTkyMDAwMDAsInVzZXJJZCI6MSwiaWRObyI6IjEwMDAxMTEiLCJpYXQiOjE1ODk1MDc0OTU5MzJ9.SeDNrPE7-I77LQkmIjPvaPCUzyg2Tj2bCsoelX0sxVU
     * appkey : 0ffbf29369124412b2400517c8b55bbc
     */

    private String driverId;
    @Unique
    private String uuid;
    private String name;
    private String phone;
    private String servicePhone;
    private String picPath;
    private double accountBalance;
    private String idNo;
    private int firstLoginSign;
    private String token;
    private String appkey;
    private int carState;
    private int driverType;
    private int lockState;
    private String integral;

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public int getLockState() {
        return lockState;
    }

    public void setLockState(int lockState) {
        this.lockState = lockState;
    }


    public UserEntity(String driverId, String uuid, String name, String phone, String servicePhone, String picPath, double accountBalance, String idNo, int firstLoginSign, String token, String appkey, int carState, int driverType, int lockState) {
        this.driverId = driverId;
        this.uuid = uuid;
        this.name = name;
        this.phone = phone;
        this.servicePhone = servicePhone;
        this.picPath = picPath;
        this.accountBalance = accountBalance;
        this.idNo = idNo;
        this.firstLoginSign = firstLoginSign;
        this.token = token;
        this.appkey = appkey;
        this.carState = carState;
        this.driverType = driverType;
        this.lockState = lockState;
    }

    @Generated(hash = 1433178141)
    public UserEntity() {
    }

    @Generated(hash = 1578685637)
    public UserEntity(String driverId, String uuid, String name, String phone, String servicePhone, String picPath, double accountBalance, String idNo, int firstLoginSign, String token, String appkey, int carState, int driverType, int lockState, String integral) {
        this.driverId = driverId;
        this.uuid = uuid;
        this.name = name;
        this.phone = phone;
        this.servicePhone = servicePhone;
        this.picPath = picPath;
        this.accountBalance = accountBalance;
        this.idNo = idNo;
        this.firstLoginSign = firstLoginSign;
        this.token = token;
        this.appkey = appkey;
        this.carState = carState;
        this.driverType = driverType;
        this.lockState = lockState;
        this.integral = integral;
    }
    public String getDriverId() {
        return this.driverId;
    }
    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getServicePhone() {
        return this.servicePhone;
    }
    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }
    public String getPicPath() {
        return this.picPath;
    }
    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
    public double getAccountBalance() {
        return this.accountBalance;
    }
    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
    public String getIdNo() {
        return this.idNo;
    }
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
    public int getFirstLoginSign() {
        return this.firstLoginSign;
    }
    public void setFirstLoginSign(int firstLoginSign) {
        this.firstLoginSign = firstLoginSign;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getAppkey() {
        return this.appkey;
    }
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }
    public int getCarState() {
        return this.carState;
    }
    public void setCarState(int carState) {
        this.carState = carState;
    }
    public int getDriverType() {
        return this.driverType;
    }
    public void setDriverType(int driverType) {
        this.driverType = driverType;
    }
   

}
