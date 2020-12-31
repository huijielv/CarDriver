package com.ymx.driver.ui.travel.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.lifecycle.Observer;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
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
import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseMapActivity;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityTravelBinding;
import com.ymx.driver.entity.app.CancelOrderEntity;
import com.ymx.driver.entity.app.UpdateStartAddressEntity;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.map.AMapUtil;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.map.overlay.DrivingRouteOverlay;
import com.ymx.driver.tts.BaiduSpeech;
import com.ymx.driver.ui.main.activity.MainActivity;
import com.ymx.driver.ui.navi.NaviActivity;
import com.ymx.driver.util.NavUtil;
import com.ymx.driver.util.NavigationMapUtils;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.util.VoicePlayMannager;
import com.ymx.driver.view.ScrollSwithViewButton;
import com.ymx.driver.viewmodel.mine.NavSettingViewModel;
import com.ymx.driver.viewmodel.travel.TravelViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import static com.amap.api.services.route.RouteSearch.DRIVING_SINGLE_DEFAULT;
import static com.ymx.driver.viewmodel.travel.TravelViewModel.DRIVER_STATE_CONFIRM_COST;
import static com.ymx.driver.viewmodel.travel.TravelViewModel.DRIVER_STATE_READY_TO_GO;

/**
 * Created by xuweihua
 * 2020/5/14
 */
public class TravelActivity extends BaseMapActivity<ActivityTravelBinding, TravelViewModel> {
    public static final String ORDERI_ID = "orderNo";
    private PassengerInfoEntity passengerInfoEntity;
    private LatLonPoint mStartPoint; //起点
    private LatLonPoint mEndPoint;//终点
    private int actionType;
    private String orderNo;
    private MovingPointOverlay smoothMarker;
    private static final String MarkerObj = "driver_car";
    private float bear;
    private int locateSuccess = 0;
    private int ocateSuccessNumber = 10;
    //距离状态
    private int distanceStatus = 0;
    private DefaultStyleDialog gpsDialog;


    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, TravelActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_travel;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();


        if (intent.hasExtra("passerInfo")) {
            passengerInfoEntity = (PassengerInfoEntity) intent.getSerializableExtra("passerInfo");
            actionType = passengerInfoEntity.getDriverState();

            orderNo = passengerInfoEntity.getOrderNo();
            xviewModel.orderStatus.set(passengerInfoEntity.getDriverState());
            xviewModel.mode.set(passengerInfoEntity.getDriverState());
            xviewModel.orderId.set(orderNo);
            xviewModel.startName.set(passengerInfoEntity.getSrcName());
            xviewModel.endName.set(passengerInfoEntity.getDesName());
            xviewModel.phoneNum.set(passengerInfoEntity.getPhone());

            xviewModel.lat.set(passengerInfoEntity.getLat());
            xviewModel.channel.set(passengerInfoEntity.getChannel());
            xviewModel.lng.set(passengerInfoEntity.getLng());
            xviewModel.businessType.set(passengerInfoEntity.getBusinessType());
            mStartPoint = new LatLonPoint(LocationManager.getInstance(activity).getLatitude(), LocationManager.getInstance(activity).getLongitude());
            mEndPoint = new LatLonPoint(xviewModel.lat.get(), xviewModel.lng.get());

            if (!TextUtils.isEmpty(passengerInfoEntity.getPhone())) {
                StringBuffer sb = new StringBuffer();
                sb.append("尾号  ");
                sb.append(passengerInfoEntity.getPhone().substring(7));
                xviewModel.passengerInfo.set(sb.toString());
            }
            xviewModel.isLoad.set(true);
            xviewModel.initRightTitleShow(actionType);
            xviewModel.initRightTitleStatus(actionType, passengerInfoEntity.getBusinessType(), 0);
            getaMap().clear();
            searchRouteResult();


        } else if (intent.hasExtra(ORDERI_ID)) {
            orderNo = intent.getStringExtra(ORDERI_ID);
            xviewModel.orderId.set(orderNo);
            xviewModel.recoverOrderDetails(orderNo);
        }

