package com.ymx.driver.http;


import com.meituan.android.walle.WalleChannelReader;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.util.SystemUtils;
import com.ymx.driver.util.UIUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wuwei
 * 2018/1/12
 * 佛祖保佑       永无BUG
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addHeader("appChannel", WalleChannelReader.getChannel(UIUtils.getContext(), "default"))
                .addHeader("channel", "3")
                .addHeader("platformType", "1")
                .addHeader("deviceId", SystemUtils.getDeviceId().equals("")?SystemUtils.getWifiMac():SystemUtils.getDeviceId())
                .addHeader("sysVersion", SystemUtils.getSystemVersion())
                .addHeader("appVersion", SystemUtils.getAppVersionCode())
                .addHeader("appSimId", SystemUtils.getSimSerialNumber())
                .addHeader("token", YmxCache.getUserToken())
                .addHeader("appKey", LoginHelper.getUserEntity() != null ? LoginHelper.getUserEntity().getAppkey() : "")
                .build();
        return chain.proceed(request);
    }
}
