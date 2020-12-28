package com.ymx.driver.ui.mine.head;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityTodayOrderBinding;
import com.ymx.driver.entity.TodayOrderEntity;
import com.ymx.driver.ui.chartercar.CharterOrderFinishOrderDetailsActivity;
import com.ymx.driver.ui.longrange.driving.LongRangDrivingFinishDetails;
import com.ymx.driver.ui.longrange.driving.LongRangeFerryFinishDetails;
import com.ymx.driver.ui.transportsite.TransferStationTripOrderDetailsActivity;
import com.ymx.driver.ui.transportsite.TransferStationTripOrderFinishDetailsActivity;
import com.ymx.driver.ui.travel.activity.TravelActivity;
import com.ymx.driver.ui.travel.activity.TravelOrderDetailsActivity;
import com.ymx.driver.ui.travel.activity.TravelOrderDetailsFinshActivity;
import com.ymx.driver.viewmodel.mine.TodayOrderViewModel;
import com.ymx.driver.viewmodel.triporderlist.TripOrderListViewModel;

public class TodayOrderActivity extends BaseActivity<ActivityTodayOrderBinding, TodayOrderViewModel> {
    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, TodayOrderActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_today_order;
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
        binding.refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                viewModel.pagerIndex.set(viewModel.pagerIndex.get() + 1);
                viewModel.getOrderTodayList();
            }
        });

        viewModel.uc.ucCanLoadmore.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.refreshLayout.setEnableLoadMore(aBoolean);
            }
        });

        viewModel.uc.ucToTodayOrder.observe(this, new Observer<TodayOrderEntity>() {
            @Override
            public void onChanged(TodayOrderEntity todayOrderEntity) {

                int driverState = todayOrderEntity.getDriverState();
                String orderId = todayOrderEntity.getOrderNo();

                int businessType = todayOrderEntity.getBusinessType();
                if (businessType == 6 || businessType == 7) {
                    Intent intent = new Intent();
                    intent.putExtra(TravelActivity.ORDERI_ID, orderId);
                    LongRangDrivingFinishDetails.start(activity, intent);
                } else if (businessType == 8) {
                    Intent intent = new Intent();
                    intent.putExtra(LongRangeFerryFinishDetails.ORDERI_ID, orderId);
                    LongRangeFerryFinishDetails.start(activity, intent);
                } else if (businessType == 9) {
                    Intent intent = new Intent();
                    intent.putExtra(LongRangeFerryFinishDetails.ORDERI_ID, orderId);
                    CharterOrderFinishOrderDetailsActivity.start(activity, intent);
                } else  if ( businessType==11){
                    if (driverState==7||driverState==8){
                        Intent intent = new Intent();
                        intent.putExtra(TransferStationTripOrderFinishDetailsActivity.ORDERI_ID, orderId);
                        TransferStationTripOrderFinishDetailsActivity.start(activity, intent);
                    }else {
                        Intent intent = new Intent();
                        intent.putExtra(TransferStationTripOrderDetailsActivity.ORDER_NO, orderId);
                        TransferStationTripOrderDetailsActivity.start(activity, intent);
                    }
                }

                else {
                    if (driverState == TripOrderListViewModel.DRIVER_STATE_TO_START || driverState == TripOrderListViewModel.DRIVER_STATE_READY_TO_GO || driverState == TripOrderListViewModel.DRIVER_STATE_ROADING || driverState == TripOrderListViewModel.DRIVER_STATE_TO_PASSENGERS) {
                        Intent intent = new Intent();
                        intent.putExtra(TravelActivity.ORDERI_ID, orderId);
                        TravelActivity.start(activity, intent);
                    } else if (driverState == TripOrderListViewModel.DRIVER_STATE_CONFIRM_COST) {
                        Intent intent = new Intent();
                        intent.putExtra(TravelOrderDetailsActivity.ORDERI_ID, orderId);
                        intent.putExtra(TravelOrderDetailsActivity.ACTION_TYPE, driverState);
                        TravelOrderDetailsActivity.start(activity, intent);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(TravelOrderDetailsFinshActivity.ORDERI_ID, orderId);
                        intent.putExtra(TravelOrderDetailsFinshActivity.ACTION_TYPE, driverState);
                        TravelOrderDetailsFinshActivity.start(activity, intent);
                    }
                }


            }

//            }
        });

    }

    @Override
    public void initData() {
        viewModel.getOrderTodayList();

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
