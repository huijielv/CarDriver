package com.ymx.driver.ui.launch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;


import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.databinding.ActivityIntroGuideBinding;
import com.ymx.driver.ui.login.activity.LoginActivity;
import com.ymx.driver.viewmodel.launch.IntroViewModel;

public class IntroActivity extends BaseActivity<ActivityIntroGuideBinding, IntroViewModel> {

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, IntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_intro_guide;
    }

    @Override
    public int initVariableId() {
        return com.ymx.driver.BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding.indicator.setViewPager(binding.viewPager);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {
        viewModel.uc.clickEnter.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                YmxCache.setIntro(true);
                LoginActivity.start(activity);
            }
        });

    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }
}
