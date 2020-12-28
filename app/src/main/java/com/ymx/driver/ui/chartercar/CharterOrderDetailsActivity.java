package com.ymx.driver.ui.chartercar;

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
import android.view.View;

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
import com.ymx.driver.R;

import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseMapActivity;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityCharterOrderDetailsBinding;
import com.ymx.driver.entity.app.CharterCancalOrder;
import com.ymx.driver.entity.app.CharterOrderDetailsEntity;
import com.ymx.driver.entity.app.GetCharterSystemTime;
import com.ymx.driver.map.AMapUtil;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.map.overlay.DrivingRouteOverlay;
import com.ymx.driver.tts.BaiduSpeech;
import com.ymx.driver.ui.chartercar.frament.ChatterDrivingTimePickDialogFragment;
import com.ymx.driver.ui.longrange.driving.RangeDrivingPayMoneyQrcodeFrament;
import com.ymx.driver.ui.longrange.driving.frament.LongRangeCirculationWaitingFrament;
import com.ymx.driver.ui.main.activity.MainActivity;
import com.ymx.driver.ui.mine.Frament.PayDialogFragment;
import com.ymx.driver.ui.travel.activity.TravelActivity;
import com.ymx.driver.util.NavUtil;
import com.ymx.driver.util.NavigationMapUtils;
import com.ymx.driver.util.VoicePlayMannager;
import com.ymx.driver.view.SwipeButton;
import com.ymx.driver.viewmodel.chartercar.ChartercarOrderDetailsViewModel;
import com.ymx.driver.viewmodel.mine.NavSettingViewModel;
import com.ymx.driver.viewmodel.travel.TravelViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;


import static com.amap.api.services.route.RouteSearch.DRIVING_SINGLE_DEFAULT;

public class CharterOrderDetailsActivity extends BaseMapActivity<ActivityCharterOrderDetailsBinding, ChartercarOrderDetailsViewModel> {

