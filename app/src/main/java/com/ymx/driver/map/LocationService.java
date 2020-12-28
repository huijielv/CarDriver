package com.ymx.driver.map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.entity.app.UpdateGpsEntity;
import com.ymx.driver.entity.app.mqtt.GpsInfoEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.MyLifecycleHandler;
import com.ymx.driver.util.NotificationUtil;
import io.reactivex.schedulers.Schedulers;

public class LocationService extends Service {
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private static final String TAG = "LocationService";



    private boolean mOnceLocation;
    private AMapLocationClientOption.AMapLocationMode mMode;
    private long mInterval;
    private static long autoUpdateTime = 1 * 60 * 1000;
    private Handler handler = new Handler();


    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {

        try {
            Intent intent = new Intent(context, LocationService.class);
            if (extras != null) {
                intent.putExtras(extras);
            }
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        startForeground(110, NotificationUtil.buildNotification(this, NotificationUtil.CHANNELID
                , NotificationUtil.CHANNELNAME));

        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            mOnceLocation = bundle.getBoolean("onceLocation", false);
            mMode = (AMapLocationClientOption.AMapLocationMode) bundle.getSerializable("mode");
            mInterval = bundle.getLong("interval", 5000);
        }

        startLocation();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 启动定位
     */
    void startLocation() {
        stopLocation();

        if (null == mLocationClient) {
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
        }

        mLocationOption = new AMapLocationClientOption();

        mLocationOption.setLocationMode(mMode);
        mLocationOption.setOnceLocation(mOnceLocation);
        mLocationOption.setInterval(mInterval);
        mLocationOption.setLocationCacheEnable(false);
        mLocationOption.setNeedAddress(true);

        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(locationListener);
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    void stopLocation() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
        }
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            LogUtil.i("LocationManager", "onLocatsasaeSuccess-000: 当前定位模式" + mMode + "  " + mOnceLocation + "  " + mInterval + "  " + aMapLocation.getProvider());
            //发送结果的通知
            if (aMapLocation == null) {
                aMapLocation = mLocationClient.getLastKnownLocation();
            }
            sendBroadcast(new Intent("LocationReceiver").putExtra("AMapLocation", aMapLocation));
        }
    };


    public void updateGps(GpsInfoEntity gpsInfoEntity) {
        RetrofitFactory.sApiService.updateGps(gpsInfoEntity)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new TObserver<UpdateGpsEntity>() {
                    @Override
                    protected void onRequestStart() {

                    }

                    @Override
                    protected void onRequestEnd() {

                    }

                    @Override
                    protected void onSuccees(UpdateGpsEntity userEntity) {

                        LogUtil.d("LocationService", "onSuccees");
                        if (userEntity != null && userEntity.getIsRemoveOrderNo() == 1) {
                            if (!TextUtils.isEmpty(YmxCache.getOrderId()) && YmxCache.getOrderId().equals(userEntity.getOrderNo())) {
                                LogUtil.d("LocationService", "LocationService");
                                YmxCache.setOrderId("");
                            }
                        }

                    }

                    @Override
                    protected void onFailure(String message) {

                    }
                });
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (LoginHelper.isLogin()) {
                LogUtil.d(TAG, "Location");
                AMapLocation aMapLocation = LocationManager.getInstance(getApplicationContext()).getAMapLocation();
                if (aMapLocation != null) {
                    GpsInfoEntity gpsInfo = new GpsInfoEntity();
                    gpsInfo.setSpeed(String.valueOf(aMapLocation.getSpeed()));
                    gpsInfo.setBear(String.valueOf(aMapLocation.getBearing()));
                    gpsInfo.setLat(String.valueOf(aMapLocation.getLatitude()));
                    gpsInfo.setLng(String.valueOf(aMapLocation.getLongitude()));
                    gpsInfo.setCurrentTime(aMapLocation.getTime());
                    gpsInfo.setDriverId(YmxCache.getDriverId());
                    if (!TextUtils.isEmpty(YmxCache.getOrderId())) {
                        gpsInfo.setOrderNo(YmxCache.getOrderId());

                    }
                    if (!MyLifecycleHandler.isApplicationInForeground()) {

                        gpsInfo.setType(String.valueOf(0));
                    } else {

                        gpsInfo.setType(String.valueOf(1));
                    }

                    gpsInfo.setStatus(String.valueOf(YmxCache.getServerStatus()));

                    updateGps(gpsInfo);
                }

            }

            handler.postDelayed(this, autoUpdateTime);


        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        if (handler != null) {
            handler.postDelayed(runnable, autoUpdateTime);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
