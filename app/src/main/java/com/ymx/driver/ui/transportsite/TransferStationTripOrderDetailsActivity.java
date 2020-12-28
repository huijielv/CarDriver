package com.ymx.driver.ui.transportsite;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityTransferStationTripOrderDetailsBinding;
import com.ymx.driver.entity.TransferCancalOrderInfo;
import com.ymx.driver.entity.app.LongDrivingPaySuccessEntigy;
import com.ymx.driver.entity.app.PassengerItemInfo;
import com.ymx.driver.entity.app.TransferCancalOrderEntity;
import com.ymx.driver.entity.app.TransferOrderCancalEntity;
import com.ymx.driver.entity.app.TransferOrderUpdateTime;
import com.ymx.driver.entity.app.TransferStationRecoverLiteItemEntity;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.ui.chartercar.frament.ChatterDrivingTimePickDialogFragment;
import com.ymx.driver.ui.longrange.driving.RangeDrivingPayMoneyQrcodeFrament;
import com.ymx.driver.ui.mine.Frament.PayDialogFragment;
import com.ymx.driver.util.DateUtils;
import com.ymx.driver.util.NavUtil;
import com.ymx.driver.util.NavigationMapUtils;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.SwipeButton;
import com.ymx.driver.viewmodel.mine.NavSettingViewModel;
import com.ymx.driver.viewmodel.transportsite.TransferStationTripOrderDetailsListItem;
import com.ymx.driver.viewmodel.transportsite.TransferStationTripOrderDetailsViewModel;

import org.greenrobot.eventbus.EventBus;

public class TransferStationTripOrderDetailsActivity extends BaseActivity<ActivityTransferStationTripOrderDetailsBinding, TransferStationTripOrderDetailsViewModel> {

