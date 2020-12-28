package com.ymx.driver.ui.longrange.driving;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;


import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityLongRangDrivingFinishDetailsBinding;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.ui.login.LoginHelper;

import com.ymx.driver.ui.mine.Frament.PayDialogFragment;
import com.ymx.driver.ui.mine.activity.WebviewActivity;
import com.ymx.driver.viewmodel.longrangdriving.LongRangDrivingFinishViewModel;

public class LongRangDrivingFinishDetails extends BaseActivity<ActivityLongRangDrivingFinishDetailsBinding, LongRangDrivingFinishViewModel> {
    public static final String ORDERI_ID = "orderNo";
    private String orderNo;

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, LongRangDrivingFinishDetails.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_long_rang_driving_finish_details;
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
        Intent intent = getIntent();
        if (intent.hasExtra(ORDERI_ID)) {
            orderNo = intent.getStringExtra(ORDERI_ID);
            viewModel.orderNo.set(orderNo);
            viewModel.rangeDrivingOrderFinishDetails(orderNo);


        }

    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.ucCallPhone.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                requestPermission(PermissionConfig.PC_CALL_PHONE, false, Manifest.permission.CALL_PHONE);
            }
        });

        viewModel.uc.ucPayDialog.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String orderNo) {

                Bundle bundle = new Bundle();
                bundle.putString(RangeDrivingPayMoneyQrcodeFrament.ORDER_ID, orderNo);
                RangeDrivingPayMoneyQrcodeFrament.newInstance(bundle).show(getSupportFragmentManager(),
                        RangeDrivingPayMoneyQrcodeFrament.class.getSimpleName());


            }
        });


        viewModel.uc.ucQrCodeDialog.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String orderNo) {

                Bundle bundle = new Bundle();
                bundle.putString(PayDialogFragment.ORDER_NO,
                        orderNo);
                PayDialogFragment.newInstance(bundle).show(getSupportFragmentManager(),
                        PayDialogFragment.class.getSimpleName());

            }
        });

        viewModel.uc.ucBillingRules.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                WebviewActivity.start(activity, new Intent()
                        .putExtra(WebviewActivity.TITLE, "计费规则")
                        .putExtra(WebviewActivity.URL, viewModel.billingRulesUrl.get()));
            }
        });


    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);


    }


    @Override
    protected void permissionGranted(int requestCode) {
        if (requestCode == PermissionConfig.PC_CALL_PHONE) {
            try {
                UserEntity userEntity = LoginHelper.getUserEntity();
                if (TextUtils.isEmpty(userEntity.getPhone())) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data;
                data = Uri.parse("tel:" + userEntity.getServicePhone());
                intent.setData(data);
                startActivity(intent);
            } catch (Exception e) {

            }
        }
    }
}
