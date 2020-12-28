package com.ymx.driver.ui.launch.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivitySplashBinding;
import com.ymx.driver.ui.launch.dialog.PermissionDeniedDialog;
import com.ymx.driver.util.SystemUtils;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.launch.SplashViewModel;

/**
 * Created by wuwei
 * 2020/4/20
 * 佛祖保佑       永无BUG
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> {
    private PermissionDeniedDialog permissionDeniedDialog;
    private boolean toAppSetting;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_splash;
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
        requestPermission(PermissionConfig.PC_SPLASH, true,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                SystemUtils.checkedAndroid_Q() ? Manifest.permission.ACCESS_BACKGROUND_LOCATION : Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (toAppSetting) {
            toAppSetting = false;
            initData();
        }
    }

    @Override
    protected void permissionGranted(int requestCode) {
        UIUtils.postDelayTask(new Runnable() {
            @Override
            public void run() {
                AdvertisingActivity.start(activity);
                doSthIsExit();
            }
        }, 2000);
    }

    @Override
    public void showPermissionDialog(int requestCode) {
        super.showPermissionDialog(requestCode);

        dismissDialog();
        permissionDeniedDialog = new PermissionDeniedDialog(activity)
                .setOnDialogListener(new PermissionDeniedDialog.Listener() {
                    @Override
                    public void negative(Dialog dialog) {
                        dialog.dismiss();
                        permissionGranted(requestCode);
                    }

                    @Override
                    public void positive(Dialog dialog) {
                        dialog.dismiss();
                        toAppSetting = true;
                        startAppSettings();
                    }
                });
        permissionDeniedDialog.show();
    }

    @Override
    public void initViewObservable() {

    }

    private void dismissDialog() {
        if (!isFinishing()) {
            if (permissionDeniedDialog != null) {
                permissionDeniedDialog.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected boolean isExit() {
        return false;
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);
    }
}
