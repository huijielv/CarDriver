package com.ymx.driver.ui.navi;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.Observer;

import com.amap.api.location.AMapLocation;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;

import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityNaviBinding;
import com.ymx.driver.entity.app.OrderPriceEntity;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.ui.launch.dialog.PermissionDeniedDialog;
import com.ymx.driver.ui.travel.activity.TravelOrderDetailsActivity;
import com.ymx.driver.util.NavUtil;
import com.ymx.driver.util.SystemUtils;
import com.ymx.driver.util.TTSController;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.util.VoicePlayMannager;
import com.ymx.driver.view.ScrollSwithViewButton;
import com.ymx.driver.viewmodel.navi.NaviViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.ymx.driver.ui.travel.activity.TravelActivity.ORDERI_ID;
import static com.ymx.driver.viewmodel.navi.NaviViewModel.DRIVER_STATE_ROADING;
import static com.ymx.driver.viewmodel.travel.TravelViewModel.DRIVER_STATE_CONFIRM_COST;

public class NaviActivity extends BaseActivity<ActivityNaviBinding, NaviViewModel> implements AMapNaviListener, AMapNaviViewListener {


    public static final String NAVILAT = "Lat";
    public static final String NAVILON = "Lon";
    private String orderNo;
    private int actionType;
    private DefaultStyleDialog gpsDialog;
    private PermissionDeniedDialog permissionDeniedDialog;


    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, NaviActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    protected AMapNaviView mAMapNaviView;
    protected AMapNavi mAMapNavi;
    protected TTSController mTtsManager;
    protected NaviLatLng mStartLatlng;
    protected NaviLatLng mEndLatlng;

    protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
    protected List<NaviLatLng> mWayPointList = new ArrayList<NaviLatLng>();

    @Override
    public int initLayoutId(Bundle savedInstanceState) {

        return R.layout.activity_navi;
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
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
//        AMapNaviViewOptions options = new AMapNaviViewOptions();
//        options.setCameraBubbleShow(true);
//        mAMapNaviView.setViewOptions(options);
        mTtsManager = TTSController.getInstance(YmxApp.getInstance());

        binding.scrollSwithViewButton.setOnSrollSwichListener(new ScrollSwithViewButton.OnSrollSwichListener() {
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
                    if (viewModel.orderStatus != null && viewModel.orderStatus.get() != null) {
                        int action = viewModel.orderStatus.get();

                        if (action == 2 || action == 3) {
                            viewModel.orderStartLocation(orderNo, action, "");
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
                                            viewModel.orderStartLocation(orderNo, action, "");

                                        }
                                    }).show();

                        } else {
                            viewModel.updateOrderDetailsStatus(orderNo, currentCoord, String.valueOf(action), "");
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
    public void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(ORDERI_ID)) {
            orderNo = intent.getStringExtra(ORDERI_ID);
            viewModel.orderId.set(orderNo);
            viewModel.recoverOrderDetails(orderNo);
        }

        requestPermission(PermissionConfig.PC_SPLASH, true,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                SystemUtils.checkedAndroid_Q() ? Manifest.permission.ACCESS_BACKGROUND_LOCATION : Manifest.permission.ACCESS_FINE_LOCATION
                );
        Double endlat = getIntent().getDoubleExtra(NAVILAT, 0);
        Double endlon = getIntent().getDoubleExtra(NAVILON, 0);


        AMapLocation aMapLocation = LocationManager.getInstance(YmxApp.getInstance()).getAMapLocation();
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        if (aMapLocation != null) {
            mStartLatlng = new NaviLatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            mEndLatlng = new NaviLatLng(endlat, endlon);

            mAMapNavi.addAMapNaviListener(this);
            sList.add(mStartLatlng);
            eList.add(mEndLatlng);
        }


    }

    @Override
    public void showPermissionDialog(int requestCode) {
        super.showPermissionDialog(requestCode);

        dismissDialog();
        permissionDeniedDialog = new PermissionDeniedDialog(activity)
                .setOnDialogListener(new PermissionDeniedDialog.Listener() {
                    @Override
                    public void negative(Dialog dialog) {
                        dialog.dismiss();
                        permissionGranted(requestCode);
                    }

                    @Override
                    public void positive(Dialog dialog) {
                        dialog.dismiss();

                        startAppSettings();
                    }
                });
        permissionDeniedDialog.show();
    }

    private void dismissDialog() {
        if (!isFinishing()) {
            if (permissionDeniedDialog != null) {
                permissionDeniedDialog.dismiss();
            }
        }
    }




