package com.ymx.driver.ui.longrange.driving;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.utils.overlay.MovingPointOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.bumptech.glide.Glide;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseMapActivity;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityRangeDriverDetailsBinding;

import com.ymx.driver.databinding.RangeDrivingDetailsPopupViewBinding;

import com.ymx.driver.dialog.OrderCirculationDialog;
import com.ymx.driver.entity.RangDrivingCancelOrder;
import com.ymx.driver.entity.app.LongDrivingPaySuccessEntigy;
import com.ymx.driver.entity.app.OrderCirculationEntity;
import com.ymx.driver.entity.app.OrderCirculationInfo;
import com.ymx.driver.entity.app.PassengerItemInfo;
import com.ymx.driver.entity.app.mqtt.LongDriverSysTemCancalOrder;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.map.AMapUtil;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.map.overlay.DrivingRouteOverlay;
import com.ymx.driver.mqtt.MQTTService;
import com.ymx.driver.ui.longrange.driving.frament.LongRangeCirculationWaitingFrament;
import com.ymx.driver.ui.longrange.driving.frament.LongRangePremiumFrament;
import com.ymx.driver.ui.mine.Frament.PayDialogFragment;
import com.ymx.driver.util.NavUtil;
import com.ymx.driver.util.NavigationMapUtils;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.util.VoicePlayMannager;
import com.ymx.driver.view.ClassicPopupWindow;
import com.ymx.driver.view.SwipeButton;
import com.ymx.driver.view.recyclerview.RecyclerAdapter;
import com.ymx.driver.view.recyclerview.RecyclerViewHolder;
import com.ymx.driver.viewmodel.longrangdriving.LongRangeDrivingDetailsViewModel;
import com.ymx.driver.viewmodel.mine.NavSettingViewModel;
import com.ymx.driver.viewmodel.travel.TravelViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import static com.amap.api.services.route.RouteSearch.DRIVING_SINGLE_DEFAULT;


public class LongRangeDrivingDetails extends BaseMapActivity<ActivityRangeDriverDetailsBinding, LongRangeDrivingDetailsViewModel> {
    public static final String ORDERI_ID = "orderNo";
    private LatLonPoint mStartPoint; //起点
    private LatLonPoint mEndPoint;//终点
    private String orderNo;
    private MovingPointOverlay smoothMarker;
    private static final String MarkerObj = "driver_car";
    private float bear;
    private int locateSuccess = 0;
    private int ocateSuccessNumber = 10;
    //距离状态
    private int distanceStatus = 0;


    private LinearLayoutManager linearLayoutManager;
    private List<PassengerItemInfo> passenGerList;
    private RecyclerAdapter<PassengerItemInfo> adapter;
    private DefaultStyleDialog gpsDialog;


    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, LongRangeDrivingDetails.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_enter_left, R.anim.slide_exit_right);
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_range_driver_details;
    }

    @Override
    public int initVariableId() {
        return com.ymx.driver.BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(ORDERI_ID)) {
            orderNo = intent.getStringExtra(ORDERI_ID);
            xviewModel.orderId.set(orderNo);
            xviewModel.recoverOrderDetails(orderNo);
        }

