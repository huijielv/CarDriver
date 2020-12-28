package com.ymx.driver.ui.transportsite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ymx.driver.R;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.AppConfig;
import com.ymx.driver.databinding.ActivityTranSportSiteUpdateCarStateBinding;
import com.ymx.driver.entity.app.FindCarpoolingSiteResultEntity;
import com.ymx.driver.mqtt.MQTTService;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.SPUtils;
import com.ymx.driver.view.SwipeButton;
import com.ymx.driver.viewmodel.transportsite.TranSportSiteViewModelUpdateStateViewModel;

// 接送站出车界面
public class TransportSiteUpdateCarStateActivity extends BaseTransPortSideActivity<ActivityTranSportSiteUpdateCarStateBinding, TranSportSiteViewModelUpdateStateViewModel> {


    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(activity, TransportSiteUpdateCarStateActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_tran_sport_site_update_car_state;
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
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.btnSubmit.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {

            }

            @Override
            public void onSwipeCancel() {

            }

            @Override
            public void onSwipeConfirm() {
                if (viewModel.carState != null && viewModel.carState.get() != null && viewModel.carState.get() == 3) {
                    viewModel.updateCarState(1);
                }

            }
        });

        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.itemList.clear();
                viewModel.siteOrderList.clear();
                viewModel.findCarpoolingSite();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.itemList.clear();
        viewModel.siteOrderList.clear();
        viewModel.findCarpoolingSite();
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });
        viewModel.transport.ucRefreshOrder.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.refreshLayout.finishRefresh();
            }
        });

        viewModel.transport.ucViewShow.observe(this, new Observer<FindCarpoolingSiteResultEntity>() {
            @Override
            public void onChanged(FindCarpoolingSiteResultEntity findCarpoolingSiteResultEntity) {
                updateViewShow(findCarpoolingSiteResultEntity);
            }
        });

        viewModel.transport.ucGoToTripList.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                TransferStationTripOrderListActivity.start(activity);
            }
        });
        viewModel.transport.ucSideId.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Intent intent = new Intent();
                intent.putExtra(TransferFindSiteOrderListActivity.SITE_ID, integer);
                TransferFindSiteOrderListActivity.start(activity, intent);

            }
        });

        viewModel.transport.ucInitSideIdList.observe(this, new Observer<FindCarpoolingSiteResultEntity>() {
            @Override
            public void onChanged(FindCarpoolingSiteResultEntity findCarpoolingSiteResultEntity) {
                YmxCache.setTranferCarState(findCarpoolingSiteResultEntity.getCarState());
                if (findCarpoolingSiteResultEntity.getCarState() != 3) {
                    YmxCache.setDriverSideIdList(findCarpoolingSiteResultEntity.getSiteIdList());
                    MQTTService.stop(YmxApp.getInstance());
                    MQTTService.start(YmxApp.getInstance(), MQTTService.initService);
                }else if (findCarpoolingSiteResultEntity.getCarState() ==3 ){
                    SPUtils.remove(AppConfig.KET_DRIVET_SIDEIDLIST);
                    MQTTService.stop(YmxApp.getInstance());
                    MQTTService.start(YmxApp.getInstance(), MQTTService.initService);
                }

            }
        });


    }


    public void updateViewShow(FindCarpoolingSiteResultEntity findCarpoolingSiteResultEntity) {
        if (findCarpoolingSiteResultEntity.getCarState() == 3) {
            binding.btnSubmit.setVisibility(View.VISIBLE);
            binding.recyc.setVisibility(View.VISIBLE);
            binding.recycOrderLl.setVisibility(View.GONE);
            binding.btnSubmitRl.setVisibility(View.GONE);
        } else {

            binding.btnSubmit.setVisibility(View.GONE);
            binding.recyc.setVisibility(View.GONE);
            binding.recycOrderLl.setVisibility(View.VISIBLE);
            binding.btnSubmitRl.setVisibility(View.VISIBLE);


        }
    }
}
