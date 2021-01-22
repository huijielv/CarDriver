package com.ymx.driver.ui.travel.activity;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseMapActivity;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityCarpoolDetailsBinding;

import com.ymx.driver.databinding.CarPoolDetailsUpdatePassengerBinding;
import com.ymx.driver.entity.app.CarPoolCancalOrderEntity;
import com.ymx.driver.entity.app.LongDrivingPaySuccessEntigy;
import com.ymx.driver.entity.app.PassengerItemInfo;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.map.AMapUtil;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.map.overlay.DrivingRouteOverlay;
import com.ymx.driver.ui.longrange.driving.RangeDrivingPayMoneyQrcodeFrament;
import com.ymx.driver.ui.main.activity.MainActivity;
import com.ymx.driver.ui.mine.Frament.PayDialogFragment;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.ClassicPopupWindow;
import com.ymx.driver.view.SwipeButton;
import com.ymx.driver.viewmodel.orderdetails.CarPoolDetailsViewModel;
import com.ymx.driver.viewmodel.travel.TravelViewModel;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import static com.amap.api.services.route.RouteSearch.DRIVING_SINGLE_DEFAULT;


public class CarPoolDetailsActivity extends BaseMapActivity<ActivityCarpoolDetailsBinding, CarPoolDetailsViewModel> {
    public static final String ORDERI_ID = "orderNo";
    private LinearLayoutManager linearLayoutManager;
    private String orderNo;
    private CarPoolAdapter carPoolAdapter;
    private List<PassengerItemInfo> passengerItemInfoList;
    private LatLonPoint mStartPoint; //起点
    private LatLonPoint mEndPoint;//终点
    private int actionType;
    private MovingPointOverlay smoothMarker;
    private static final String MarkerObj = "driver_car";
    private float bear;
    private int locateSuccess = 0;
    private int ocateSuccessNumber = 10;
    private int distanceStatus = 0;
    private PassengerItemInfo passengerInfo;

    private DefaultStyleDialog confimActionDialog;

