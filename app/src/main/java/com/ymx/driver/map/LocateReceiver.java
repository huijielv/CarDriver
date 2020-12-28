package com.ymx.driver.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.amap.api.location.AMapLocation;

/**
 * Created by wuwei
 * 2018/3/24
 * 佛祖保佑       永无BUG
 */
public class LocateReceiver {
    private Context mContext;
    private LocateSuccessListener mListener;
    private LocateSuccessReceiver mLocateSuccessReceiver;

    public LocateReceiver(Context context) {
        mContext = context;
        mLocateSuccessReceiver = new LocateSuccessReceiver();
    }

    public void startObserve(LocateSuccessListener locateSuccessListener) {
        mListener = locateSuccessListener;
        registerListener();
    }

    public void endObserver() {
        unregisterListener();
    }


    //动态广播注册
    private void registerListener() {
        if (mContext != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("LocationReceiver");
            mContext.registerReceiver(mLocateSuccessReceiver, intentFilter);
        }
    }

    //销毁广播
    private void unregisterListener() {
        if (mContext != null)
            mContext.unregisterReceiver(mLocateSuccessReceiver);
    }

    public class LocateSuccessReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("LocationReceiver" .equals(intent.getAction())) {
                AMapLocation aMapLocation = intent.getParcelableExtra("AMapLocation");
                if (mListener != null) {
                    mListener.onLocateSuccess(aMapLocation);
                }
            }
        }
    }

    public interface LocateSuccessListener {
        void onLocateSuccess(AMapLocation aMapLocation);
    }
}
