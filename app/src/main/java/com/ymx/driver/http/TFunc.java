package com.ymx.driver.http;

import android.text.TextUtils;

import com.dgrlucky.log.LogX;
import com.ymx.driver.BuildConfig;
import com.ymx.driver.config.HttpConfig;
import com.ymx.driver.entity.http.HttpResult;

import io.reactivex.functions.Function;

/**
 * Created by wuwei
 * 2018/1/12
 * 佛祖保佑       永无BUG
 */


public class TFunc<T> implements Function<HttpResult<T>, T> {


    public TFunc() {

    }

    @Override
    public T apply(HttpResult<T> tReply) throws Exception {
        String error_code = tReply.getCode();
        if (!TextUtils.equals(HttpConfig.HTTP_SUCCESS_CODE, error_code)) {
            if (BuildConfig.DEBUG) {
                LogX.e("请求失败");
            }
            throw new ResultException(tReply.getMessage(), tReply.getCode());
        } else {
            if (BuildConfig.DEBUG) {
                LogX.e("请求成功");
            }
            return tReply.getContent();
        }
    }
}