    public static final String ORDERI_ID = "orderNo";
    private LatLonPoint mStartPoint; //起点
    private LatLonPoint mEndPoint;//终点
    private String orderNo;
    private MovingPointOverlay smoothMarker;
    private static final String MarkerObj = "driver_car";
    private float bear;
    private DefaultStyleDialog defaultStyleDialog;

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, CharterOrderDetailsActivity.class);
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
        moveIngCar(aMapLocation);


    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_charter_order_details;
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

                if (!NavUtil.isGpsOpen(getApplicationContext())) {
                    defaultStyleDialog = new DefaultStyleDialog(activity)

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
                    defaultStyleDialog.show();
                } else {
                    if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == 2) {
                        xviewModel.CharterOrderStartLocation(xviewModel.orderId.get(), 1, "");
                    } else if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == 4) {

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
                                        dialog.dismiss();
                                        if (xviewModel.isLastDay.get() == 0) {
                                            xviewModel.CharterOrderStartLocation(xviewModel.orderId.get(), 4, "");
                                        } else {
                                            xviewModel.getCharterSystemTime(xviewModel.orderId.get());

                                        }
                                    }
                                }).show();


                    }
                }


            }
        });
    }

    @Override
    protected TextureMapView getTextureMapView() {
        return xbinding.mapView;
    }

    @Override
    protected void initMapLoad() {
        if (LocationManager.getInstance(activity).getLatitude() != 0
                && LocationManager.getInstance(activity).getLongitude() != 0) {

            moveToCamera(new LatLng(LocationManager.getInstance(activity).getLatitude(),
                    LocationManager.getInstance(activity).getLongitude()));


        }

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(ORDERI_ID)) {
            orderNo = intent.getStringExtra(ORDERI_ID);
            xviewModel.orderId.set(orderNo);
            xviewModel.recoverCharteredOrderDetails(orderNo);
        }
    }

    @Override
    public void initViewObservable() {
        xviewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        xviewModel.uc.ucRecoverOrderDetails.observe(this, new Observer<CharterOrderDetailsEntity>() {
            @Override
            public void onChanged(CharterOrderDetailsEntity charterOrderDetailsEntit) {


                xbinding.mapView.setVisibility(View.VISIBLE);
                xbinding.btnSubmit.setVisibility(View.VISIBLE);
                if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_TO_PASSENGERS) {

                    clearMap();
                    searchRouteResult();
                    mStartPoint = new LatLonPoint(LocationManager.getInstance(activity).getLatitude(), LocationManager.getInstance(activity).getLongitude());
                    mEndPoint = new LatLonPoint(xviewModel.lat.get(), xviewModel.lng.get());
                    searchRouteResult();

                }

                initSubmitBtn(xviewModel.orderStatus.get());
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
                if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == 2) {
                    navigation(xviewModel.lat.get(), xviewModel.lng.get(), "");
                } else if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == 4) {
                    navigation(0.0, 0.0, "");
                }

            }
        });

        xviewModel.uc.ucQrcode.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                VoicePlayMannager.getInstance(getApplicationContext()).play(R.raw.long_drvingqcode);
                Bundle bundle = new Bundle();
                bundle.putString(RangeDrivingPayMoneyQrcodeFrament.ORDER_ID,
                        xviewModel.orderId.get());
                RangeDrivingPayMoneyQrcodeFrament.newInstance(bundle).show(getSupportFragmentManager(),
                        RangeDrivingPayMoneyQrcodeFrament.class.getSimpleName());

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

        xviewModel.uc.ucPaySuccess.observe(this, new Observer<Void>() {

            @Override
            public void onChanged(Void aVoid) {

                xviewModel.payState.set(1);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_QUERY_PAY_RESULT));
            }

        });

        xviewModel.uc.ucUpdateOrderStatus.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                initSubmitBtn(xviewModel.orderStatus.get());
                if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == TravelViewModel.DRIVER_ORDER_FINISH) {

                    YmxCache.setOrderId("");
                    doSthIsExit();
                    if (xviewModel.orderType.get() == 2 && xviewModel.isLastDay.get() == 1) {
                        BaiduSpeech.getInstance(YmxApp.getInstance()).playText(getString(R.string.driver_charter_end_service_last_day_voice),null);

                    } else {
                        BaiduSpeech.getInstance(YmxApp.getInstance()).playText(getString(R.string.driver_charter_end_service_voice),null);

                    }
                    MainActivity.start(activity);

                } else if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_ROADING) {

//
                } else if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == 5) {

                    BaiduSpeech.getInstance(YmxApp.getInstance()).playText(getString(R.string.driver_charter_wait_voice),null);
                    Intent intent = new Intent();
                    intent.putExtra(TravelActivity.ORDERI_ID, xviewModel.orderId.get());
                    CharterOrderWaitingActivity.start(activity, intent);
                    doSthIsExit();

                }

            }
        });

        xviewModel.uc.ucCharterWait.observe(this, new Observer<Void>() {

            @Override
            public void onChanged(Void aVoid) {
                new DefaultStyleDialog(activity)
                        .setBody("是否确认行程等待?")
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

                                xviewModel.CharterOrderStartLocation(xviewModel.orderId.get(), 2, "");

                            }
                        }).show();
            }
        });


        xviewModel.uc.ucCancelOrder.observe(this, new Observer<CharterCancalOrder>() {

            @Override
            public void onChanged(CharterCancalOrder charterCancalOrder) {
                new DefaultStyleDialog(activity)
                        .setBody(charterCancalOrder.getTips())
                        .setPositiveText("确定")
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
        xviewModel.uc.ucCharterSystemTime.observe(this, new Observer<GetCharterSystemTime>() {

            @Override
            public void onChanged(GetCharterSystemTime getCharterSystemTime) {
                if (!isFinishing() && getCharterSystemTime != null && !TextUtils.isEmpty(getCharterSystemTime.getNextStartTime())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ChatterDrivingTimePickDialogFragment.TIME, getCharterSystemTime.getNextStartTime());
                    bundle.putInt(ChatterDrivingTimePickDialogFragment.NEXT_DAY_TYPE, getCharterSystemTime.getNextDay());
                    ChatterDrivingTimePickDialogFragment.newInstance(bundle).show(getSupportFragmentManager(),
                            ChatterDrivingTimePickDialogFragment.class.getSimpleName());
                } else {

                }
            }
        });
    }

    public void initSubmitBtn(int status) {
        if (status == TravelViewModel.DRIVER_STATE_TO_PASSENGERS) {
            xbinding.btnSubmit.setText("行程开始");
        } else if (status == TravelViewModel.DRIVER_STATE_ROADING) {
            xbinding.btnSubmit.setText("行程结束");
        } else if (status == TravelViewModel.DRIVER_ORDER_FINISH) {
            xbinding.btnSubmit.setText("完成");
        }
    }

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


                            getaMap().addMarker(new MarkerOptions()
                                    .position(AMapUtil.convertToLatLng(result.getStartPos()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_from)));
                            getaMap().addMarker(new MarkerOptions()
                                    .position(AMapUtil.convertToLatLng(result.getTargetPos()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_end)));


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

    public void clearMap() {
        if (smoothMarker != null) {
            smoothMarker.setMoveListener(null);
            smoothMarker.destroy();
            smoothMarker = null;
        }
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

    public void moveIngCar(AMapLocation aMapLocation) {
        if (xviewModel.orderStatus != null && xviewModel.orderStatus.get() != null && xviewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_ROADING) {

            try {
                if (smoothMarker != null) {
                    xviewModel.smoothRunningLatLng.add(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    smoothMarker.setPoints(xviewModel.smoothRunningLatLng);
                    smoothMarker.setTotalDuration(2);
                    smoothMarker.startSmoothMove();
                    xviewModel.smoothRunningLatLng.remove(0);
                    getaMap().animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 16));

                } else {
                    getaMap().clear();
                    Marker marker = getaMap().addMarker(new MarkerOptions()
                            .position(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()))
                            .zIndex(1)
                            .anchor(0.5f, 0.5f)
                            .rotateAngle(360 - bear)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
                    marker.setObject(MarkerObj);
                    marker.showInfoWindow();
                    smoothMarker = new MovingPointOverlay(getaMap(), marker);
                    xviewModel.smoothRunningLatLng.clear();
                    xviewModel.smoothRunningLatLng.add(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    getaMap().animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 16));

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
        if (defaultStyleDialog != null && defaultStyleDialog.isShowing()) {
            defaultStyleDialog.dismiss();
        }
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_TRIP_REFRESH_DATA_CODE));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_HOME_FRAGMENT_DATA_CODE));
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);
        overridePendingTransition(R.anim.slide_enter_right, R.anim.slide_exit_left);
    }


}