    public static final String ORDER_NO = "ORDER_NO";
    private DefaultStyleDialog driverCancalDialog;
    private DefaultStyleDialog cancalDialog;
    private DefaultStyleDialog updatePpassengerTypeDialog;
    private DefaultStyleDialog updateTransferStationDialog;
    private RangeDrivingPayMoneyQrcodeFrament qrcodeFrament;

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(activity, TransferStationTripOrderDetailsActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_transfer_station_trip_order_details;
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

                if (viewModel.driverState != null && viewModel.driverState.get() != null) {
                    if (viewModel.driverState.get() == 1) {
                        viewModel.orderStartLocation(viewModel.orderNo.get(), 1, 1);
                    } else if (viewModel.driverState.get() == 2) {

                        if (viewModel.orderType != null & viewModel.orderType.get() != null && viewModel.orderType.get() == 8) {
                            viewModel.orderStartLocation(viewModel.orderNo.get(), 3, 1);
                        } else if (viewModel.orderType != null & viewModel.orderType.get() != null && viewModel.orderType.get() == 7) {
                            if (updateAllPessenerDestination()) {
                                return;
                            }
                            if (updateAllPessenerReadyCar()) {
                                return;
                            }
                            viewModel.orderStartLocation(viewModel.orderNo.get(), 3, 1);
                        }


                    } else if (viewModel.driverState.get() == 4) {
                        if (viewModel.orderType != null & viewModel.orderType.get() != null && viewModel.orderType.get() == 8) {
                            if (updateAllPessenerReadyTheDestination()) {
                                return;
                            }
                            viewModel.orderStartLocation(viewModel.orderNo.get(), 4, 1);
                        } else if (viewModel.orderType != null & viewModel.orderType.get() != null && viewModel.orderType.get() == 7) {
                            viewModel.orderStartLocation(viewModel.orderNo.get(), 4, 1);
                        }


                    } else if (viewModel.driverState.get() == 7) {
                        doSthIsExit();
                    }
                }


            }
        });
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                viewModel.tripOrderRecoverDetails(viewModel.orderNo.get());
            }
        });


    }

    @Override
    public void initData() {
        if (getIntent().hasExtra(ORDER_NO)) {
            viewModel.orderNo.set(getIntent().getStringExtra(ORDER_NO));
            viewModel.tripOrderRecoverDetails(viewModel.orderNo.get());
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

        viewModel.uc.ucRefreshOrderDetails.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (!TextUtils.isEmpty(viewModel.orderNo.get())) {
                    viewModel.tripOrderRecoverDetails(viewModel.orderNo.get());
                }

            }
        });


        viewModel.uc.ucOrderDetailsSuccess.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                binding.btnSubmit.setVisibility(View.VISIBLE);
                if (viewModel.buttonText != null && viewModel.buttonText.get() != null && !TextUtils.isEmpty(viewModel.buttonText.get())) {
                    binding.btnSubmit.setText(viewModel.buttonText.get());
                }

                if (viewModel.driverState.get() == 7) {
                    TransportSiteUpdateCarStateActivity.start(activity);
                    doSthIsExit();
                }

            }
        });

        viewModel.uc.ucPassengerPhone.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                requestPermission(PermissionConfig.PC_CALL_PHONE, false, Manifest.permission.CALL_PHONE);
            }
        });
        viewModel.uc.ucTransferCancalOrderSuccess.observe(this, new Observer<TransferCancalOrderEntity>() {
            @Override
            public void onChanged(TransferCancalOrderEntity transferCancalOrderEntity) {
                if (transferCancalOrderEntity.getDriverState() == 2) {
                    viewModel.tripOrderRecoverDetails(transferCancalOrderEntity.getOrderNo());
                } else if (transferCancalOrderEntity.getDriverState() == 8) {
                    doSthIsExit();
                    TransferStationTripOrderListActivity.start(activity);
                }
            }
        });


        viewModel.uc.ucTransferCancalOrder.observe(this, new Observer<TransferCancalOrderInfo>() {
            @Override
            public void onChanged(TransferCancalOrderInfo transferCancalOrderInfo) {
                driverCancalDialog = new DefaultStyleDialog(activity)
                        .setBody("是否确定取消" + transferCancalOrderInfo.getPhoneInfo() + "乘客的订单?")
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
                                viewModel.transferCancalOrder(transferCancalOrderInfo.getOrderNo());
                            }
                        });
                driverCancalDialog.show();
            }
        });

        viewModel.uc.ucShowCancalOrderDialog.observe(this, new Observer<TransferOrderCancalEntity>() {
            @Override
            public void onChanged(TransferOrderCancalEntity transferOrderCancalEntity) {
                cancalDialog = new DefaultStyleDialog(activity)
                        .setBody(transferOrderCancalEntity.getTips())
                        .setPositiveText("确定")
                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                            @Override
                            public void negative(Dialog dialog) {
                                dialog.dismiss();

                            }

                            @Override
                            public void positive(Dialog dialog) {
                                dialog.dismiss();
                                if (transferOrderCancalEntity.getDriverState() == 8) {
                                    doSthIsExit();
                                } else if (transferOrderCancalEntity.getDriverState() == 2) {
                                    viewModel.tripOrderRecoverDetails(viewModel.orderNo.get());
                                }

                            }
                        });
                cancalDialog.show();


            }
        });
        viewModel.uc.ucRefreshOrder.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.refreshLayout.finishRefresh();
            }
        });
        viewModel.uc.ucGrabOrder.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (viewModel.sideId != null && viewModel.sideId.get() != null) {
                    Intent intent = new Intent();
                    intent.putExtra(TransferFindSiteOrderListActivity.SITE_ID, viewModel.sideId.get());
                    TransferFindSiteOrderListActivity.start(activity, intent);
                    doSthIsExit();
                }

            }
        });
        viewModel.uc.ucShowSelectTime.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (!isFinishing()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ChatterDrivingTimePickDialogFragment.TIME, DateUtils.timeStamp2Date(String.valueOf(System.currentTimeMillis() / 1000),
                            "yyyy-MM-dd HH:mm:ss"));
                    bundle.putInt(ChatterDrivingTimePickDialogFragment.NEXT_DAY_TYPE, 2);
                    ChatterDrivingTimePickDialogFragment.newInstance(bundle).show(getSupportFragmentManager(),
                            ChatterDrivingTimePickDialogFragment.class.getSimpleName());
                }
            }
        });

        viewModel.uc.ucShowUpdateTime.observe(this, new Observer<TransferOrderUpdateTime>() {
            @Override
            public void onChanged(TransferOrderUpdateTime transferOrderUpdateTime) {
                DefaultStyleDialog transferOrderUpdateTimeDialog = new DefaultStyleDialog(activity)
                        .setBody(transferOrderUpdateTime.getTips())
                        .setPositiveText("确定")
                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                            @Override
                            public void negative(Dialog dialog) {
                                dialog.dismiss();

                            }

                            @Override
                            public void positive(Dialog dialog) {
                                dialog.dismiss();

                                if (transferOrderUpdateTime.getState() == 1) {
                                    viewModel.tripOrderRecoverDetails(viewModel.orderNo.get());
                                }

                            }
                        });
                transferOrderUpdateTimeDialog.show();
            }
        });
        viewModel.uc.ucNavi.observe(this, new Observer<TransferStationRecoverLiteItemEntity>() {
            @Override
            public void onChanged(TransferStationRecoverLiteItemEntity entity) {
                if (viewModel.driverState != null && viewModel.driverState.get() != null && (viewModel.driverState.get() == 2 && (entity.getPassengerState() == 3 || entity.getPassengerState() == 4)) || (viewModel.driverState.get() == 4)) {
                    navigation(entity.getLat(), entity.getLng(), entity.getDesName());
                } else if (viewModel.driverState != null && viewModel.driverState.get() != null && viewModel.driverState.get() == 2 && entity.getPassengerState() == 2) {
                    navigation(entity.getLat(), entity.getLng(), entity.getSrcName());
                } else {
                    navigation(entity.getLat(), entity.getLng(), "");
                }
            }
        });

        viewModel.uc.ucUpdatePassengerType.observe(this, new Observer<TransferStationRecoverLiteItemEntity>() {
            @Override
            public void onChanged(TransferStationRecoverLiteItemEntity type) {

                StringBuffer sb = new StringBuffer();
                switch (type.getPassengerState()) {
                    case 2:
                        sb.append("是否确定已到达尾号").append(type.getPhone().substring(7, 11)).append("乘客上车点？");
                        break;
                    case 3:
                        sb.append("是否确定尾号").append(type.getPhone().substring(7, 11)).append("乘客已上车？");
                        break;
                    case 4:
                        sb.append("是否确定尾号").append(type.getPhone().substring(7, 11)).append("乘客\n" +
                                "已到达目的地并发起付款？");
                        break;
                }
                updatePpassengerTypeDialog = new DefaultStyleDialog(activity)
                        .setBody(sb.toString())
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
                                if (viewModel.driverState != null && viewModel.driverState.get() == 2) {
                                    switch (type.getPassengerState()) {
                                        case 2:
                                            viewModel.orderStartLocation(type.getOrderNo(), 1, 2);
                                            break;
                                        case 3:
                                            viewModel.orderStartLocation(type.getOrderNo(), 2, 2);
                                            break;
                                    }

                                } else if (viewModel.driverState != null && viewModel.driverState.get() == 4) {
                                    viewModel.orderStartLocation(type.getOrderNo(), 3, 2);
                                }


                            }
                        });
                updatePpassengerTypeDialog.show();
            }
        });

        viewModel.uc.ucErrorMsg.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                DefaultStyleDialog defaultStyleDialog = new DefaultStyleDialog(activity)
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


                            }
                        });
                defaultStyleDialog.show();

            }
        });

        viewModel.uc.ucShowQrCode.observe(this, new Observer<TransferStationRecoverLiteItemEntity>() {
            @Override
            public void onChanged(TransferStationRecoverLiteItemEntity transferStationRecoverLiteItemEntity) {
                Bundle bundle = new Bundle();
                bundle.putString(RangeDrivingPayMoneyQrcodeFrament.ORDER_ID,
                        transferStationRecoverLiteItemEntity.getOrderNo());
                qrcodeFrament = RangeDrivingPayMoneyQrcodeFrament.newInstance(bundle);

                qrcodeFrament.show(getSupportFragmentManager(),
                        RangeDrivingPayMoneyQrcodeFrament.class.getSimpleName());
            }
        });


        viewModel.uc.ucPaySuccess.observe(this, new Observer<LongDrivingPaySuccessEntigy>() {
            @Override
            public void onChanged(LongDrivingPaySuccessEntigy longDrivingPaySuccessEntigy) {

                if (longDrivingPaySuccessEntigy.getDriverOrderNo().equals(viewModel.orderNo.get())) {

                    viewModel.itemList.clear();
                    viewModel.tripOrderRecoverDetails(viewModel.orderNo.get());
                }

            }
        });

        viewModel.uc.ucPayDialog.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String orderId) {
                Bundle bundle = new Bundle();
                bundle.putString(PayDialogFragment.ORDER_NO,
                        orderId);
                PayDialogFragment.newInstance(bundle).show(getSupportFragmentManager(),
                        PayDialogFragment.class.getSimpleName());
            }
        });

        viewModel.uc.ucDriverCancanOrder.observe(this, new Observer<TransferCancalOrderEntity>() {
            @Override
            public void onChanged(TransferCancalOrderEntity transferCancalOrderEntity) {
                if (transferCancalOrderEntity.getDriverState() == 2 || transferCancalOrderEntity.getDriverState() == 1) {
                    viewModel.tripOrderRecoverDetails(transferCancalOrderEntity.getOrderNo());
                } else if (transferCancalOrderEntity.getDriverState() == 8) {
                    TransportSiteUpdateCarStateActivity.start(activity);
                    doSthIsExit();
                }
            }
        });
        viewModel.uc.ucCancanOrderFails.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (viewModel.itemList.size() == 1) {
                    doSthIsExit();
                } else if (viewModel.itemList.size() > 1) {
                    if (!TextUtils.isEmpty(viewModel.orderNo.get())) {
                        viewModel.tripOrderRecoverDetails(viewModel.orderNo.get());
                    }
                }
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
                if (TextUtils.isEmpty(viewModel.passengerPhone.get())) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + viewModel.passengerPhone.get());
                intent.setData(data);
                startActivity(intent);
            } catch (Exception e) {

            }
        }
    }

    public boolean updateAllPessenerDestination() {
        boolean allPessenerDestination = false;
        for (TransferStationTripOrderDetailsListItem transferStationRecoverLiteItemEntity : viewModel.itemList) {
            TransferStationRecoverLiteItemEntity transferEntity = transferStationRecoverLiteItemEntity.entity.get();
            if (transferEntity != null) {
                if (transferEntity.getPassengerState() == 2) {
                    allPessenerDestination = true;
                    break;
                }
            }
        }
        if (allPessenerDestination) {
            showTransferStationDialog(UIUtils.getString(R.string.transfer_pessener_destination));
            updateTransferStationDialog.show();
        }


        return allPessenerDestination;

    }

    public boolean updateAllPessenerReadyCar() {
        boolean allPessenerReadyCar = false;
        for (TransferStationTripOrderDetailsListItem transferStationRecoverLiteItemEntity : viewModel.itemList) {
            TransferStationRecoverLiteItemEntity transferEntity = transferStationRecoverLiteItemEntity.entity.get();
            if (transferEntity != null) {
                if (transferEntity.getPassengerState() == 3) {
                    allPessenerReadyCar = true;
                    break;
                }
            }
        }
        if (allPessenerReadyCar) {
            showTransferStationDialog(UIUtils.getString(R.string.driver_transfer_pessener_ready_car));
        }


        return allPessenerReadyCar;
    }

    public boolean updateAllPessenerReadyTheDestination() {
        boolean allPessenerReadTheDestination = false;
        for (TransferStationTripOrderDetailsListItem transferStationRecoverLiteItemEntity : viewModel.itemList) {
            TransferStationRecoverLiteItemEntity transferEntity = transferStationRecoverLiteItemEntity.entity.get();
            if (transferEntity != null) {
                if (transferEntity.getPassengerState() == 7) {
                    allPessenerReadTheDestination = true;
                    break;
                }
            }
        }
        if (allPessenerReadTheDestination) {
            updateTransferStationDialog = new DefaultStyleDialog(activity)
                    .setBody("是否确定所有乘客\n" +
                            "已到达目的地并发起付款？")
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
                            viewModel.orderStartLocation(viewModel.orderNo.get(), 4, 1);
                        }
                    });
            updateTransferStationDialog.show();
        }


        return allPessenerReadTheDestination;

    }


    public void showTransferStationDialog(String title) {
        updateTransferStationDialog = new DefaultStyleDialog(activity)
                .setBody(title)
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
                        viewModel.orderStartLocation(viewModel.orderNo.get(), 3, 1);
                    }
                });
        updateTransferStationDialog.show();
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
    protected void onDestroy() {
        super.onDestroy();
        if (driverCancalDialog != null && driverCancalDialog.isShowing()) {
            driverCancalDialog.dismiss();
        }
        if (cancalDialog != null && cancalDialog.isShowing()) {
            cancalDialog.dismiss();
        }
        if (updatePpassengerTypeDialog != null && updatePpassengerTypeDialog.isShowing()) {
            updatePpassengerTypeDialog.dismiss();
        }
        if (updateTransferStationDialog != null && updateTransferStationDialog.isShowing()) {
            updateTransferStationDialog.dismiss();
        }
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_TRIP_REFRESH_DATA_CODE));
    }


}
