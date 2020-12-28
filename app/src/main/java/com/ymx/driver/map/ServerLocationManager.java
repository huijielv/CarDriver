package com.ymx.driver.map;

import android.content.Context;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ymx.driver.util.LogUtil;


import static com.amap.api.location.AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;

public class ServerLocationManager {
    private Context context;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private ServerLocation serverLocation;
    private static volatile ServerLocationManager sLocationManager;

    private ServerLocationManager(Context context) {
        this.context = context;
        initLocation();
    }

    public static ServerLocationManager getInstance(Context context) {
        if (sLocationManager == null) {
            synchronized (LocationManager.class) {
                if (sLocationManager == null) {
                    sLocationManager = new ServerLocationManager(context);
                }
            }
        }
        return sLocationManager;
    }


    public void startLocation(ServerLocation serverLocation) {
        this.serverLocation = serverLocation;
        onDestroy();
        initLocation();

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    private void initLocation() {
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);


    }

    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    serverLocation.success(amapLocation);

                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    serverLocation.fails();
                }
            }
        }
    };

    public void onDestroy() {

        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

}
