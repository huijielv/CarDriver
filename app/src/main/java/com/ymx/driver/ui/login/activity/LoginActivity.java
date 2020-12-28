package com.ymx.driver.ui.login.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityLoginBinding;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.ui.main.activity.MainActivity;
import com.ymx.driver.ui.mine.activity.PasswordResetActivity;
import com.ymx.driver.viewmodel.login.LoginViewModel;

import androidx.lifecycle.Observer;

/**
 * Created by wuwei
 * 2020/5/6
 * 佛祖保佑       永无BUG
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {
    private static String TAG = "LoginActivity";

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, LoginActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (extras != null) {
            intent.putExtras(extras);
        }
        Log.i(TAG, "start: ");
        context.startActivity(intent);
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucFogetPwd.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                PasswordResetActivity.start(activity);
            }
        });

        viewModel.uc.ucLoginSuccess.observe(this, new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                LoginHelper.getInstance(activity).loginSuccess(userEntity);
                MainActivity.start(activity);
                doSthIsExit();
            }
        });
    }

    @Override
    protected boolean isExit() {
        return false;
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }
}
