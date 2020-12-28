package com.ymx.driver.ui.longrange.driving;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.WindowManager;


import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.amap.api.location.AMapLocation;
import com.ymx.driver.R;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.databinding.ActivityOrderCirulationBinding;
import com.ymx.driver.entity.app.AcceptOrderCirculationBodyEntity;
import com.ymx.driver.entity.app.OrderCirculationEntity;
import com.ymx.driver.entity.app.OrderCirculationNoticeEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.ui.travel.activity.TravelActivity;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.LoadingDialog;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class OrderCirculationDialogActivity extends Activity {
    public static final String NOTICE = "notice";
    private ActivityOrderCirulationBinding binding;
    private LoadingDialog loadingDialog;
    private String orderNo;
    private OrderCirculationNoticeEntity orderCirculationNoticeEntity;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, OrderCirculationDialogActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);

    }

    public BindingCommand cancal = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            refuseOrderCirculation(orderNo);


        }
    });
    public BindingCommand positive = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            AMapLocation aMapLocation = LocationManager.getInstance(getApplicationContext()).getAMapLocation();

            refuseOrderCirculation(orderNo, String.valueOf(aMapLocation.getLatitude()), String.valueOf(aMapLocation.getLongitude()));

        }
    });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.activity_order_cirulation, null, false);
        setContentView(binding.getRoot());
        orderCirculationNoticeEntity = (OrderCirculationNoticeEntity) getIntent().getSerializableExtra(NOTICE);
        orderNo = orderCirculationNoticeEntity.getOrderNo();
        binding.setDialog(this);
        binding.line.setText(orderCirculationNoticeEntity.getLineName());
        binding.dintance.setText(orderCirculationNoticeEntity.getTravelMileage());
        binding.number.setText(orderCirculationNoticeEntity.getRideNumber());
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        WindowManager windowManager = (WindowManager)
                getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay();
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (d.getWidth() * 0.725);
        getWindow().setAttributes(p);
    }


    public void refuseOrderCirculation(String orderNo) {
        RetrofitFactory.sApiService.refuseOrderCirculation(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<OrderCirculationEntity>() {
                    @Override
                    protected void onRequestStart() {
                        loadingDialog.show();
                    }

                    @Override
                    protected void onRequestEnd() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    protected void onSuccees(OrderCirculationEntity orderCirculationEntity) {
                        UIUtils.showToast("取消流转订单");
                        finish();

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public void refuseOrderCirculation(String orderNo, String lat, String lon) {
        RetrofitFactory.sApiService.acceptOrderCirculation(new AcceptOrderCirculationBodyEntity(orderNo,  lon,lat, ""))
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<OrderCirculationEntity>() {
                    @Override
                    protected void onRequestStart() {
                        loadingDialog.show();
                    }

                    @Override
                    protected void onRequestEnd() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    protected void onSuccees(OrderCirculationEntity orderCirculationEntity) {
                        finish();
                        YmxCache.setOrderId(orderCirculationEntity.getOrderNo());
                        Intent intent = new Intent(OrderCirculationDialogActivity.this, LongRangeDrivingDetails.class);
                        intent.putExtra(TravelActivity.ORDERI_ID, orderCirculationEntity.getOrderNo());
                        startActivity(intent);

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

}
