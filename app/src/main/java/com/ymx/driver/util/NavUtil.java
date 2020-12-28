package com.ymx.driver.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.ymx.driver.R;

import java.util.List;

/**
 * Created by wuwei
 * 2019/12/27
 * 佛祖保佑       永无BUG
 */
public class NavUtil {
    public static final String PN_GAODE_MAP = "com.autonavi.minimap";// 高德地图包名
    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap"; // 百度地图包名
    public static final String PN_TENCENT_MAP = "com.tencent.map"; // 腾讯地图包名

    /**
     * 检查地图应用是否安装
     *
     * @return
     */
    public static boolean isGdMapInstalled(Context context) {
        return isInstallPackage(context, PN_GAODE_MAP);
    }

    public static boolean isBaiduMapInstalled(Context context) {
        return isInstallPackage(context, PN_BAIDU_MAP);
    }

    public static boolean isTencentMapInstalled(Context context) {
        return isInstallPackage(context, PN_TENCENT_MAP);
    }

    private static boolean isInstallPackage(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packageInfoList = manager.getInstalledPackages(0);
        if (packageInfoList != null) {
            for (int i = 0; i < packageInfoList.size(); i++) {
                String package_name = packageInfoList.get(i).packageName;
                if (package_name.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 百度转高德
     *
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    /**
     * 高德、腾讯转百度
     *
     * @param gd_lon
     * @param gd_lat
     * @return
     */
    private static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     *
     * @param latLng
     * @returns 使用此方法需要下载导入百度地图的BaiduLBS_Android.jar包
     */
    public static LatLng BD09ToGCJ02(LatLng latLng) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = latLng.longitude - 0.0065;
        double y = latLng.latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lat = z * Math.sin(theta);
        double gg_lng = z * Math.cos(theta);
        return new LatLng(gg_lat, gg_lng);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * 即谷歌、高德 转 百度
     *
     * @param latLng
     * @returns 需要百度地图的BaiduLBS_Android.jar包
     */
    public static LatLng GCJ02ToBD09(LatLng latLng) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double z = Math.sqrt(latLng.longitude * latLng.longitude + latLng.latitude * latLng.latitude) + 0.00002 * Math.sin(latLng.latitude * x_pi);
        double theta = Math.atan2(latLng.latitude, latLng.longitude) + 0.000003 * Math.cos(latLng.longitude * x_pi);
        double bd_lat = z * Math.sin(theta) + 0.006;
        double bd_lng = z * Math.cos(theta) + 0.0065;
        return new LatLng(bd_lat, bd_lng);
    }

    /**
     * 打开高德地图导航功能
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openGaoDeNavi(Context context, String sname, double slat, double slon, String dname, double dlat, double dlon) {
        try {
            String uriString = null;
            StringBuilder builder = new StringBuilder("amapuri://route/plan?sourceApplication=maxuslife");
            if (slat != 0) {
                builder.append("&sname=").append(sname)
                        .append("&slat=").append(slat)
                        .append("&slon=").append(slon);
            }
            builder.append("&dlat=").append(dlat)
                    .append("&dlon=").append(dlon)
                    .append("&dname=").append(dname)
                    .append("&dev=0")
                    .append("&t=0");
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_GAODE_MAP);
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        } catch (Exception e) {
            UIUtils.showToast(UIUtils.getString(R.string.have_not_install_gaode_map));
        }
    }

    /**
     * 打开腾讯地图
     * params 参考http://lbs.qq.com/uri_v1/guide-route.html
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     *                驾车：type=drive，policy有以下取值
     *                0：较快捷
     *                1：无高速
     *                2：距离
     *                policy的取值缺省为0
     *                &from=" + dqAddress + "&fromcoord=" + dqLatitude + "," + dqLongitude + "
     */
    public static void openTencentMap(Context context, String sname, double slat, double slon, String dname, double dlat, double dlon) {
        try {
            String uriString = null;
            StringBuilder builder = new StringBuilder("qqmap://map/routeplan?type=walk&policy=0&referer=ZHCBZ-S373G-OJXQG-IJQRG-UXX7E-ANBO5");
            if (slat != 0) {
                builder.append("&from=").append(sname)
                        .append("&fromcoord=").append(slat)
                        .append(",")
                        .append(slon);
            }
            builder.append("&to=").append(dname)
                    .append("&tocoord=").append(dlat)
                    .append(",")
                    .append(dlon);
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_TENCENT_MAP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        } catch (Exception e) {
            UIUtils.showToast(UIUtils.getString(R.string.have_not_install_tecent_map));
        }
    }

    /**
     * 打开百度地图导航功能(默认坐标点是高德地图，需要转换)
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openBaiDuNavi(Context context, String sname, double slat, double slon, String dname, double dlat, double dlon) {
        try {

            String uriString = null;
            //终点坐标转换
            //        此方法需要百度地图的BaiduLBS_Android.jar包
            //        LatLng destination = new LatLng(dlat,dlon);
            //        LatLng destinationLatLng = GCJ02ToBD09(destination);
            //        dlat = destinationLatLng.latitude;
            //        dlon = destinationLatLng.longitude;

            double[] destination = gaoDeToBaidu(dlat, dlon);
            dlat = destination[0];
            dlon = destination[1];

            StringBuilder builder = new StringBuilder("baidumap://map/direction?mode=walk&");
            if (slat != 0) {
                //起点坐标转换

                //            LatLng origin = new LatLng(slat,slon);
                //            LatLng originLatLng = GCJ02ToBD09(origin);
                //            slat = originLatLng.latitude;
                //            slon = originLatLng.longitude;

                double[] origin = gaoDeToBaidu(slat, slon);
                slat = origin[0];
                slon = origin[1];

                builder.append("origin=latlng:")
                        .append(slat)
                        .append(",")
                        .append(slon)
                        .append("|name:")
                        .append(sname);
            }
            builder.append("&destination=latlng:")
                    .append(dlat)
                    .append(",")
                    .append(dlon)
                    .append("|name:")
                    .append(dname);
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_BAIDU_MAP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        } catch (Exception e) {
            UIUtils.showToast(UIUtils.getString(R.string.have_not_install_baidu_map));
        }
    }

    /**
     * @param slat 起点纬度
     * @param slon 起点经度
     * @param dlat 终点纬度
     * @param dlon 终点经度
     */
    public static void neizhiGaodeNavi(Context context, double slat, double slon, double dlat, double dlon, String endName) {
        Poi start = null;
        Poi end = null;
        //如果设置了起点
        if (slat != 0 && slon != 0) {
            start = new Poi("", new LatLng(slat, slon), "");
        }
        if (dlat != 0 && dlon != 0) {
            end = new Poi(endName, new LatLng(dlat, dlon), "");
        }

        AmapNaviParams params = new AmapNaviParams(start, null, end, AmapNaviType.DRIVER);
        params.setUseInnerVoice(true);
        params.setMultipleRouteNaviMode(true);
        params.setNeedDestroyDriveManagerInstanceWhenNaviExit(true);
        //发起导航

        if (dlat == 0 && dlon == 0) {
            AmapNaviPage.getInstance().showRouteActivity(context, new AmapNaviParams(null), null);
        } else {
            AmapNaviPage.getInstance().showRouteActivity(context, params, new INaviInfoCallback() {
                @Override
                public void onInitNaviFailure() {

                }

                @Override
                public void onGetNavigationText(String s) {

                }

                @Override
                public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

                }

                @Override
                public void onArriveDestination(boolean b) {

                }

                @Override
                public void onStartNavi(int i) {

                }

                @Override
                public void onCalculateRouteSuccess(int[] ints) {

                }

                @Override
                public void onCalculateRouteFailure(int i) {

                }

                @Override
                public void onStopSpeaking() {

                }

                @Override
                public void onReCalculateRoute(int i) {

                }

                @Override
                public void onExitPage(int i) {

                }

                @Override
                public void onStrategyChanged(int i) {

                }

                @Override
                public View getCustomNaviBottomView() {
                    return null;
                }

                @Override
                public View getCustomNaviView() {
                    return null;
                }

                @Override
                public void onArrivedWayPoint(int i) {

                }

                @Override
                public void onMapTypeChanged(int i) {

                }

                @Override
                public View getCustomMiddleView() {
                    return null;
                }

                @Override
                public void onNaviDirectionChanged(int i) {

                }

                @Override
                public void onDayAndNightModeChanged(int i) {

                }

                @Override
                public void onBroadcastModeChanged(int i) {

                }

                @Override
                public void onScaleAutoChanged(boolean b) {

                }
            });
        }


    }


    public static boolean isGpsOpen(Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGps;
    }

}
