package com.ymx.driver.ui.transportsite;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.databinding.ActivityTransferStationTripOrderListBinding;
import com.ymx.driver.entity.app.TransferOrderCancalEntity;
import com.ymx.driver.viewmodel.transportsite.TransferStationTripOrderListViewModel;

//加入行程订单界面
public class TransferStationTripOrderListActivity extends BaseTransPortSideActivity<ActivityTransferStationTripOrderListBinding, TransferStationTripOrderListViewModel> {

    public static final String ORDER_NO = "ORDER_NO";

    private DefaultStyleDialog timeDialog;
    private DefaultStyleDialog cancalDialog;
    private DefaultStyleDialog backDialog;

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(activity, TransferStationTripOrderListActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_transfer_station_trip_order_list;
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

        if (getIntent().hasExtra(ORDER_NO)) {
            viewModel.orderNo.set(getIntent().getStringExtra(ORDER_NO));
            viewModel.tripOrderListTitle.set("选择加入行程");
            viewModel.isShow.set(false);
//            viewModel.findTripOrderList(viewModel.orderNo.get());
        } else {
            viewModel.tripOrderListTitle.set("行程列表");
            viewModel.isShow.set(true);
//            viewModel.findTripOrderList(0);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.itemList.clear();
        if (viewModel.isShow.get()) {
            viewModel.findTripOrderList(viewModel.selectTypeNum.get());
        } else {
            viewModel.findTripOrderList(viewModel.orderNo.get());
        }

    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (!viewModel.isShow.get()) {
                    if (viewModel.giveUpOrderResult.get() == 0) {
                        backDialog = new DefaultStyleDialog(activity)
                                .setBody("返回后取消接单，确定返回吗？")
                                .setNegativeText("取消")
                                .setPositiveText("确定")
                                .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                                    @Override
                                    public void negative(Dialog dialog) {
                                        dialog.dismiss();

                                    }

                                    @Override
                                    public void positive(Dialog dialog) {
                                        dialog.dismiss();
                                        viewModel.giveUpOrder(viewModel.orderNo.get());
                                    }
                                });
                        backDialog.show();
                    } else {
                        doSthIsExit();
                    }

                } else {
                    doSthIsExit();
                }


            }
        });
        viewModel.ucStationTrip.ucGiveUpOrder.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
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
                viewModel.findTripOrderList(integer);
            }
        });
        viewModel.ucStationTrip.ucShowTimeNavalidDialog.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                timeDialog = new DefaultStyleDialog(activity)
                        .setBody(s)
                        .setPositiveText("确定")
                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                            @Override
                            public void negative(Dialog dialog) {
                                dialog.dismiss();

                            }

                            @Override
                            public void positive(Dialog dialog) {
                                dialog.dismiss();
                                doSthIsExit();
                            }
                        });
                timeDialog.show();
                viewModel.giveUpOrderResult.set(1);

            }
        });

        viewModel.ucStationTrip.ucShowCancalOrderDialog.observe(this, new Observer<TransferOrderCancalEntity>() {
            @Override
            public void onChanged(TransferOrderCancalEntity transferOrderCancalEntity) {
                cancalDialog = new DefaultStyleDialog(activity)
                        .setBody(transferOrderCancalEntity.getTips())
                        .setNegativeText("取消")
                        .setPositiveText("确定")
                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                            @Override
                            public void negative(Dialog dialog) {
                                dialog.dismiss();

                            }

                            @Override
                            public void positive(Dialog dialog) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                cancalDialog.show();
            }
        });

        viewModel.ucStationTrip.ucGoToDetails.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String orderNo) {
                if (!TextUtils.isEmpty(orderNo)) {
                    Intent intent = new Intent();
                    intent.putExtra(TransferStationTripOrderDetailsActivity.ORDER_NO, orderNo);
                    TransferStationTripOrderDetailsActivity.start(activity, intent);

                }

            }
        });

        viewModel.ucStationTrip.ucJoin.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String orderNo) {
                Intent intent = new Intent();
                intent.putExtra(TransferStationTripOrderDetailsActivity.ORDER_NO, orderNo);
                TransferStationTripOrderDetailsActivity.start(activity, intent);

                doSthIsExit();
            }
        });

        viewModel.ucStationTrip.ucLoadDataSuccess.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                if (viewModel.more.get()) {
                    binding.emptyViewLl.setVisibility(View.VISIBLE);
                    binding.recyc.setVisibility(View.GONE);
                } else {
                    binding.emptyViewLl.setVisibility(View.GONE);
                    binding.recyc.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeDialog != null && timeDialog.isShowing()) {
            timeDialog.dismiss();
        }
        if (cancalDialog != null && cancalDialog.isShowing()) {
            cancalDialog.dismiss();
        }

        if (backDialog != null && backDialog.isShowing()) {
            backDialog.dismiss();
        }
    }
}
