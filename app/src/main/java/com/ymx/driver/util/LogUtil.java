package com.ymx.driver.util;

import android.util.Log;

import com.ymx.driver.BuildConfig;


/**
 * Created by wuwei on 2017/6/3.
 */

/**
 * log工具类
 */
public class LogUtil {

    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }


    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message);
        }
    }


    public static void w(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message);
        }
    }


    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }
}
