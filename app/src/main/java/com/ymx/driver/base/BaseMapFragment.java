package com.ymx.driver.base;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.ymx.driver.util.AMapUtil;

import java.lang.reflect.Field;

/**
 * Created by wuwei
 * 2020/4/21
 * 佛祖保佑       永无BUG
 */
public abstract class BaseMapFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment {
    protected V xbinding;
    protected VM xviewModel;
    private TextureMapView mapView;
    protected AMap aMap;

    @Override
    public void initView(Bundle savedInstanceState) {
        this.xbinding = (V) binding;
        this.xviewModel = (VM) viewModel;
        initMapView(savedInstanceState);
    }

    private void initMapView(Bundle savedInstanceState) {
        mapView = getTextureMapView();
        if (mapView != null) {
            aMap = mapView.getMap();
            AMapUtil.setAMapStyle(aMap);
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    initMapLoad();
                }
            });
            mapView.onCreate(savedInstanceState);
        }
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        if (null != mapView) {
            mapView.onResume();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        if (null != mapView) {
            mapView.onPause();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != mapView) {
            mapView.onSaveInstanceState(outState);
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        removeMapRouteView();
        if (null != mapView) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        try {
//            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
//            childFragmentManager.setAccessible(true);
//            childFragmentManager.set(this, null);
//        } catch (Exception e) {
//
//        }
    }

    /**
     * 移除地图上的marker和路线
     */
    protected void removeMapRouteView() {
        if (null != getaMap()) {
            getaMap().clear();
        }

    }

    public AMap getaMap() {
        return aMap;
    }

    protected void setMapCenter(int width, int height) {
        if (getaMap() != null) {
            getaMap().setPointToCenter(width, height);
        }
    }

    public void moveToCamera(LatLng center) {
        //设置缩放级别
        float zoom = 17;
        if (null != getaMap()) {
            //zoom - 缩放级别，[3-20]。
            getaMap().animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
        }

    }

    /**
     * 在导航的地图MapView上移动视角
     */
    public void moveToCamera(LatLngBounds latLngBounds, Rect rect) {
        if (null != getaMap()) {
            getaMap().animateCamera(CameraUpdateFactory.newLatLngBoundsRect(latLngBounds,
                    rect.left, rect.right, rect.top, rect.bottom));
        }
    }

    /**
     * 在导航的地图MapView上移动视角
     */
    public void moveToCamera(LatLng start, LatLng end, Rect rect) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(start);
        builder.include(end);
        LatLngBounds latLngBounds = builder.build();
        // newLatLngBoundsRect(LatLngBounds latlngbounds, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom)
        //newLatLngBoundsRect(LatLngBounds latlngbounds,
        //int paddingLeft,设置经纬度范围和mapView左边缘的空隙。
        //int paddingRight,设置经纬度范围和mapView右边缘的空隙
        //int paddingTop,设置经纬度范围和mapView上边缘的空隙。
        //int paddingBottom)设置经纬度范围和mapView下边缘的空隙。
        if (null != getaMap()) {
            getaMap().animateCamera(CameraUpdateFactory.newLatLngBoundsRect(latLngBounds, rect.left, rect.right, rect.top, rect.bottom));
        }
    }

    /**
     * 获取地图mapView
     *
     * @return
     */
    protected abstract TextureMapView getTextureMapView();

    /**
     * 初始化地图一些initMapCreate 在onActivityCreated后调用
     * initViews比initMapCreate先执行
     */
    protected abstract void initMapLoad();
}
