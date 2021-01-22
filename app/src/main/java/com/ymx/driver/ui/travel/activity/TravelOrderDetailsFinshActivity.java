package com.ymx.driver.ui.travel.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityTripOrderDetailsFinishBinding;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.ui.mine.activity.WebviewActivity;

import com.ymx.driver.viewmodel.finishorderdetails.TripOrderDetailsFinshViewModel;

import org.greenrobot.eventbus.EventBus;

public class TravelOrderDetailsFinshActivity extends BaseActivity<ActivityTripOrderDetailsFinishBinding, TripOrderDetailsFinshViewModel> {


    public static final String ORDERI_ID = "orderNo";
    public static final String ACTION_TYPE = "actionType";


    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, TravelOrderDetailsFinshActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_trip_order_details_finish;
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
            String orderNo = intent.getStringExtra(ORDERI_ID);
            int actionType = intent.getIntExtra(ACTION_TYPE, 0);
            viewModel.orderStatus.set(actionType);
            viewModel.getOrderDetails(orderNo);

        }

    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucCallPhone.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                requestPermission(PermissionConfig.PC_CALL_PHONE, false, Manifest.permission.CALL_PHONE);
            }
        });
        viewModel.uc.back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.ucQrCode.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                Intent intent = new Intent();
                intent.putExtra(TravelActivity.ORDERI_ID, viewModel.orderId.get());
                intent.putExtra(PhoneOrderPayActivity.TYPE, 2);
                PhoneOrderPayActivity.start(activity, intent);

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

        viewModel.uc.ucOrderDetails.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {


                if ((viewModel.businessType.get() == 10&&viewModel.categoryType.get()==0) || (viewModel.businessType.get() == 5 && viewModel.driverType.get() == 6)) {
                    binding.taxiLl.setVisibility(View.VISIBLE);
                    binding.recyc.setVisibility(View.GONE);
                }

            }
        });

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

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);
        overridePendingTransition(R.anim.slide_enter_right, R.anim.slide_exit_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_TRIP_REFRESH_DATA_CODE));
    }
}