    public static void start(Activity activity, Intent extras) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(activity, CarPoolDetailsActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    protected void locateSuccess(AMapLocation aMapLocation) {
        if (aMapLocation == null || aMapLocation.getLatitude() == 0 || aMapLocation.getLongitude() == 0) {
            return;
        }
        bear = aMapLocation.getBearing();
        if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_ROADING) {
            clearMap();

            locateSuccess++;
            if (xviewModel.lat != null && xviewModel.lat.get() != null) {
                mStartPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                mEndPoint = new LatLonPoint(xviewModel.lat.get(), xviewModel.lng.get());
            }
            if (xviewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_ROADING) {
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
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_carpool_details;
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
                if(carPoolAdapter.getData().size()==0){
                    AMapLocation aMapLocation = LocationManager.getInstance(YmxApp.getInstance()).getAMapLocation();
                    if (aMapLocation != null) {
                        xviewModel.driverCarpoolEndTrip(orderNo, aMapLocation.getLongitude(), aMapLocation.getLatitude());
                    }
                }else if ( carPoolAdapter.getData().size()>0){


                    new DefaultStyleDialog(activity)
                            .setBody("存在乘客未到达目的地，\n" +
                                    "请完成所有订单后结束行程")
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


                                }
                            }).show();
                }


            }
        });
        initRecyView();

    }

    public void initMap() {
        if (LocationManager.getInstance(activity).getLatitude() != 0
                && LocationManager.getInstance(activity).getLongitude() != 0) {
            moveToCamera(new LatLng(LocationManager.getInstance(activity).getLatitude(),
                    LocationManager.getInstance(activity).getLongitude()));


        }
    }

    public void initRecyView() {
        linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xbinding.recyclerView.setLayoutManager(linearLayoutManager);
        passengerItemInfoList = new ArrayList<>();
        carPoolAdapter = new CarPoolAdapter(passengerItemInfoList);
        xbinding.recyclerView.setAdapter(carPoolAdapter);

    }

    @Override
    protected TextureMapView getTextureMapView() {
        return xbinding.mapView;
    }

    @Override
    protected void initMapLoad() {
        initMap();
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(ORDERI_ID)) {
            orderNo = intent.getStringExtra(ORDERI_ID);
//            xviewModel.orderId.set(orderNo);
            xviewModel.recoverOrderDetails(orderNo);
        }
    }

    @Override
    public void initViewObservable() {
        xviewModel.uc.ucRecoverOrderDetails.observe(this, new Observer<PassengerInfoEntity>() {
            @Override
            public void onChanged(PassengerInfoEntity passengerInfoEntity) {

                try {
                    mStartPoint = new LatLonPoint(LocationManager.getInstance(activity).getLatitude(), LocationManager.getInstance(activity).getLongitude());
                    mEndPoint = new LatLonPoint(xviewModel.lat.get(), xviewModel.lng.get());
                    actionType = xviewModel.orderStatus.get();


                    if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == 4) {
                        moveIngCar(new LatLng(LocationManager.getInstance(activity).getLatitude(), LocationManager.getInstance(activity).getLongitude()), null);

                    } else {

                        getaMap().clear();
                        searchRouteResult();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                xbinding.btnSubmit.setText(xviewModel.buttonText.get());
                carPoolAdapter.setNewData(passengerInfoEntity.getPassengerList());

            }
        });

        xviewModel.uc.ucPowPopShow.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                showPopupWindow();
            }
        });

        xviewModel.uc.ucCancalOrder.observe(this, new Observer<CarPoolCancalOrderEntity>() {
            @Override
            public void onChanged(CarPoolCancalOrderEntity carPoolCancalOrderEntity) {
                if (carPoolCancalOrderEntity.getDriverState() == 1) {
                    xviewModel.recoverOrderDetails(carPoolCancalOrderEntity.getOrderNo());
                } else if (carPoolCancalOrderEntity.getDriverState() == 8) {
                    doSthIsExit();
                    YmxCache.setOrderId("");
                    MainActivity.start(activity);
                }
            }
        });

        xviewModel.uc.ucSystemCancalOrder.observe(this, new Observer<CarPoolCancalOrderEntity>() {
            @Override
            public void onChanged(CarPoolCancalOrderEntity carPoolCancalOrderEntity) {

                new DefaultStyleDialog(activity)
                        .setBody(carPoolCancalOrderEntity.getTips())
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
                                if (carPoolCancalOrderEntity.getDriverState() == 1) {
                                    xviewModel.recoverOrderDetails(carPoolCancalOrderEntity.getDriverOrderNo());
                                } else if (carPoolCancalOrderEntity.getDriverState() == 8) {
                                    doSthIsExit();
                                    YmxCache.setOrderId("");
                                    MainActivity.start(activity);
                                }

                            }
                        }).show();


            }
        });


        xviewModel.uc.ucErrorMsg.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                new DefaultStyleDialog(activity)
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
                        }).show();
            }
        });

        xviewModel.uc.ucPayDialog.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String orderId) {
                Bundle bundle = new Bundle();
                bundle.putString(PayDialogFragment.ORDER_NO,
                        passengerInfo.getOrderNo());
                PayDialogFragment.newInstance(bundle).show(getSupportFragmentManager(),
                        PayDialogFragment.class.getSimpleName());
            }
        });

        xviewModel.uc.ucPaySuccess.observe(this, new Observer<LongDrivingPaySuccessEntigy>() {
            @Override
            public void onChanged(LongDrivingPaySuccessEntigy longDrivingPaySuccessEntigy) {

//                if (longDrivingPaySuccessEntigy.getDriverOrderNo().equals(orderNo)) {

                xviewModel.recoverOrderDetails(longDrivingPaySuccessEntigy.getDriverOrderNo());
//                }

            }
        });

        xviewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

    }

    public void showPopupWindow() {
        ClassicPopupWindow.Builder popWindow = new ClassicPopupWindow.Builder(activity, 1);

        CarPoolDetailsUpdatePassengerBinding popWindowBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.car_pool_details_update_passenger,
                null, false);
        popWindow.setView(popWindowBinding.getRoot()).build().showAsBottom_AnchorRight_Right(xbinding.leftLine, 0);

        if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null) {
            switch (xviewModel.orderStatus.get()) {
                case 1:
                    popWindowBinding.updateOrderStatusIv.setBackground(UIUtils.getDrawable(R.drawable.icon_qujieichengke));
                    popWindowBinding.updateOrderStatusTv.setText("去接乘客");
                    break;
                case 2:
                    popWindowBinding.updateOrderStatusIv.setBackground(UIUtils.getDrawable(R.drawable.icon_daodashangchedian));
                    popWindowBinding.updateOrderStatusTv.setText("到达上车点");
                    break;
                case 3:
                    popWindowBinding.updateOrderStatusIv.setBackground(UIUtils.getDrawable(R.drawable.icon_yishangche));
                    popWindowBinding.updateOrderStatusTv.setText("乘客已上车");
                    popWindowBinding.canceOrderTv.setText("未接到乘客");
                    popWindowBinding.canceOrderIv.setBackground(getDrawable(R.drawable.icon_long_driving_weijiedaochengke));

                    break;

                case 4:
                    popWindowBinding.updateOrderStatusIv.setBackground(UIUtils.getDrawable(R.drawable.icon_daodamudidi));

                    if (passengerInfo.getPassengerState() == 4) {
                        popWindowBinding.updateOrderStatusTv.setText("到达目的地");
                    } else if (passengerInfo.getPassengerState() == 7) {
                        popWindowBinding.updateOrderStatusTv.setText("行程结束");
                    }

                    if (passengerInfo.getPayState() == 1) {
                        popWindowBinding.qRcodeLl.setVisibility(View.GONE);
                        popWindowBinding.qRcodeAndCancalLl.setVisibility(View.GONE);
                    } else if (passengerInfo.getPayState() == 0) {
                        popWindowBinding.qRcodeLl.setVisibility(View.VISIBLE);
                        popWindowBinding.qRcodeAndCancalLl.setVisibility(View.VISIBLE);
                    }
                    popWindowBinding.callPhone.setVisibility(View.GONE);
                    popWindowBinding.canceOrder.setVisibility(View.GONE);
                    break;
                case 7:
                    if (passengerInfo.getPayState() == 0) {
                        popWindowBinding.updateOrderStatusTv.setText("行程结束");
                        popWindowBinding.qRcodeLl.setVisibility(View.VISIBLE);
                        popWindowBinding.qRcodeAndCancalLl.setVisibility(View.VISIBLE);
                    }
                    popWindowBinding.callPhone.setVisibility(View.GONE);
                    popWindowBinding.canceOrder.setVisibility(View.GONE);
                    break;
            }


        }

        popWindowBinding.navigationLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();

                if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null) {
                    if (xviewModel.orderStatus.get() == 4 || xviewModel.orderStatus.get() == 7) {
                        mapNavigation(xviewModel.lat.get(), xviewModel.lng.get(), xviewModel.endName.get());
                    } else {
                        mapNavigation(xviewModel.lat.get(), xviewModel.lng.get(), xviewModel.startName.get());
                    }
                }


            }
        });


        popWindowBinding.updateOrderStatusLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null) {
                    switch (xviewModel.orderStatus.get()) {
                        case 1:
                            xviewModel.orderStartLocation(passengerInfo.getOrderNo(), 1);
                            break;
                        case 2:
                            updateOrderStatus(passengerInfo.getOrderNo(), 2);
                            break;
                        case 3:
                            updateOrderStatus(passengerInfo.getOrderNo(), 3);
                            break;
                        case 4:
                            if (passengerInfo.getPassengerState() == 4) {
                                updateOrderStatus(passengerInfo.getOrderNo(), 4);
                            } else if (passengerInfo.getPassengerState() == 7) {
                                xviewModel.orderStartLocation(passengerInfo.getOrderNo(), 5);
                            }


                            break;
                    }
                }
            }
        });

        popWindowBinding.callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                requestPermission(PermissionConfig.PC_CALL_PHONE, false, Manifest.permission.CALL_PHONE);
            }
        });
        popWindowBinding.canceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                new DefaultStyleDialog(activity)
                        .setBody("是否取消尾号" + xviewModel.passengerInfo.get() + "乘客订单？")
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
                                xviewModel.carpoolCanCalOrder(passengerInfo.getOrderNo());

                            }
                        }).show();

            }
        });

        popWindowBinding.qRcodeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString(RangeDrivingPayMoneyQrcodeFrament.ORDER_ID,
                        passengerInfo.getOrderNo());
                RangeDrivingPayMoneyQrcodeFrament.newInstance(bundle).show(getSupportFragmentManager(),
                        RangeDrivingPayMoneyQrcodeFrament.class.getSimpleName());
            }
        });


    }

    public void updateOrderStatus(String passengerNo, int actionType) {
        StringBuffer stringBuffer = new StringBuffer();
        if (actionType == 2) {
            stringBuffer.append("是否确定已到达" + xviewModel.passengerInfo.get() + "乘客上车点？");
        } else if (actionType == 3) {
            stringBuffer.append("是否确定" + xviewModel.passengerInfo.get() + "乘客已上车？");
        } else if (actionType == 4) {
            stringBuffer.append("是否确定" + xviewModel.passengerInfo.get() + "已到达目的地?");
        } else if (actionType == 5) {
            stringBuffer.append("是否确定" + xviewModel.passengerInfo.get() + "乘客结束行程并付款?");
        }

        confimActionDialog = new DefaultStyleDialog(activity)
                .setBody(stringBuffer.toString())
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

                        xviewModel.orderStartLocation(passengerNo, actionType);

                    }
                });

        confimActionDialog.show();


    }


    public void searchRouteResult() {
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


                            if (xviewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_ROADING
                            ) {

                                moveIngCar(list.get(0), result);


                            } else {
                                getaMap().addMarker(new MarkerOptions()
                                        .position(AMapUtil.convertToLatLng(result.getStartPos()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_from)));
                                getaMap().addMarker(new MarkerOptions()
                                        .position(AMapUtil.convertToLatLng(result.getTargetPos()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_end)));
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
                                    200, xbinding.btnSubmit.getHeight() + 400,
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


    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }


    public class CarPoolAdapter extends BaseQuickAdapter<PassengerItemInfo, BaseViewHolder> {

        public CarPoolAdapter(List<PassengerItemInfo> data) {
            super(R.layout.carpool_recy_item, data);
        }


        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, PassengerItemInfo passengerItemInfo) {
            LinearLayout Ll= baseViewHolder.getView(R.id.Ll);
            Ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passengerInfo = passengerItemInfo;

                    if (xviewModel.phoneNum != null && xviewModel.phoneNum.get() != null) {
                        if (xviewModel.phoneNum.get().equals(passengerItemInfo.getPhone())) {
                            showPopupWindow();
                        } else {
                            xviewModel.carpoolSwitchPassenger(passengerItemInfo.getOrderNo());

                        }
                    }else {
                        UIUtils.showToast("请稍后再试");

                    }


                }
            });
            TextView phoneNumberTv = baseViewHolder.getView(R.id.phoneNumber);
            TextView timeTv = baseViewHolder.getView(R.id.time);
            TextView numberTv = baseViewHolder.getView(R.id.number);

            if (!TextUtils.isEmpty(passengerItemInfo.getPhone()) && passengerItemInfo.getPhone().length() > 7) {
                StringBuffer sb = new StringBuffer();
                sb.append("尾号");
                sb.append(passengerItemInfo.getPhone().substring(7));
                phoneNumberTv.setText(sb.toString());
            }
            if (!TextUtils.isEmpty(passengerItemInfo.getRideNumber())) {
                numberTv.setText(passengerItemInfo.getRideNumber());
            }
            if (!TextUtils.isEmpty(passengerItemInfo.getAppointmentTime())) {
                timeTv.setText(passengerItemInfo.getAppointmentTime());
            }


        }

    }

    public void moveIngCar(LatLng latLng, DriveRouteResult result) {
        if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_ROADING) {

            try {
                if (smoothMarker != null) {
                    xviewModel.smoothRunningLatLng.add(latLng);
                    smoothMarker.setPoints(xviewModel.smoothRunningLatLng);
                    smoothMarker.setTotalDuration(2);
                    smoothMarker.startSmoothMove();
                    xviewModel.smoothRunningLatLng.remove(0);

                } else {
                    getaMap().clear();
                    Marker marker = getaMap().addMarker(new MarkerOptions()
                            .position(latLng)
                            .zIndex(1)
                            .anchor(0.5f, 0.5f)
                            .rotateAngle(360 - bear)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
                    marker.setObject(MarkerObj);
                    marker.showInfoWindow();
                    smoothMarker = new MovingPointOverlay(getaMap(), marker);
                    xviewModel.smoothRunningLatLng.clear();
                    xviewModel.smoothRunningLatLng.add(latLng);
                    if (result != null) {
                        getaMap().addMarker(new MarkerOptions()
                                .position(AMapUtil.convertToLatLng(result.getTargetPos()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_end)));
                    }

                }
            } catch (Exception e) {

            }
        }
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

            }
        }
    }

    protected void removeMapRouteView() {
        if (null != getaMap()) {
            getaMap().clear();
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
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_TRIP_REFRESH_DATA_CODE));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_HOME_FRAGMENT_DATA_CODE));
    }


}
