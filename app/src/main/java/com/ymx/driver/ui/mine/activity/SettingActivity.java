package com.ymx.driver.ui.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.databinding.ActivitySettingBinding;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.ui.test.TestActivity;
import com.ymx.driver.util.IgnoreBatteryOptUtil;
import com.ymx.driver.viewmodel.mine.SettingViewModel;

/**
 * Created by wuwei
 * 2020/4/24
 * 佛祖保佑       永无BUG
 */
public class SettingActivity extends BaseActivity<ActivitySettingBinding, SettingViewModel> {

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, SettingActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_setting;
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
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.ucPwdMOdify.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                PasswordResetActivity.start(activity);
            }
        });

        viewModel.uc.ucNavSet.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                NavSettingActivity.start(activity);
            }
        });

        viewModel.uc.ucAbout.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                TestActivity.start(activity);
//                AboutActivity.start(activity);

            }
        });

        viewModel.uc.ucLogout.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                viewModel.driverLogout();
            }
        });
        viewModel.uc.ucLogoutSucecess.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                LoginHelper.logout();
            }
        });
        viewModel.uc.ucLogoutFail.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
//                LoginHelper.logout();
            }
        });

        viewModel.uc.ucNavStatusSet.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {


                if (viewModel.navStatus.get() == 0) {
                    YmxCache.setNavStatus(1);
                    viewModel.navStatus.set(1);

                } else if (viewModel.navStatus.get() == 1) {
                    YmxCache.setNavStatus(0);
                    viewModel.navStatus.set(0);
                }
            }
        });

        viewModel.uc.ucPowerSettingsSet.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                IgnoreBatteryOptUtil.openIgnoreBatteryOpt(SettingActivity.this);
            }
        });

    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }
}