    @Override
    public void initViewObservable() {


        viewModel.uc.ucOrderPrice.observe(this, new Observer<OrderPriceEntity>() {
            @Override
            public void onChanged(OrderPriceEntity orderPriceEntity) {
                if (orderPriceEntity != null) {

                    if (orderNo.equals(orderPriceEntity.getOrderNo()) && !TextUtils.isEmpty(orderPriceEntity.getAmount())) {
                        binding.scrollSwithViewButton.setOrderPrice(orderPriceEntity.getAmount() + "元");
                    }

                }
            }
        });


        viewModel.uc.ucUpdateOrderStatus.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                actionType = viewModel.orderStatus.get();
                orderNo = viewModel.orderId.get();
                if (actionType == DRIVER_STATE_ROADING) {
                    AMapLocation aMapLocation = LocationManager.getInstance(YmxApp.getInstance()).getAMapLocation();
                    mStartLatlng = new NaviLatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    mEndLatlng = new NaviLatLng(viewModel.lat.get(), viewModel.lng.get());
                    sList.clear();
                    eList.clear();
                    sList.add(mStartLatlng);
                    eList.add(mEndLatlng);
                    mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, 0);
                }

                if (actionType == DRIVER_STATE_CONFIRM_COST) {
                    Intent intent = new Intent();
                    intent.putExtra(TravelOrderDetailsActivity.ORDERI_ID, orderNo);
                    TravelOrderDetailsActivity.start(activity, intent);
                    doSthIsExit();
                    VoicePlayMannager.getInstance(getApplication()).play(R.raw.reach_destination);
                }
                binding.scrollSwithViewButton.setText(viewModel.updateOrderContent.get());
            }
        });

        viewModel.uc.ucRecoverOrderDetails.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                binding.scrollSwithViewButtonRL.setVisibility(View.VISIBLE);
                actionType = viewModel.orderStatus.get();
                orderNo = viewModel.orderId.get();
                binding.scrollSwithViewButton.setText(viewModel.updateOrderContent.get());

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTtsManager.stopSpeaking();
        mAMapNaviView.onDestroy();
        if (mAMapNavi != null) {
            mAMapNavi.stopNavi();
            mAMapNavi.destroy();
        }

        if (gpsDialog != null && gpsDialog.isShowing()) {
            gpsDialog.dismiss();
        }


    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {
        //初始化成功
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
//            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
    }

    @Override
    public void onStartNavi(int type) {
        //开始导航回调
    }

    @Override
    public void onTrafficStatusUpdate() {
        //
    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {
        //当前位置回调
    }

    @Override
    public void onGetNavigationText(int type, String text) {
        //播报类型和播报文字回调

    }

    @Override
    public void onGetNavigationText(String s) {
        if (mTtsManager != null)
            mTtsManager.onGetText(s);
    }

    @Override
    public void onEndEmulatorNavi() {
        //结束模拟导航
    }

    @Override
    public void onArriveDestination() {
        //到达目的地
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {


    }

    @Override
    public void onReCalculateRouteForYaw() {
        //偏航后重新计算路线回调
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        //拥堵后重新计算路线回调
    }

    @Override
    public void onArrivedWayPoint(int wayID) {
        //到达途径点
    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
        //GPS开关状态回调
    }

    @Override
    public void onNaviSetting() {
        //底部导航设置点击回调
    }

    @Override
    public void onNaviMapMode(int naviMode) {
        //导航态车头模式，0:车头朝上状态；1:正北朝上模式。
    }

    @Override
    public void onNaviCancel() {
        finish();
    }


    @Override
    public void onNaviTurnClick() {
        //转弯view的点击回调
    }

    @Override
    public void onNextRoadClick() {
        //下一个道路View点击回调
    }


    @Override
    public void onScanViewButtonClick() {
        //全览按钮点击回调
    }

    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
        //过时
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] amapServiceAreaInfos) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
        //导航过程中的信息更新，请看NaviInfo的具体说明

//        LogUtil.d("TEST1------", String.valueOf(naviinfo.getPathRetainDistance()) + "时间----" + String.valueOf(AMapUtil.getFriendlyTime(naviinfo.getPathRetainTime())));

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        //已过时
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        //已过时
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        //显示转弯回调
    }

    @Override
    public void hideCross() {
        //隐藏转弯回调
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {
        //显示车道信息

    }

    @Override
    public void hideLaneInfo() {
        //隐藏车道信息
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        //多路径算路成功回调

        mAMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        //更新交通设施信息
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        //更新巡航模式的统计信息
    }


    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        //更新巡航模式的拥堵信息
    }

    @Override
    public void onPlayRing(int i) {

    }


    @Override
    public void onLockMap(boolean isLock) {
        //锁地图状态发生变化时回调
    }

    @Override
    public void onNaviViewLoaded() {


    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }


    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }
}
