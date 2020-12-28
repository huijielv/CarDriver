package com.ymx.driver.ui.mine.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.Observer;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityContactUsBinding;
import com.ymx.driver.viewmodel.mine.ContactUsViewModel;

/**
 * Created by wuwei
 * 2020/4/24
 * 佛祖保佑       永无BUG
 */
public class ContactUsActivity extends BaseActivity<ActivityContactUsBinding, ContactUsViewModel> {

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, ContactUsActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_contact_us;
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

        viewModel.uc.ucCall.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                requestPermission(PermissionConfig.PC_CALL_PHONE, false, Manifest.permission.CALL_PHONE);
            }
        });
    }

    @Override
    protected void permissionGranted(int requestCode) {
        if (requestCode == PermissionConfig.PC_CALL_PHONE) {
            try {
                if (TextUtils.isEmpty(viewModel.phoneNum.get())) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + viewModel.phoneNum.get());
                intent.setData(data);
                startActivity(intent);
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }
}
