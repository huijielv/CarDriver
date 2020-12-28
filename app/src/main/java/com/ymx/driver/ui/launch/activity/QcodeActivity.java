package com.ymx.driver.ui.launch.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;


import androidx.lifecycle.Observer;


import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;

import com.ymx.driver.databinding.ActivityDriverQcodeBinding;

import com.ymx.driver.viewmodel.launch.QcodeViewModel;

public class QcodeActivity extends BaseActivity<ActivityDriverQcodeBinding, QcodeViewModel> {


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_driver_qcode;
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

    }

    @Override
    public void initData() {
        viewModel.queryDriverQCode();
    }

    @Override
    public void initViewObservable() {



        viewModel.uc.back.observe(this, new Observer<Void>() {
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
