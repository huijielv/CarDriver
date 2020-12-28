package com.ymx.driver.util;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.ymx.driver.R;


/**
 * Created by let on 13/07/2017.
 */

public class AMapUtil {
    public static void setAMapStyle(AMap aMap) {
        if (aMap == null) {
            return;
        }
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setLogoBottomMargin(-50);
        aMap.showBuildings(true);
        aMap.showIndoorMap(true);
        aMap.showMapText(true);

        CustomMapStyleOptions options = new CustomMapStyleOptions();
        options.setEnable(true);
        /*String styleName = "style.data";
        InputStream inputStream = null;
        try {
            inputStream = baseActivity.getAssets().open(styleName);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            if (options != null) {
                // 设置自定义样式
                options.setStyleData(b);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        aMap.setCustomMapStyle(options);

        MyLocationStyle myLocationStyle = aMap.getMyLocationStyle();
        if (myLocationStyle == null) {
            myLocationStyle = new MyLocationStyle();
        }
        myLocationStyle.strokeWidth(0);
        myLocationStyle.radiusFillColor(UIUtils.getColor(R.color.rx_transparent));

        aMap.setMyLocationEnabled(true);
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);

        aMap.getUiSettings().setZoomGesturesEnabled(true);
        aMap.getUiSettings().setScrollGesturesEnabled(true);
        aMap.getUiSettings().setTiltGesturesEnabled(false);
        aMap.getUiSettings().setRotateGesturesEnabled(false);

        aMap.setMyLocationStyle(myLocationStyle);
    }
}
