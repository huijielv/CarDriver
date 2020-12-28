package com.ymx.driver.ui.chartercar;

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
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityCharterOrderDayFinishBinding;
import com.ymx.driver.map.LocationManager;

import com.ymx.driver.util.NavUtil;
import com.ymx.driver.util.NavigationMapUtils;
import com.ymx.driver.viewmodel.chartercar.CharterDetailsViewModel;
import com.ymx.driver.viewmodel.mine.NavSettingViewModel;


public class CharterOrderDayFinishActivity extends BaseActivity<ActivityCharterOrderDayFinishBinding, CharterDetailsViewModel> {
    public static final String ORDERI_ID = "orderNo";
    private String orderNo;

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, CharterOrderDayFinishActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_charter_order_day_finish;
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
            viewModel.orderId.set(orderNo);
            viewModel.charteredOrderDetails(orderNo);
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

        viewModel.uc.ucCall.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                requestPermission(PermissionConfig.PC_CALL_PHONE, false, Manifest.permission.CALL_PHONE);
            }
        });

        viewModel.uc.ucNavigate.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (viewModel.lat != null && viewModel.lat.get() != null && viewModel.lng != null && viewModel.lng.get() != null) {
                    navigation(viewModel.lat.get(), viewModel.lng.get(), "");
                } else {
                    navigation(0.0, 0.0, "");
                }

            }
        });

    }

    public void navigation(double lat, double log, String addressName) {
        if (YmxCache.getNavStatus() == 0) {
            if (YmxCache.getNavMode() == NavSettingViewModel.NAV_MODE_GAODE) {
                NavigationMapUtils.goGaodeMap(getApplicationContext(), lat, log, addressName);
            } else if (YmxCache.getNavMode() == NavSettingViewModel.NAV_MODE_BAIDU) {
                NavUtil.openBaiDuNavi(getApplicationContext(), "", LocationManager.getInstance(getApplicationContext()).getLatitude(), LocationManager.getInstance(getApplicationContext()).getLongitude(), addressName, lat, log);
            } else if (YmxCache.getNavMode() == NavSettingViewModel.NAV_MODE_TENCENT) {
                NavUtil.openTencentMap(getApplicationContext(), "", LocationManager.getInstance(getApplicationContext()).getLatitude(), LocationManager.getInstance(getApplicationContext()).getLongitude(), addressName, lat, log);
            }
        } else {
            NavUtil.neizhiGaodeNavi(getApplicationContext(), LocationManager.getInstance(getApplicationContext()).getLatitude(), LocationManager.getInstance(getApplicationContext()).getLongitude(), lat, log, addressName);
        }
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
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
