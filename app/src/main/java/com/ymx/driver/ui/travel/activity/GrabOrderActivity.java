package com.ymx.driver.ui.travel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.ActivityGrabOrderDetailsBinding;
import com.ymx.driver.viewmodel.orderdetails.OrderDetailsViewModel;

import org.greenrobot.eventbus.EventBus;


public class GrabOrderActivity extends BaseActivity<ActivityGrabOrderDetailsBinding, OrderDetailsViewModel> {
    private String orderNo;

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, GrabOrderActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_grab_order_details;
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
        if (intent.hasExtra(TravelActivity.ORDERI_ID)) {
            orderNo = intent.getStringExtra(TravelActivity.ORDERI_ID);
            viewModel.orderId.set(orderNo);
            viewModel.getOrderDetails(orderNo);
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucGrabOrder.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                viewModel.grabOrder(orderNo);

            }
        });

        viewModel.uc.ucCanOrder.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                viewModel.cancelOrder(orderNo);
            }
        });
        viewModel.uc.ucGrabOrderStatus.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent();
                    intent.putExtra(TravelActivity.ORDERI_ID, orderNo);
                    TravelActivity.start(activity, intent);
                    doSthIsExit();
                } else {
                    doSthIsExit();
                }
            }
        });

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
        overridePendingTransition(R.anim.slide_enter_right, R.anim.slide_exit_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_TRIP_REFRESH_DATA_CODE));
    }
}
