package com.ymx.driver.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.trace.LBSTraceClient;
import com.google.gson.Gson;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.config.MqttConfig;
import com.ymx.driver.entity.app.mqtt.GpsInfoEntity;
import com.ymx.driver.mqtt.MQTTService;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.MyLifecycleHandler;
import org.greenrobot.eventbus.EventBus;
import java.util.LinkedList;


/**
 * Created by wuwei
 * 2019/9/25
 * 佛祖保佑       永无BUG
 */
public class LocationManager {
    private static final int locationNumber = 3;
    private static final int locationTimeNumber = 2;
    private static volatile LocationManager sLocationManager;
    private final LocateReceiver mLocateReceiver;
    private Context mContext;

    private double mLatitude;
    private double mLongitude;
    private float mAccuracy;
    private AMapLocation mAMapLocation;
    private long mLastPublishPositionTime;
    private LBSTraceClient lbsTraceClient = null;
    private LinkedList<AMapLocation> aMapLocationsList;
    private GpsInfoEntity gpsInfoEntity;

    public GpsInfoEntity getGpsInfoEntity() {
        return gpsInfoEntity;
    }

    public void setGpsInfoEntity(GpsInfoEntity gpsInfoEntity) {
        this.gpsInfoEntity = gpsInfoEntity;
    }

    /**
     * * * * * * * * 前台       连续 高精度 5s
     * * * *有人看
     * * * * * * * * 后台/杀死   单次 省电
     * <p>
     * <p>
     * * * * * * * * 前台  连续 高精度 5s
     * * * *没人看
     * * * * * * * * 后台   后台 连续 省电 10m
     */
    private boolean mIsBackground;

    public void setBackground(boolean background) {
        mIsBackground = background;
        startLocationService();
    }

    /**
     * 开始定位服务
     */
    public void startLocationService() {
        boolean onceLocation = false;
        AMapLocationClientOption.AMapLocationMode mode = AMapLocationClientOption.AMapLocationMode.Battery_Saving;
        long interval = 5000;

        if (mIsBackground) {
            onceLocation = false;
            mode = AMapLocationClientOption.AMapLocationMode.Battery_Saving;
            interval = locationTimeNumber * 1000;
        } else {
            onceLocation = false;
            mode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
            interval = locationTimeNumber * 1000;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("onceLocation", onceLocation);
        bundle.putSerializable("mode", mode);
        bundle.putSerializable("interval", interval);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        LocationService.start(mContext, intent);
    }

    private LocationManager(Context context) {
        mContext = context;
        lbsTraceClient = LBSTraceClient.getInstance(context);
        // 定位监听
        mLocateReceiver = new LocateReceiver(context);
        aMapLocationsList = new LinkedList<>();
        mLocateReceiver.startObserve(new LocateReceiver.LocateSuccessListener() {

            @Override
            public void onLocateSuccess(AMapLocation aMapLocation) {


                if (aMapLocation == null || aMapLocation.getLatitude() == 0 ||
                        aMapLocation.getLongitude() == 0 || aMapLocation.getErrorCode() == 12) {
                    return;
                }

                long time = System.currentTimeMillis() / 1000;// 定位完成时间
                // 通知到地图界面
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_LOCATION_SUCCESS, aMapLocation));



                aMapLocationsList.offer(aMapLocation);
                if (aMapLocationsList.size() >= locationNumber) {
                    for (int i = 0; i < locationNumber; i++) {
                        aMapLocationsList.poll();
                    }
                    publishPosition(time, aMapLocation.getLatitude(), aMapLocation.getLongitude(), aMapLocation.getBearing()
                            , aMapLocation.getSpeed() ,aMapLocation.getTime());
                }
                mLatitude = aMapLocation.getLatitude();
                mLongitude = aMapLocation.getLongitude();
                mAccuracy = aMapLocation.getAccuracy();
                mAMapLocation = aMapLocation;


            }
        });

    }

    public static LocationManager getInstance(Context context) {
        if (sLocationManager == null) {
            synchronized (LocationManager.class) {
                if (sLocationManager == null) {
                    sLocationManager = new LocationManager(context);
                }
            }
        }
        return sLocationManager;
    }

    /**
     * 关闭服务
     */
    public void stopLocationService() {
        if (mLocateReceiver != null) {
            mLocateReceiver.endObserver();
        }

        if (lbsTraceClient != null) {
            lbsTraceClient.stopTrace();
        }

    }

    public synchronized void publishPosition(long time, double lat, double lng, float bear, float speed ,long gaodeTime) {
        if (!LoginHelper.isLogin() || lat == 0 || lng == 0) {
            return;
        }

        mLastPublishPositionTime = time;

        GpsInfoEntity gpsInfo = new GpsInfoEntity();
        if (YmxCache.getCarState() == 0 || YmxCache.getCarState() == 1 || YmxCache.getDriverType() == 2 ||YmxCache.getDriverType() == 3||YmxCache.getDriverType() == 4||YmxCache.getDriverType() == 5||YmxCache.getDriverType() == 6) {
            gpsInfo.setSpeed(String.valueOf(speed));
            gpsInfo.setBear(String.valueOf(bear));
            gpsInfo.setLat(String.valueOf(lat));
            gpsInfo.setLng(String.valueOf(lng));
            gpsInfo.setDriverId(YmxCache.getDriverId());
            gpsInfo.setCurrentTime(gaodeTime);

            if (!TextUtils.isEmpty(YmxCache.getOrderId())) {
                gpsInfo.setOrderNo(YmxCache.getOrderId());

            }
            if (!MyLifecycleHandler.isApplicationInForeground()) {

                gpsInfo.setType(String.valueOf(0));
            } else {

                gpsInfo.setType(String.valueOf(1));
            }


            gpsInfo.setStatus(String.valueOf(YmxCache.getServerStatus()));
            LogUtil.d("LocationManager", gpsInfo.toString());

            Gson gson = new Gson();
            MQTTService.publish(gson.toJson(gpsInfo).getBytes(), MqttConfig.MQTT_TOPIC_GPS_UPLOAD);
        }


    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public float getAccuracy() {
        return mAccuracy;
    }

    public AMapLocation getAMapLocation() {
        return mAMapLocation;
    }
}
