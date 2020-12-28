package com.ymx.driver.config;

import com.ymx.driver.BuildConfig;

/**
 * Created by wuwei
 * 2019/12/5
 * 佛祖保佑       永无BUG
 */
public class HttpConfig {
    public static final String HTTP_SUCCESS_CODE = "1";

    public static final String HTTP_KICK_OFFLINE_CODE = "102";

    public static final String HTTP_EXPIRE_CODE = "100";
    public static final String HTTP_RETURN_MAIN_CODE = "-100";
    public static final String HTTP_RETURN_TOKEN_EXPIRE_CODE = "1001";

//    public static final String HTTP_DEBUG_URL = "http://192.168.1.201:18082";


    public static final String HTTP_DEBUG_URL = "http://39.101.196.81:18081/";

    public static final String HTTP_RELEASE_URL = "http://39.101.196.81:18081/";
    public static final int DEFAULT_COOKIE_NETWORK_TIME = 60 * 1000;
    public static final int DEFAULT_COOKIE_NO_NETWORK_TIME = 30 * 24 * 60 * 60 * 1000;


    public static final String BillingRulesDebugPrice = "http://39.101.196.81:5550/price-rules/index.html?";

//    public static final String BillingRulesDebugPrice = "http://192.168.1.202:5550/price-rules/index.html?";

    public static final String BillingPrice = "http://scapp.xysc16.com:5550/price-rules/index.html?";


    public static final String BillingRulesUrl = BuildConfig.DEBUG ? BillingRulesDebugPrice : BillingPrice;

}
