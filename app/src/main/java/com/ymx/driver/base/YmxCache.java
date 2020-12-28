package com.ymx.driver.base;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ymx.driver.config.AppConfig;
import com.ymx.driver.util.SPUtils;

import org.intellij.lang.annotations.JdkConstants;

import java.util.List;

/**
 * Created by wuwei
 * 2019/12/7
 * 佛祖保佑       永无BUG
 */
public class YmxCache {
    public static String getUserAccount() {
        return SPUtils.getString(AppConfig.KEY_UID);
    }

    public static String getUserToken() {
        return SPUtils.getString(AppConfig.KEY_TOKEN);
    }

    public static String getDriverId() {
        return SPUtils.getString(AppConfig.KEY_DRIVERID);
    }

    public static void setUserAccount(String userAccount) {
        SPUtils.saveString(AppConfig.KEY_UID, userAccount);
    }

    public static void setUserToken(String userToken) {
        SPUtils.saveString(AppConfig.KEY_TOKEN, userToken);
    }

    public static void setOrderId(String orderId) {
        SPUtils.saveString(AppConfig.KET_SERVER_ORDERID, orderId);
    }

    public static String getOrderId() {
        return SPUtils.getString(AppConfig.KET_SERVER_ORDERID);
    }

    public static void setCarState(int carState) {
        SPUtils.saveInt(AppConfig.KET_CAR_START, carState);
    }



    public static int getServerStatus() {

        return SPUtils.getInt(AppConfig.KET_SERVER_STATUS, 0);

    }

    public static int getCarState() {

        return SPUtils.getInt(AppConfig.KET_CAR_START, 0);

    }


    public static void setDriverId(String driverId) {
        SPUtils.saveString(AppConfig.KEY_DRIVERID, driverId);
    }

    public static boolean getIntro() {
        return SPUtils.getBoolean(AppConfig.INTRO);
    }

    public static void setIntro(boolean intro) {
        SPUtils.saveBoolean(AppConfig.INTRO, intro);
    }

    public static int getNavMode() {
        return SPUtils.getInt(AppConfig.KEY_NAV_MODE, -1);
    }

    public static void setNavMode(int navMode) {
        SPUtils.saveInt(AppConfig.KEY_NAV_MODE, navMode);
    }

    public static void setServerStatus(int status) {
        SPUtils.saveInt(AppConfig.KET_SERVER_STATUS, status);
    }

    public static boolean isIntro() {
        return YmxCache.getIntro();
    }


    public static void setDeviceToken(String deviceToken) {
        SPUtils.saveString(AppConfig.KET_PUSH_DRIVET_DEVICE_TOKEN, deviceToken);
    }

    public static String getDeviceToken() {
        return SPUtils.getString(AppConfig.KET_PUSH_DRIVET_DEVICE_TOKEN);
    }


    public static int getNavStatus() {

        return SPUtils.getInt(AppConfig.KET_DRIVET_NAV_STATUS, 1);

    }


    public static void setNavStatus(int status) {
        SPUtils.saveInt(AppConfig.KET_DRIVET_NAV_STATUS, status);
    }

    public static void setDriverType(int driverType) {
        SPUtils.saveInt(AppConfig.KET_DRIVET_TYPE, driverType);
    }

    public static void setDriverIntegral(String integral) {
        SPUtils.saveString(AppConfig.KET_DRIVET_INTERGRAL, integral);
    }

    public static String getDriverIntegral() {
        return SPUtils.getString(AppConfig.KET_DRIVET_INTERGRAL);
    }


    public static int getDriverType() {

        return SPUtils.getInt(AppConfig.KET_DRIVET_TYPE, -1);

    }


    public static void setLockDriverLock(int lock) {
        SPUtils.saveInt(AppConfig.KET_DRIVET_LOCK_STATUS, lock);
    }

    public static int getLockDriverLock() {

        return SPUtils.getInt(AppConfig.KET_DRIVET_LOCK_STATUS, 0);

    }


    public static void setDriverSideIdList(List<String> driverSideIdList) {
        Gson gson = new Gson();
        String sideIdList = gson.toJson(driverSideIdList);
        SPUtils.saveString(AppConfig.KET_DRIVET_SIDEIDLIST, sideIdList);
    }

    public static List<String> getDriverSideIdList() {
        String json = SPUtils.getString(AppConfig.KET_DRIVET_SIDEIDLIST);
        if (!TextUtils.isEmpty(json)) {
            Gson gson = new Gson();
            List<String> list = gson.fromJson(json, new TypeToken<List<String>>() {
            }.getType());
            return list;
        }
        return null;
    }

    public static void setTranferCarState(int carState) {
        SPUtils.saveInt(AppConfig.KET_DRIVET_TRANSFER_CAR_TYPE , carState);
    }

    public static int getTranferCarState() {
        return SPUtils.getInt(AppConfig.KET_DRIVET_TRANSFER_CAR_TYPE, 0);
    }

}
