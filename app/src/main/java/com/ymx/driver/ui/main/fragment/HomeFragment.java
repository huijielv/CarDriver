package com.ymx.driver.ui.main.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.amap.api.maps.TextureMapView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseMapFragment;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.databinding.FragmentHomeBinding;
import com.ymx.driver.dialog.GrapOrderDailog;
import com.ymx.driver.dialog.GrapOrderTipsDialog;
import com.ymx.driver.dialog.SafetyTipsDialog;
import com.ymx.driver.dialog.UpdateCarStateDialog;
import com.ymx.driver.entity.NetChangeEntity;
import com.ymx.driver.entity.app.ConfirmOrderEntity;
import com.ymx.driver.entity.app.DriverLockEntity;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.mqtt.MQTTService;
import com.ymx.driver.ui.chartercar.CharterOrderDetailsActivity;
import com.ymx.driver.ui.longrange.driving.LongRangeDrivingDetails;
import com.ymx.driver.ui.mine.head.TodayIncomeActivity;
import com.ymx.driver.ui.mine.head.TodayOrderActivity;
import com.ymx.driver.ui.my.wallet.MyWalletActivity;
import com.ymx.driver.ui.travel.activity.CarPoolDetailsActivity;
import com.ymx.driver.ui.travel.activity.TravelActivity;
import com.ymx.driver.ui.trip.activity.TripOrderListActivity;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.viewmodel.main.HomeViewModel;


import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;


/**
 * Created by xuweihua
 * 2020/5/4
 */
public class HomeFragment extends BaseMapFragment<FragmentHomeBinding, HomeViewModel> {

    private String TAG = this.getClass().getSimpleName();
    private UpdateCarStateDialog updateCarStateDialog;
    private SafetyTipsDialog safetyTipsDialog;

    public static HomeFragment newInstance() {
        return newInstance(null);
    }

    public static HomeFragment newInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.fragment_home;
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
        super.initView(savedInstanceState);

        xbinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                xviewModel.getHeadDetailInfo();

            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateCarStateDialog != null && updateCarStateDialog.isShowing()) {
            updateCarStateDialog.dismiss();
        }
        if (safetyTipsDialog != null && safetyTipsDialog.isShowing()) {
            safetyTipsDialog.dismiss();
        }

    }

    @Override
    protected TextureMapView getTextureMapView() {
        return null;
    }


    @Override
    protected void initMapLoad() {

    }


    @Override
    public void initData() {

        xviewModel.getHeadDetailInfo();
        xviewModel.driverStatus.set(YmxCache.getCarState());
        xviewModel.lockDrvierState.set(YmxCache.getLockDriverLock());
        xviewModel.drvierType.set(YmxCache.getDriverType());

    }

    @Override
    public void initViewObservable() {

        xviewModel.uc.ucStartWorking.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {


                if (xviewModel.driverStatus.get() != 0) {
                    if (YmxCache.getDriverType() == 6 || YmxCache.getDriverType() == 1) {
                        updateCarStateDialog = new UpdateCarStateDialog(getActivity(), YmxCache.getDriverType());
                        updateCarStateDialog.setSelectPostion(new UpdateCarStateDialog.selectCarStatePostion() {
                            @Override
                            public void getSelectPostion(int position) {
                                updateCarStateDialog.dismiss();
                                if (position == 0) {
                                    xviewModel.startWork(1);
                                } else if (position == 1) {
                                    xviewModel.startWork(2);
                                }


                            }
                        });
                        updateCarStateDialog.show();

                    } else {
                        xviewModel.startWork(0);
                    }


                }


            }
        });


        xviewModel.uc.ucEndWorking.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                xviewModel.stopWork();

            }
        });

        xviewModel.uc.ucShowAutoReceiveDialog.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                showAutoReceiveOrderDialog(HomeViewModel.autoOrderType, "");
            }
        });
        xviewModel.uc.ucGoToTripOrderActicity.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                TripOrderListActivity.start(activity);
                MQTTService.start(YmxApp.getInstance(), MQTTService.Action);
            }
        });

        // 抢订单
        xviewModel.uc.ucGrapPassengerInfo.observe(this, new Observer<PassengerInfoEntity>() {
            @Override
            public void onChanged(PassengerInfoEntity passengerInfoEntity) {
                if (passengerInfoEntity.getBusinessType() == 6 || passengerInfoEntity.getBusinessType() == 7 || passengerInfoEntity.getBusinessType() == 8) {
                    Intent intent = new Intent();
                    intent.putExtra(TravelActivity.ORDERI_ID, passengerInfoEntity.getOrderNo());
                    LongRangeDrivingDetails.start(activity, intent);

                } else if (passengerInfoEntity.getBusinessType() == 9) {
                    Intent intent = new Intent();
                    intent.putExtra(TravelActivity.ORDERI_ID, passengerInfoEntity.getOrderNo());
                    CharterOrderDetailsActivity.start(activity, intent);
                } else {
                    showGradOrderDialog(passengerInfoEntity);
                }


            }
        });
        //我的钱包
        xviewModel.uc.ucMyWallet.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                MyWalletActivity.start(activity);
                MQTTService.start(YmxApp.getInstance(), MQTTService.Action);
            }
        });
        xviewModel.uc.ucGrapPassengerSucess.observe(this, new Observer<ConfirmOrderEntity>() {
            @Override
            public void onChanged(ConfirmOrderEntity confirmOrderEntity) {

                if (confirmOrderEntity.getCategoryType() == 2) {
                    Intent intent = new Intent();
                    intent.putExtra(TravelActivity.ORDERI_ID,  confirmOrderEntity.getOrderNo());
                    CarPoolDetailsActivity.start(activity, intent);
                } else {
                    showAutoReceiveOrderDialog(HomeViewModel.manualOrderType, confirmOrderEntity.getOrderNo());
                }

            }
        });

        xviewModel.uc.ucCancelOrder.observe(this, new Observer<String>() {

            @Override
            public void onChanged(String info) {
                GrapOrderTipsDialog grapOrderTipsDialog = new GrapOrderTipsDialog(activity).setContxtText(info);
                grapOrderTipsDialog.show();
                xviewModel.getHeadDetailInfo();

            }
        });
        xviewModel.uc.refreshSucess.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                xviewModel.driverStatus.set(YmxCache.getCarState());
                xbinding.refreshLayout.finishRefresh();
            }
        });


        xviewModel.uc.ucServiceCompleted.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                xviewModel.getHeadDetailInfo();
            }
        });
        xviewModel.uc.ucSystemCancelOrder.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                xviewModel.getHeadDetailInfo();
            }
        });


        xviewModel.uc.ucNetChange.observe(this, new Observer<NetChangeEntity>() {
            @Override
            public void onChanged(NetChangeEntity netChangeEntity) {
                xbinding.refreshLayout.finishRefresh();
                MQTTService.start(YmxApp.getInstance(), MQTTService.Action);
            }
        });


        xviewModel.uc.ucTodayOrder.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                TodayOrderActivity.start(activity);
                MQTTService.start(YmxApp.getInstance(), MQTTService.Action);
            }
        });

        xviewModel.uc.ucTodayIncome.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                Intent intent = new Intent();
                intent.setClass(activity, TodayIncomeActivity.class);
                startActivity(intent);

                MQTTService.start(YmxApp.getInstance(), MQTTService.Action);
            }
        });


        xviewModel.uc.ucLockDrvier.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                if (xviewModel.lockDrvierState != null && xviewModel.lockDrvierState.get() == 1) {
                    xviewModel.driverLock(0);
                } else if (xviewModel.lockDrvierState != null && xviewModel.lockDrvierState.get() == 0) {
                    new DefaultStyleDialog(activity)
                            .setBody("您确定要锁定吗？锁定后系统将不再为您匹配新的乘客")
                            .setNegativeText("取消")
                            .setPositiveText("确定锁定")
                            .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                                @Override
                                public void negative(Dialog dialog) {
                                    dialog.dismiss();

                                }

                                @Override
                                public void positive(Dialog dialog) {
                                    dialog.dismiss();
                                    xviewModel.driverLock(1);
                                }
                            }).show();
                }

            }
        });


        xviewModel.uc.ucLockDrvierHiHt.observe(this, new Observer<DriverLockEntity>() {
            @Override
            public void onChanged(DriverLockEntity driverLockEntity) {

                new DefaultStyleDialog(activity)
                        .setBody(driverLockEntity.getLockTips())
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
                        }).show();
            }
        });
        xviewModel.uc.ucSafetyTipsShow.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                safetyTipsDialog = new SafetyTipsDialog(getActivity());
                safetyTipsDialog.show();
            }
        });

    }


    public void showAutoReceiveOrderDialog(int type, String orderNo) {
        String grapOrderTipsText = "";
        if (type == HomeViewModel.autoOrderType) {
            grapOrderTipsText = getString((R.string.sys_auto_receive_order));
        } else if (type == HomeViewModel.manualOrderType) {
            grapOrderTipsText = getString((R.string.grad_order_dialog_hiht));
        }

        GrapOrderTipsDialog grapOrderTipsDialog = new GrapOrderTipsDialog(activity).setContxtText(grapOrderTipsText);


        grapOrderTipsDialog.show();


        new Handler().postDelayed(new Runnable() {
            public void run() {
                grapOrderTipsDialog.dismiss();
                if (type == HomeViewModel.autoOrderType) {
                    PassengerInfoEntity passengerInfoEntity = xviewModel.uc.ucPassengerInfo.getValue();
                    if (passengerInfoEntity.getBusinessType() == 6 || passengerInfoEntity.getBusinessType() == 7 || passengerInfoEntity.getBusinessType() == 8) {
                        Intent intent = new Intent();
                        intent.putExtra(TravelActivity.ORDERI_ID, passengerInfoEntity.getOrderNo());
                        LongRangeDrivingDetails.start(activity, intent);

                    } else if (passengerInfoEntity.getBusinessType() == 9) {
                        Intent intent = new Intent();
                        intent.putExtra(TravelActivity.ORDERI_ID, passengerInfoEntity.getOrderNo());
                        CharterOrderDetailsActivity.start(activity, intent);
                    } else {
                        if (passengerInfoEntity != null) {
                            Intent intent = new Intent();
                            intent.putExtra("passerInfo", passengerInfoEntity);
                            TravelActivity.start(activity, intent);
                        } else {
                            TravelActivity.start(activity);
                        }
                    }

                } else if (type == HomeViewModel.manualOrderType) {
                    Intent intent = new Intent();
                    intent.putExtra(TravelActivity.ORDERI_ID, orderNo);
                    TravelActivity.start(activity, intent);
                }


            }
        }, 2000);
    }

    // 点击抢单的弹窗的窗口
    private void showGradOrderDialog(PassengerInfoEntity passengerInfo) {
        new GrapOrderDailog(activity, passengerInfo)
                .setNegativeText(getString(R.string.grad_order_dialog_cancal))
                .setPositiveText(getString(R.string.grad_order_dialog_confirm))
                .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                    @Override
                    public void negative(Dialog dialog) {
                        dialog.dismiss();
                        xviewModel.cancelOrder(passengerInfo.getOrderNo());
                    }

                    @Override
                    public void positive(Dialog dialog) {
                        dialog.dismiss();
                        xviewModel.grabOrder(passengerInfo.getOrderNo());
                    }
                }).show();
    }
}
