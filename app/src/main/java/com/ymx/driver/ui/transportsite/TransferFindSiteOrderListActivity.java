package com.ymx.driver.ui.transportsite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.ymx.driver.R;
import com.ymx.driver.databinding.ActivityTransferFindSideOrderListBinding;
import com.ymx.driver.entity.app.TransferStationGrabOrder;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.transportsite.TransferFindSiteOrderListViewModel;

// 接送站接单
public class TransferFindSiteOrderListActivity extends BaseTransPortSideActivity<ActivityTransferFindSideOrderListBinding, TransferFindSiteOrderListViewModel> {

    public static final String SITE_ID = "side_id";
    private int siteId;

    Runnable grabRunable = new Runnable() {
        @Override
        public void run() {
            viewModel.itemList.clear();
            viewModel.pagerIndex.set(1);
            viewModel.findTransferFindSiteOrderList(10, viewModel.pagerIndex.get(), siteId, viewModel.selectTypeNum.get());
        }
    };

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(activity, TransferFindSiteOrderListActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_transfer_find_side_order_list;
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
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.itemList.clear();
                viewModel.pagerIndex.set(1);
                viewModel.findTransferFindSiteOrderList(10, viewModel.pagerIndex.get(), siteId, viewModel.selectTypeNum.get());
            }
        });
        binding.refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                viewModel.pagerIndex.set(viewModel.pagerIndex.get() + 1);
                viewModel.findTransferFindSiteOrderList(10, viewModel.pagerIndex.get(), siteId, viewModel.selectTypeNum.get());

            }
        });

    }

    @Override
    public void initData() {
        Intent intent = getIntent();


        if (intent.hasExtra(SITE_ID)) {
            siteId = intent.getIntExtra(SITE_ID, 0);

        }

    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish();
            }
        });
        viewModel.uc.ucRefreshOrder.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.refreshLayout.finishRefresh();
            }
        });


        viewModel.ucStationTrip.ucSecectType.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                selectTripOrderPop(binding.selectType, viewModel.selectTypeNum.get());
            }
        });
        viewModel.uc.ucSelectType.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer) {
                    case 0:
                        viewModel.selectTypeHiht.set("全部");
                        break;
                    case 1:
                        viewModel.selectTypeHiht.set("接站");
                        break;
                    case 2:
                        viewModel.selectTypeHiht.set("送站");
                        break;
                }
                viewModel.selectTypeNum.set(integer);
                viewModel.pagerIndex.set(1);
                viewModel.itemList.clear();
                viewModel.findTransferFindSiteOrderList(10, viewModel.pagerIndex.get(), siteId, viewModel.selectTypeNum.get());
            }
        });
        // 抢单结果跳转逻辑
        viewModel.ucStationTrip.ucGrapOrder.observe(this, new Observer<TransferStationGrabOrder>() {
            @Override
            public void onChanged(TransferStationGrabOrder transferStationGrabOrder) {
                switch (transferStationGrabOrder.getIsChooseTrip()) {
                    case 0:
                        if (!TextUtils.isEmpty(transferStationGrabOrder.getOrderNodeNo())) {
                            Intent intent = new Intent();
                            intent.putExtra(TransferStationTripOrderDetailsActivity.ORDER_NO, transferStationGrabOrder.getOrderNodeNo());
                            TransferStationTripOrderDetailsActivity.start(activity, intent);
                            doSthIsExit();
                        }

                        break;
                    case 1:
                        // 跳转行程界面

                        Intent goTo = new Intent();
                        goTo.putExtra(TransferStationTripOrderListActivity.ORDER_NO, transferStationGrabOrder.getOrderNo());
                        TransferStationTripOrderListActivity.start(activity, goTo);
//                        doSthIsExit();

                        break;
                }
            }
        });

        viewModel.ucStationTrip.onGrabFails.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                UIUtils.postDelayTask(grabRunable, 2000);
            }
        });
        viewModel.ucStationTrip.onUcTripList.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                TransferStationTripOrderListActivity.start(activity);
                doSthIsExit();
            }
        });

        viewModel.   ucStationTrip .  onGrabOrder.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String orderNo) {
              viewModel. ransferStationGrabOrder(orderNo);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.pagerIndex.set(1);
        viewModel.itemList.clear();
        viewModel.findTransferFindSiteOrderList(10, viewModel.pagerIndex.get(), siteId, viewModel.selectTypeNum.get());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIUtils.removeTask(grabRunable);
    }
}