        //终点
        if (passengerInfoEntity != null) {
            xbinding.scrollSwithViewButton.setText(passengerInfoEntity.getButtonText());
        }


    }

    @Override
    protected void locateSuccess(AMapLocation aMapLocation) {
        if (aMapLocation == null || aMapLocation.getLatitude() == 0 || aMapLocation.getLongitude() == 0) {
            return;
        }
        bear = aMapLocation.getBearing();


        if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_ROADING) {
            clearMap();
            if (xviewModel.businessType != null && xviewModel.businessType.get() != null && xviewModel.businessType.get() == 10) {

                moveIngCar(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), null);
            } else if (xviewModel.businessType != null && xviewModel.businessType.get() != null && xviewModel.driverType != null && xviewModel.driverType.get() != null && xviewModel.businessType.get() == 5 && xviewModel.driverType.get() == 6) {

                moveIngCar(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), null);
            } else {
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


    }


    @Override
    protected TextureMapView getTextureMapView() {
        return xbinding.mapView;
    }

    public void initMap() {
        if (LocationManager.getInstance(activity).getLatitude() != 0
                && LocationManager.getInstance(activity).getLongitude() != 0) {
            moveToCamera(new LatLng(LocationManager.getInstance(activity).getLatitude(),
                    LocationManager.getInstance(activity).getLongitude()));


        }
    }

    @Override
    protected void initMapLoad() {
        initMap();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        xbinding.scrollSwithViewButton.setOnSrollSwichListener(new ScrollSwithViewButton.OnSrollSwichListener() {
            @Override
            public void onSrollingMoreThanCritical() {

            }

            @Override
            public void onSrollingLessThanCriticalX() {

            }

            @Override
            public void onSlideFinishSuccess() {

                String currentCoord = "";

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
                    if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null) {
                        int action = xviewModel.orderStatus.get();
                        if (action == 2 || action == 3) {
                            xviewModel.orderStartLocation(orderNo, action, "");
                        } else if (action == 4) {
                            new DefaultStyleDialog(activity)
                                    .setBody("是否确认行程结束？")
                                    .setNegativeText("取消")
                                    .setPositiveText("确认")
                                    .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                                        @Override
                                        public void negative(Dialog dialog) {
                                            dialog.dismiss();

                                        }

                                        @Override
                                        public void positive(Dialog dialog) {
                                            dialog.dismiss();
                                            xviewModel.orderStartLocation(orderNo, action, "");

                                        }
                                    }).show();

                        } else {
                            xviewModel.updateOrderDetailsStatus(orderNo, currentCoord, String.valueOf(action), "");
                        }
                    } else {
                        UIUtils.showToast("网络信号差,请稍后再试");
                    }
                }

            }

            @Override
            public void onSlideFinishCancel() {

            }
        });

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

                if (xviewModel.lng != null && xviewModel.lng.get() != null) {
                    String addressName;
                    if (xviewModel.orderStatus != null && (xviewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_ROADING)) {
                        addressName = xviewModel.endName.get();
                    } else {
                        addressName = xviewModel.startName.get();
                    }


                    if (YmxCache.getNavStatus() == 0) {
                        if (YmxCache.getNavMode() == NavSettingViewModel.NAV_MODE_GAODE) {
                            NavigationMapUtils.goGaodeMap(getApplicationContext(), xviewModel.lat.get(), xviewModel.lng.get(), addressName);
                        } else if (YmxCache.getNavMode() == NavSettingViewModel.NAV_MODE_BAIDU) {
                            NavUtil.openBaiDuNavi(getApplicationContext(), "", LocationManager.getInstance(getApplicationContext()).getLatitude(), LocationManager.getInstance(getApplicationContext()).getLongitude(), addressName, xviewModel.lat.get(), xviewModel.lng.get());
                        } else if (YmxCache.getNavMode() == NavSettingViewModel.NAV_MODE_TENCENT) {
                            NavUtil.openTencentMap(getApplicationContext(), "", LocationManager.getInstance(getApplicationContext()).getLatitude(), LocationManager.getInstance(getApplicationContext()).getLongitude(), addressName, xviewModel.lat.get(), xviewModel.lng.get());
                        }
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(NaviActivity.NAVILAT, xviewModel.lat.get());
                        intent.putExtra(NaviActivity.NAVILON, xviewModel.lng.get());
                        intent.putExtra(TravelActivity.ORDERI_ID, orderNo);
                        NaviActivity.start(activity, intent);
//                        NavUtil.neizhiGaodeNavi(getApplicationContext(), LocationManager.getInstance(getApplicationContext()).getLatitude(), LocationManager.getInstance(getApplicationContext()).getLongitude(), xviewModel.lat.get(), xviewModel.lng.get(), addressName);
                    }

                }

            }
        });

        xviewModel.uc.ucCancelOrder.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                showDriverCancelOrderDialog();
            }
        });

        xviewModel.uc.ucUpdateOrderStatus.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                actionType = xviewModel.orderStatus.get();
                orderNo = xviewModel.orderId.get();
                if (actionType == DRIVER_STATE_CONFIRM_COST) {
                    Intent intent = new Intent();
                    intent.putExtra(TravelOrderDetailsActivity.ORDERI_ID, orderNo);
                    TravelOrderDetailsActivity.start(activity, intent);
                    doSthIsExit();
                    VoicePlayMannager.getInstance(getApplication()).play(R.raw.reach_destination);
                }
                xbinding.scrollSwithViewButton.setText(xviewModel.updateOrderContent.get());
            }
        });

        xviewModel.uc.ucRecoverOrderDetails.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                try {
                    mStartPoint = new LatLonPoint(LocationManager.getInstance(activity).getLatitude(), LocationManager.getInstance(activity).getLongitude());
                    mEndPoint = new LatLonPoint(xviewModel.lat.get(), xviewModel.lng.get());
                    actionType = xviewModel.orderStatus.get();
                    orderNo = xviewModel.orderId.get();
                    //出租车逻辑
                    if (actionType == 4 && xviewModel.businessType.get() == 10) {
                        moveIngCar(new LatLng(LocationManager.getInstance(activity).getLatitude(), LocationManager.getInstance(activity).getLongitude()), null);
                        //出租车好友逻辑
                    } else if (actionType == 4 && xviewModel.businessType.get() == 5 && xviewModel.driverType.get() == 6) {
                        moveIngCar(new LatLng(LocationManager.getInstance(activity).getLatitude(), LocationManager.getInstance(activity).getLongitude()), null);
                    } else {
                        //网约车逻辑
                        getaMap().clear();
                        searchRouteResult();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                xbinding.scrollSwithViewButton.setText(xviewModel.updateOrderContent.get());

            }
        });


        xviewModel.uc.ucChangeMap.observe(this, new Observer<Void>() {

            @Override
            public void onChanged(Void aVoid) {
                searchRouteResult();
            }
        });

        xviewModel.uc.ucTransferOrder.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                new DefaultStyleDialog(activity)
                        .setBody(getString(R.string.transfer_order_hiht))
                        .setNegativeText("是，确定转单")
                        .setPositiveText("否，继续行程")
                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                            @Override
                            public void negative(Dialog dialog) {
                                dialog.dismiss();
                                xviewModel.transferOrder(xviewModel.orderId.get());
                            }

                            @Override
                            public void positive(Dialog dialog) {
                                dialog.dismiss();


                            }
                        }).show();
            }
        });


        xviewModel.uc.ucTransferOrderFails.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String info) {
                new DefaultStyleDialog(activity)
                        .setBody(info)
                        .setPositiveText("知道了")
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


        xviewModel.uc.ucCancelOrderSuccess.observe(this, new Observer<CancelOrderEntity>() {
            @Override
            public void onChanged(CancelOrderEntity cancelOrderEntity) {

                new DefaultStyleDialog(activity)
                        .setBody(cancelOrderEntity.isIntegralCompensate() ? cancelOrderEntity.getTips() : cancelOrderEntity.getMessage())
                        .setPositiveText("确定")
                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                            @Override
                            public void negative(Dialog dialog) {
                                dialog.dismiss();

                            }

                            @Override
                            public void positive(Dialog dialog) {
                                if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == DRIVER_STATE_READY_TO_GO) {
                                    VoicePlayMannager.getInstance(getApplicationContext()).play(R.raw.driver_jifen);
                                }

                                dialog.dismiss();
                                doSthIsExit();

                            }
                        }).show();
            }
        });
        xviewModel.uc.ucFailsMsg.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                new DefaultStyleDialog(activity)
                        .setBody(s)
                        .setPositiveText(getString(R.string.driver_cancel_order_confirm))
                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                            @Override
                            public void negative(Dialog dialog) {
                                dialog.dismiss();

                            }

                            @Override
                            public void positive(Dialog dialog) {
                                dialog.dismiss();
                                MainActivity.start(activity);

                            }
                        }).show();
            }
        });

        xviewModel.uc.ucUpdateAddress.observe(this, new Observer<UpdateStartAddressEntity>() {
            @Override
            public void onChanged(UpdateStartAddressEntity updateStartAddressEntity) {


                new DefaultStyleDialog(activity)
                        .setBody(updateStartAddressEntity.getTips())
                        .setNegativeText("拒绝")
                        .setPositiveText("同意")
                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                            @Override
                            public void negative(Dialog dialog) {
                                dialog.dismiss();
                                xviewModel.updateStartAddress(updateStartAddressEntity.getOrderNo(),2);
                            }

                            @Override
                            public void positive(Dialog dialog) {
                                dialog.dismiss();
                                xviewModel.updateStartAddress(updateStartAddressEntity.getOrderNo(),1);
                                BaiduSpeech.getInstance(YmxApp.getInstance()).playText(updateStartAddressEntity.getVoiceText(),null);
                            }
                        }).show();
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

            }
        }
    }


    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);
        overridePendingTransition(R.anim.slide_enter_right, R.anim.slide_exit_left);
    }

    private void showDriverCancelOrderDialog() {
        new DefaultStyleDialog(activity)
                .setBody("是否取消订单？")
                .setNegativeText(getString(R.string.driver_cancel_order_cancel))
                .setPositiveText(getString(R.string.driver_cancel_order_confirm))
                .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                    @Override
                    public void negative(Dialog dialog) {
                        dialog.dismiss();

                    }

                    @Override
                    public void positive(Dialog dialog) {
                        dialog.dismiss();
                        xviewModel.cancelOrder(xviewModel.orderId.get());
                    }
                }).show();
    }


    /**
     * 开始搜索路径规划方案
     */
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
                            sb.append("    ");
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
                                    200, xbinding.scrollSwithViewButton.getHeight() + 400,
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


    public void moveIngCar(LatLng latLng, DriveRouteResult result) {
        if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_ROADING) {

            try {
                if (smoothMarker != null) {
                    xviewModel.smoothRunningLatLng.add(latLng);
                    smoothMarker.setPoints(xviewModel.smoothRunningLatLng);
                    smoothMarker.setTotalDuration(2);
                    smoothMarker.startSmoothMove();
                    xviewModel.smoothRunningLatLng.remove(0);

                    if (xviewModel.businessType != null && xviewModel.businessType.get() != null) {
                        if (xviewModel.businessType.get() == 10) {
                            getaMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        } else if (xviewModel.driverType != null && xviewModel.driverType.get() != null && xviewModel.businessType.get() == 5 && xviewModel.driverType.get() == 6) {
                            getaMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        }
                    }

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
                    if (xviewModel.businessType != null && xviewModel.businessType.get() != null) {
                        if (xviewModel.businessType.get() == 10) {
                            getaMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        } else if (xviewModel.driverType != null && xviewModel.driverType.get() != null && xviewModel.businessType.get() == 5 && xviewModel.driverType.get() == 6) {
                            getaMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        }
                    }


                }
            } catch (Exception e) {

            }
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
    protected boolean isExit() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}
