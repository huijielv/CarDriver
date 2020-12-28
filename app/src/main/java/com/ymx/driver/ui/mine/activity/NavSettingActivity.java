package com.ymx.driver.ui.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.databinding.ActivityNavSettingBinding;
import com.ymx.driver.util.NavUtil;
import com.ymx.driver.viewmodel.mine.NavSettingViewModel;

/**
 * Created by wuwei
 * 2020/4/24
 * 佛祖保佑       永无BUG
 */
public class NavSettingActivity extends BaseActivity<ActivityNavSettingBinding, NavSettingViewModel> {

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, NavSettingActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_nav_setting;
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
        if (NavUtil.isGdMapInstalled(activity)) {
            binding.gaode.setVisibility(View.VISIBLE);
        } else {
            binding.gaode.setVisibility(View.GONE);
        }

        if (NavUtil.isBaiduMapInstalled(activity)) {
            binding.baidu.setVisibility(View.VISIBLE);
        } else {
            binding.baidu.setVisibility(View.GONE);
        }

        if (NavUtil.isTencentMapInstalled(activity)) {
            binding.tencent.setVisibility(View.VISIBLE);
        } else {
            binding.tencent.setVisibility(View.GONE);
        }
        viewModel.select.set(YmxCache.getNavMode());
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
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }
}