//        initOrderCirculationDialog();
    }

    public void initOrderCirculationDialog() {
        new OrderCirculationDialog(activity)
                .setTitle("请输入流转的司机编码")
                .setNegativeText("取消")
                .setPositiveText("流转")
                .setOnDialogListener(new OrderCirculationDialog.DialogListener() {
                    @Override
                    public void negative(Dialog dialog) {
                        dialog.dismiss();

                    }

                    @Override
                    public void positive(Dialog dialog) {
                        if (TextUtils.isEmpty(xviewModel.driverId.get())) {
                            UIUtils.showToast("请填写司机编号 ");
                            return;
                        }
                        dialog.dismiss();
                        xviewModel.orderCirculation(orderNo, xviewModel.driverId.get());


                    }

                    @Override
                    public void orderqrcode(Dialog dialog) {
                        Bundle bundle1 = new Bundle();
                        bundle1.putString(OrderCirculationQrcodeFrament.ORDER_ID, orderNo);
                        OrderCirculationQrcodeFrament.newInstance(bundle1).show(getSupportFragmentManager(),
                                OrderCirculationQrcodeFrament.class.getSimpleName());
                    }
                }).show();
    }


    public void reonseDialog(String reonse) {
        new DefaultStyleDialog(activity)
                .setBody(reonse)
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


    @Override
    protected void locateSuccess(AMapLocation aMapLocation) {
        if (aMapLocation == null || aMapLocation.getLatitude() == 0 || aMapLocation.getLongitude() == 0) {
            return;
        }
        bear = aMapLocation.getBearing();


        if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_ROADING) {

            locateSuccess++;
            if (xviewModel.lat != null && xviewModel.lat.get() != null) {
                mStartPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                mEndPoint = new LatLonPoint(xviewModel.lat.get(), xviewModel.lng.get());
            }

            if (xviewModel.orderStatus.get() == LongRangeDrivingDetailsViewModel.DRIVER_STATE_ROADING) {
                if (locateSuccess / ocateSuccessNumber == 1) {
                    clearMap();
                    removeMapRouteView();

                    if (distanceStatus == 1) {
                        searchRouteResult();

                    }
                    locateSuccess = 0;


                }
                // 小于300KM 地图规划策略
                if (distanceStatus == 0) {

                    searchRouteResult();
                }


            }
        }


    }


    @Override
    protected TextureMapView getTextureMapView() {
        return xbinding.mapView;
    }

    @Override
    protected void initMapLoad() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        xbinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        xbinding.btnSubmit.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {

            }

            @Override
            public void onSwipeCancel() {

            }

            @Override
            public void onSwipeConfirm() {
                AMapLocation aMapLocation = LocationManager.getInstance(getApplicationContext()).getAMapLocation();
                MQTTService.start(YmxApp.getInstance(), MQTTService.Action);

                if (!NavUtil.isGpsOpen(getApplicationContext())) {
                    gpsDialog = new DefaultStyleDialog(activity)

                            .setBody(getString(R.string.open_gps_body))
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

                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    try {
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException ex) {

                                        intent.setAction(Settings.ACTION_SETTINGS);
                                        try {
                                            startActivity(intent);
                                        } catch (Exception e) {
                                        }
                                    }


                                }
                            });
                    gpsDialog.show();
                } else {
                    if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() == 2) {

                        new DefaultStyleDialog(activity)
                                .setBody("是否确认行程开始？\n" +
                                        "\n" +
                                        "请确认所有乘客已上车")
                                .setNegativeText(getString(R.string.driver_cancel_order_cancel))
                                .setPositiveText("确定")
                                .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                                    @Override
                                    public void negative(Dialog dialog) {
                                        dialog.dismiss();

                                    }

                                    @Override
                                    public void positive(Dialog dialog) {

                                        xviewModel.updateOrderDetailsStatus(orderNo, aMapLocation.getLongitude(), aMapLocation.getLatitude(), 3, "");
                                        dialog.dismiss();
                                    }
                                }).show();


                    } else if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() == 4) {
                        new DefaultStyleDialog(activity)
                                .setBody("是否确认行程结束？请确认所有乘客付款情况")
                                .setNegativeText("取消")
                                .setPositiveText("确定")
                                .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                                    @Override
                                    public void negative(Dialog dialog) {
                                        dialog.dismiss();

                                    }

                                    @Override
                                    public void positive(Dialog dialog) {

                                        xviewModel.updateOrderDetailsStatus(orderNo, aMapLocation.getLongitude(), aMapLocation.getLatitude(), 4, "");
                                        dialog.dismiss();
                                    }
                                }).show();

                    }
                }


            }
        });
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter = new RecyclerAdapter<PassengerItemInfo>(passenGerList, R.layout.long_rang_driving_details_item_view) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, PassengerItemInfo item) {

                ImageView img = holder.get(R.id.img);
                if (!TextUtils.isEmpty(item.getHeadimg())) {
                    Glide.with(activity)
                            .load(item.getHeadimg())
                            .into(img);
                }
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupWindow(item, v, position);
                    }
                });


                if (!TextUtils.isEmpty(item.getPhone())) {
                    holder.setText(R.id.phoneNumber, getCustomerphone(item.getPhone()));
                    holder.setVisibility(R.id.phoneNumber, View.VISIBLE);
                } else {
                    holder.setVisibility(R.id.phoneNumber, View.GONE);
                }

                if (!TextUtils.isEmpty(item.getDrivingTime())) {
                    holder.setText(R.id.time, item.getDrivingTime());
                    if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null) {
                        if (xviewModel.orderStatus.get() == LongRangeDrivingDetailsViewModel.DRIVER_STATE_ROADING) {
                            holder.setVisibility(R.id.time, View.GONE);
                        } else {
                            holder.setVisibility(R.id.time, View.VISIBLE);
                        }
                    }
                } else {
                    holder.setVisibility(R.id.time, View.GONE);
                }
                if (!TextUtils.isEmpty(item.getRideNumber())) {
                    holder.setText(R.id.number, item.getRideNumber() + "人");
                }

                if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null) {
                    if (xviewModel.orderStatus.get() == LongRangeDrivingDetailsViewModel.DRIVER_STATE_ROADING) {
                        if (item.getPayState() == 1) {
                            holder.setVisibility(R.id.payMent, View.VISIBLE);
                            holder.setVisibility(R.id.payImg, View.VISIBLE);

                        } else if (item.getPayState() == 0) {
                            holder.setVisibility(R.id.payMent, View.GONE);
                            holder.setVisibility(R.id.payImg, View.GONE);
                        }

                    } else {
                        holder.setVisibility(R.id.payMent, View.GONE);
                        holder.setVisibility(R.id.payImg, View.GONE);
                    }
                }

                if (position == passenGerList.size() - 1) {
                    holder.setVisibility(R.id.lineView, View.GONE);
                } else {
                    holder.setVisibility(R.id.lineView, View.VISIBLE);
                }


            }
        };

        xbinding.recyclerView.setLayoutManager(linearLayoutManager);
        xbinding.recyclerView.setAdapter(adapter);

    }

    public void showPopupWindow(PassengerItemInfo item, View view, int position) {
        ClassicPopupWindow.Builder popWindow = (xviewModel.orderStatus.get() == LongRangeDrivingDetailsViewModel.DRIVER_STATE_ROADING) ? new ClassicPopupWindow.Builder(activity, 1) : new ClassicPopupWindow.Builder(activity, 0);

        RangeDrivingDetailsPopupViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.range_driving_details_popup_view,
                null, false);

        binding.navigationLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();

                if (item != null) {
                    xviewModel.startName.set(item.getAddress());
                    xviewModel.passengerInfo.set(getCustomerphone(item.getPhone()));
                    xviewModel.lat.set(item.getLat());
                    xviewModel.lng.set(item.getLng());
                    mStartPoint = new LatLonPoint(LocationManager.getInstance(activity).getLatitude(), LocationManager.getInstance(activity).getLongitude());
                    mEndPoint = new LatLonPoint(item.getLat(), item.getLng());
                }

                if (xviewModel.orderStatus.get() == LongRangeDrivingDetailsViewModel.DRIVER_STATE_ROADING) {
                    smoothMarker = null;
                }
                getaMap().clear();
                searchRouteResult();


                navigation(item.getLat(), item.getLng(), item.getAddress());
            }
        });

        if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() == 2) {
            binding.callPhone.setVisibility(View.VISIBLE);
            if (item.getPayState() == 0) {
                binding.canceOrder.setVisibility(View.VISIBLE);
            } else {
                binding.canceOrder.setVisibility(View.GONE);
            }

            binding.qRcodeLl.setVisibility(View.GONE);
            binding.navigationlineView.setVisibility(View.GONE);
        } else if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() == 4) {
            binding.callPhone.setVisibility(View.GONE);
            binding.canceOrder.setVisibility(View.GONE);
            if (item.getPayState() == 0) {
                binding.qRcodeLl.setVisibility(View.VISIBLE);
                binding.navigationlineView.setVisibility(View.GONE);
            } else {
                binding.navigationlineView.setVisibility(View.VISIBLE);
                binding.qRcodeLl.setVisibility(View.GONE);

            }
            if (item.getPriceMarkupState() == 0) {
                binding.premiumRl.setVisibility(View.GONE);
            } else {
                binding.premiumRl.setVisibility(View.VISIBLE);
            }

        }
        binding.callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xviewModel.phoneNum.set(item.getPhone());
                popWindow.dismiss();
                requestPermission(PermissionConfig.PC_CALL_PHONE, false, Manifest.permission.CALL_PHONE);
            }
        });

        binding.canceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                showDriverCancelOrderDialog(item.getOrderNo());
            }
        });


        binding.premiumRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable(LongRangePremiumFrament.PASSENGER_INFO, item);
                LongRangePremiumFrament.newInstance(bundle1).show(getSupportFragmentManager(),
                        LongRangePremiumFrament.class.getSimpleName());
            }
        });


        binding.qRcodeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popWindow.dismiss();
                VoicePlayMannager.getInstance(getApplicationContext()).play(R.raw.long_drvingqcode);
                Bundle bundle = new Bundle();
                bundle.putString(RangeDrivingPayMoneyQrcodeFrament.ORDER_ID,
                        item.getOrderNo());
                RangeDrivingPayMoneyQrcodeFrament.newInstance(bundle).show(getSupportFragmentManager(),
                        RangeDrivingPayMoneyQrcodeFrament.class.getSimpleName());


            }
        });

        popWindow.setView(binding.getRoot()).build().showAsTop_AnchorRight_Right(view, -20);

    }


    @Override
    public void initViewObservable() {
        xviewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        xviewModel.uc.ucCall.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                requestPermission(PermissionConfig.PC_CALL_PHONE, false, Manifest.permission.CALL_PHONE);
            }
        });

        xviewModel.uc.ucNavigate.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (xviewModel.lng != null && xviewModel.lat != null) {
                    navigation(xviewModel.lat.get(), xviewModel.lng.get(), xviewModel.startName.get());
                }
            }
        });


        xviewModel.uc.ucUpdateOrderStatus.observe(this, new Observer<PassengerInfoEntity>() {
            @Override
            public void onChanged(PassengerInfoEntity passengerInfoEntity) {

                if (xviewModel.updateOrderContent != null && !TextUtils.isEmpty(xviewModel.updateOrderContent.get())) {
                    xbinding.btnSubmit.setText(xviewModel.updateOrderContent.get());
                }
                if (xviewModel.orderStatus.get() == LongRangeDrivingDetailsViewModel.DRIVER_ORDER_FINISH) {

                    YmxCache.setOrderId("");
                    VoicePlayMannager.getInstance(getApplicationContext()).play(R.raw.long_driving_serving_finish);
                    doSthIsExit();


                } else if (xviewModel.orderStatus.get() == LongRangeDrivingDetailsViewModel.DRIVER_STATE_ROADING) {

//                    if (passengerInfoEntity.getCirculationState() == 0) {
                    if (passengerInfoEntity.getPassengerList() != null && !passengerInfoEntity.getPassengerList().isEmpty()) {
                        passenGerList.clear();
                        passenGerList.addAll(passengerInfoEntity.getPassengerList());
                        adapter.notifyDataSetChanged();
                        PassengerItemInfo item = passengerInfoEntity.getPassengerList().get(0);
                        if (item != null) {
                            if (xviewModel.loadMapInfo.get() == true) {
                                xviewModel.startName.set(item.getAddress());
                                xviewModel.passengerInfo.set(getCustomerphone(item.getPhone()));
                                xviewModel.lat.set(item.getLat());
                                xviewModel.lng.set(item.getLng());
                                mStartPoint = new LatLonPoint(LocationManager.getInstance(activity).getLatitude(), LocationManager.getInstance(activity).getLongitude());
                                mEndPoint = new LatLonPoint(item.getLat(), item.getLng());
                            }
                        }
                    }
                    YmxCache.setOrderId(orderNo);
                    if (passengerInfoEntity.getCirculationState() == 1) {
                        initOrderCirculationDialog();
                    }
                }

            }
        });

        xviewModel.uc.ucRecoverOrderDetails.observe(this, new Observer<PassengerInfoEntity>() {
            @Override
            public void onChanged(PassengerInfoEntity passengerInfoEntity) {

                if (passengerInfoEntity.getCirculationState() == 1 && xviewModel.initWatiDialog.get() == 1) {

                    if (!isFinishing()) {
                        Bundle bundle = new Bundle();
                        bundle.putString(LongRangeCirculationWaitingFrament.DRIVER_NAME, passengerInfoEntity.getCirculationDriverName());
                        bundle.putString(LongRangeCirculationWaitingFrament.DRIVER_ID, passengerInfoEntity.getCirculationDriverNo());
                        bundle.putLong(LongRangeCirculationWaitingFrament.DRIVER_TIME, passengerInfoEntity.getCirculationTimeout());
                        LongRangeCirculationWaitingFrament.newInstance(bundle).show(getSupportFragmentManager(),
                                LongRangeCirculationWaitingFrament.class.getSimpleName());
                    }

                }

                orderNo = xviewModel.orderId.get();
                if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() == LongRangeDrivingDetailsViewModel.DRIVER_STATE_TO_PASSENGERS) {
                    xbinding.btnSubmit.setText("行程开始");
                } else if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() == LongRangeDrivingDetailsViewModel.DRIVER_STATE_ROADING) {
                    xbinding.btnSubmit.setText("行程结束");
                }


            }
        });


        xviewModel.uc.ucChangeMap.observe(this, new Observer<Void>() {

            @Override
            public void onChanged(Void aVoid) {
                searchRouteResult();
            }
        });

        xviewModel.uc.uclist.observe(this, new Observer<List<PassengerItemInfo>>() {
            @Override
            public void onChanged(List<PassengerItemInfo> list) {
                if (passenGerList != null && !passenGerList.isEmpty()) {
                    passenGerList.clear();
                }


                passenGerList = list;

                PassengerItemInfo passengerItemInfo = passenGerList.get(0);


                if (passengerItemInfo != null) {
                    xviewModel.startName.set(passengerItemInfo.getAddress());
                    xviewModel.passengerInfo.set(getCustomerphone(passengerItemInfo.getPhone()));
                    xviewModel.lat.set(passengerItemInfo.getLat());
                    xviewModel.lng.set(passengerItemInfo.getLng());
                    mStartPoint = new LatLonPoint(LocationManager.getInstance(activity).getLatitude(), LocationManager.getInstance(activity).getLongitude());
                    mEndPoint = new LatLonPoint(passengerItemInfo.getLat(), passengerItemInfo.getLng());
                }

                getaMap().clear();
                searchRouteResult();

                initRecyclerView();

            }
        });
        // 取消子订单重新刷新详情
        xviewModel.uc.ucCancelOrder.observe(this, new Observer<RangDrivingCancelOrder>() {
            @Override
            public void onChanged(RangDrivingCancelOrder list) {
                xviewModel.recoverOrderDetails(orderNo);
            }
        });
        xviewModel.uc.ucUpdateOrderInfo.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String orderId) {
                xviewModel.recoverOrderDetails(orderId);
            }
        });


        xviewModel.uc.ucSysTemCancalOrder.observe(this, new Observer<LongDriverSysTemCancalOrder>() {
            @Override
            public void onChanged(LongDriverSysTemCancalOrder longDriverSysTemCancalOrder) {
                if (longDriverSysTemCancalOrder != null) {
                    xviewModel.recoverOrderDetails(longDriverSysTemCancalOrder.getDriverOrderNo());
                }
                if (longDriverSysTemCancalOrder.getDriverState() == LongRangeDrivingDetailsViewModel.DRIVER_ORDER_CLOSED) {
                    xbinding.recyclerView.setVisibility(View.GONE);
                }

                new DefaultStyleDialog(activity)
                        .setBody(longDriverSysTemCancalOrder.getTips())
                        .setPositiveText("确认")
                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                            @Override
                            public void negative(Dialog dialog) {

                                if (longDriverSysTemCancalOrder.getDriverState() == LongRangeDrivingDetailsViewModel.DRIVER_ORDER_CLOSED) {
                                    doSthIsExit();

                                }
                                dialog.dismiss();

                            }

                            @Override
                            public void positive(Dialog dialog) {

                                if (longDriverSysTemCancalOrder.getDriverState() == LongRangeDrivingDetailsViewModel.DRIVER_ORDER_CLOSED) {
                                    doSthIsExit();
                                }
                                dialog.dismiss();
                            }
                        }).show();


            }
        });


        xviewModel.uc.ucPayDialog.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String orderId) {
                Bundle bundle = new Bundle();
                bundle.putString(PayDialogFragment.ORDER_NO,
                        orderId);
                PayDialogFragment.newInstance(bundle).show(getSupportFragmentManager(),
                        PayDialogFragment.class.getSimpleName());
            }
        });
        xviewModel.uc.ucPaySuccess.observe(this, new Observer<LongDrivingPaySuccessEntigy>() {
            @Override
            public void onChanged(LongDrivingPaySuccessEntigy longDrivingPaySuccessEntigy) {

                if (longDrivingPaySuccessEntigy.getDriverOrderNo().equals(orderNo)) {

                    if (passenGerList != null && !passenGerList.isEmpty() && longDrivingPaySuccessEntigy != null) {
                        for (int i = 0; i < passenGerList.size(); i++) {
                            PassengerItemInfo passengerItemInfo = passenGerList.get(i);
                            if (passengerItemInfo.getOrderNo().equals(longDrivingPaySuccessEntigy.getPaaaengerOrderNo())) {
                                passenGerList.get(i).setPayState(1);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();


                }

            }
        });


        xviewModel.uc.ucOrderCirculation.observe(this, new Observer<OrderCirculationEntity>() {
            @Override
            public void onChanged(OrderCirculationEntity orderCirculationEntity) {

                if (orderCirculationEntity != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(LongRangeCirculationWaitingFrament.DRIVER_NAME, orderCirculationEntity.getCirculationDriverName());
                    bundle.putString(LongRangeCirculationWaitingFrament.DRIVER_ID, orderCirculationEntity.getCirculationDriverNo());
                    bundle.putLong(LongRangeCirculationWaitingFrament.DRIVER_TIME, orderCirculationEntity.getCirculationTimeout());
                    LongRangeCirculationWaitingFrament.newInstance(bundle).show(getSupportFragmentManager(),
                            LongRangeCirculationWaitingFrament.class.getSimpleName());

                }

            }
        });

        xviewModel.uc.ucOrderCirculationNotice.observe(this, new Observer<OrderCirculationInfo>() {
            @Override
            public void onChanged(OrderCirculationInfo orderCirculationInfo) {


                reonseDialog(orderCirculationInfo.getTips());
                xviewModel.recoverOrderDetails(orderNo);
            }

        });

        xviewModel.uc.updatePassengerList.observe(this, new Observer<List<PassengerItemInfo>>() {
            @Override
            public void onChanged(List<PassengerItemInfo> passengerItemInfos) {
                if (passengerItemInfos != null && !passengerItemInfos.isEmpty()) {
                    passenGerList.clear();
                    passenGerList.addAll(passengerItemInfos);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    protected void permissionGranted(int requestCode) {
        if (requestCode == PermissionConfig.PC_CALL_PHONE) {
            try {
                if (TextUtils.isEmpty(xviewModel.phoneNum.get())) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + xviewModel.phoneNum.get());
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
        overridePendingTransition(R.anim.slide_enter_right, R.anim.slide_exit_left);
    }

    private void showDriverCancelOrderDialog(String orderId) {
        new DefaultStyleDialog(activity)
                .setBody("确定没有接到乘客吗？")
                .setNegativeText("取消订单")
                .setPositiveText("继续等待")
                .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                    @Override
                    public void negative(Dialog dialog) {
                        dialog.dismiss();
                        if (!TextUtils.isEmpty(orderId)) {
                            xviewModel.cancelOrder(orderId);
                        }
                    }

                    @Override
                    public void positive(Dialog dialog) {
                        dialog.dismiss();

                    }
                }).show();
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult() {
        Log.i("TAG", "searchRouteResult: ");

        RouteSearch routeSearch = new RouteSearch(activity);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint,
                mEndPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, DRIVING_SINGLE_DEFAULT,
                null, null, "");
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
                if (getaMap() == null) {
                    return;
                }


                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {


                    if (result != null && result.getPaths() != null) {
                        if (result.getPaths().size() > 0) {
                            final DrivePath drivePath = result.getPaths().get(0);
                            if (drivePath == null) {
                                return;
                            }
                            int dis = (int) drivePath.getDistance();
                            int dur = (int) drivePath.getDuration();
                            StringBuffer sb = new StringBuffer();
                            sb.append("剩余");

                            if (dis > 300000) {
                                distanceStatus = 1;
                                ocateSuccessNumber = 30;
                            }
                            if (dis < 1000) {
                                sb.append(dis).append("米");
                            } else if (dis >= 1000) {
                                Double disDistans = (double) dis / 1000;
                                String length = new Formatter().format("%.2f", disDistans).toString();
                                sb.append(length).append("公里");
                            }
                            sb.append(" ");
                            sb.append(AMapUtil.getFriendlyTime(dur));
                            xviewModel.orderTimeDesc.set(sb.toString());

                            List<LatLng> list = new ArrayList<>();
                            for (DriveStep step : drivePath.getSteps()) {
                                List<LatLonPoint> latlonPoints = step.getPolyline();
                                for (LatLonPoint latlonpoint : latlonPoints) {
                                    list.add(new LatLng(latlonpoint.getLatitude(), latlonpoint.getLongitude()));
                                }
                            }
                            if (list.size() <= 0) {
                                return;
                            }


                            if (xviewModel.orderStatus.get() == LongRangeDrivingDetailsViewModel.DRIVER_STATE_ROADING
                            ) {
                                //添加司机的车marker
                                if (smoothMarker != null) {
                                    xviewModel.smoothRunningLatLng.add(list.get(0));
                                    smoothMarker.setPoints(xviewModel.smoothRunningLatLng);
                                    smoothMarker.setTotalDuration(2);
                                    smoothMarker.startSmoothMove();
                                    xviewModel.smoothRunningLatLng.remove(0);

                                } else {
                                    getaMap().clear();
                                    Marker marker = getaMap().addMarker(new MarkerOptions()
                                            .position(list.get(0))
                                            .zIndex(1)
                                            .anchor(0.5f, 0.5f)
                                            .rotateAngle(360 - bear)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
                                    marker.setObject(MarkerObj);
                                    marker.showInfoWindow();
                                    smoothMarker = new MovingPointOverlay(getaMap(), marker);
                                    xviewModel.smoothRunningLatLng.clear();
                                    xviewModel.smoothRunningLatLng.add(list.get(0));
                                    getaMap().addMarker(new MarkerOptions()
                                            .position(AMapUtil.convertToLatLng(result.getTargetPos()))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_end)));


                                }


                            } else {
                                getaMap().addMarker(new MarkerOptions()
                                        .position(AMapUtil.convertToLatLng(result.getStartPos()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_from)));
                                getaMap().addMarker(new MarkerOptions()
                                        .position(AMapUtil.convertToLatLng(result.getTargetPos()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_end)));
                            }

                            if (dis >= 400000) {
                                List<DriveStep> driveSteps = drivePath.getSteps();
                                List<DriveStep> newdriveSteps = new ArrayList<>();
                                newdriveSteps.add(driveSteps.get(0));
                                newdriveSteps.add(driveSteps.get(drivePath.getSteps().size() - 1));
                                drivePath.setSteps(newdriveSteps);

                            }

                            // 自定义的一个绘制处理
                            DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                                    activity, getaMap(), drivePath,
                                    result.getStartPos(),
                                    result.getTargetPos(), null);
                            drivingRouteOverlay.setNodeIconVisibility(false); // 是否显示节点图标,这个里面没有设置所以没有，也不需要显示
                            drivingRouteOverlay.setIsColorfulline(true); // 是否显示线路的交通状态
                            drivingRouteOverlay.removeFromMap(); // 先进行移除掉
                            drivingRouteOverlay.addToMap(); // 添加到地图上面
                            // 根据线路的起点和终点自动缩放zoom值，显示在屏幕的中间
                            drivingRouteOverlay.zoomToSpan(200, 200,
                                    200, 400,
                                    new AMap.CancelableCallback() {
                                        @Override
                                        public void onFinish() {
                                            if (getaMap() == null) {
                                                return;
                                            }
                                        }

                                        @Override
                                        public void onCancel() {

                                        }
                                    });
                        }
                    }
                }
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
        routeSearch.calculateDriveRouteAsyn(query);
    }


    protected void removeMapRouteView() {
        if (null != getaMap()) {
            getaMap().clear();
        }

    }

    public String getCustomerphone(String phone) {
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(phone)) {
            sb.append("尾号");
            sb.append(phone.substring(7));

        }


        return sb.toString();
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


    public void clearMap() {
        if (smoothMarker != null) {
            smoothMarker.setMoveListener(null);
            smoothMarker.destroy();
            smoothMarker = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        clearMap();
        removeMapRouteView();
        if (gpsDialog != null && gpsDialog.isShowing()) {
            gpsDialog.dismiss();
        }
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_TRIP_REFRESH_DATA_CODE));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_HOME_FRAGMENT_DATA_CODE));
    }

}


