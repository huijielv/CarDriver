package com.ymx.driver.http;

import android.app.Activity;
import android.text.TextUtils;

import com.dgrlucky.log.LogX;
import com.google.gson.JsonSyntaxException;
import com.ymx.driver.BuildConfig;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.config.HttpConfig;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.util.NetworkUtil;
import com.ymx.driver.util.UIUtils;

import java.net.SocketTimeoutException;

import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public abstract class TAddObserver<T> extends TObserver<T> {
    public TAddObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        onRequestStart();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        onRequestEnd();
        if (t != null) {
            onSuccees(t);
        } else {
            onFailure("ret 不是1");
        }
    }

    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        if (e instanceof SocketTimeoutException) {
            if (BuildConfig.DEBUG) {
                LogX.e("请求超时，请检查您的网络");
            }
            onFailure(UIUtils.getString(R.string.http_timeout));
        } else if (e instanceof JsonSyntaxException) {
            if (BuildConfig.DEBUG) {
                LogX.e("数据解析异常");
            }
            onFailure(UIUtils.getString(R.string.http_exception));
        } else if (e instanceof HttpException) {
            if (BuildConfig.DEBUG) {
                LogX.e("服务器异常");
            }
            if (!NetworkUtil.isNetAvailable(UIUtils.getContext())) {
                onFailure(UIUtils.getString(R.string.net_errer));
            } else {
                onFailure(UIUtils.getString(R.string.http_exception));
            }


        } else if (e instanceof ResultException) {
            ResultException resultException = (ResultException) e;
            if (BuildConfig.DEBUG) {
                LogX.e(resultException.getErrMsg());
            }

            if (TextUtils.equals(HttpConfig.HTTP_RETURN_TOKEN_EXPIRE_CODE, resultException.getErrCode())) {
                showTokenExpireDialog();


            } else if (TextUtils.equals(HttpConfig.HTTP_RETURN_MAIN_CODE, resultException.getErrCode())) {
                onFailure(resultException);

            } else if (TextUtils.equals(HttpConfig.HTTP_KICK_OFFLINE_CODE, resultException.getErrCode()) || TextUtils.equals(HttpConfig.HTTP_EXPIRE_CODE, resultException.getErrCode())) {
                LoginHelper.logout();
                onFailure(resultException.getErrMsg());
            } else {
                onFailure(resultException);
            }
        }
    }

    private void showTokenExpireDialog() {
        Activity activity = AppManager.getAppManager().currentActivity();
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            if (LoginHelper.isLogin()) {
                LoginHelper.logout();
            }
            return;
        }
    }

    /**
     * 请求开始
     */
    protected abstract void onRequestStart();

    /**
     * 请求结束
     */
    protected abstract void onRequestEnd();

    /**
     * 返回成功
     *
     * @param t
     * @throws Exception
     */
    protected abstract void onSuccees(T t);

    /**
     * 返回失败
     *
     * @param
     * @throws String
     */
    protected abstract void onFailure(String message);

    /**
     * 返回失败
     *
     * @param
     * @throws Exception
     */
    protected abstract void onFailure(ResultException e) ;





}
