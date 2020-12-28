package com.ymx.driver.keepalive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


import com.ymx.driver.util.LogUtil;

import java.lang.ref.WeakReference;

/**
 * APP保活的管理类
 */

public class KeepAliveManager {

    private static WeakReference<Activity> weakReference;
    private static KeepAliveManager instance;
    private static ScreenReceiver screenReceiver;
    private static KeepAliveActivity keepAliveActivity;

    private KeepAliveManager() {
    }

    public static KeepAliveManager getInstance() {
        if (instance == null) {
            instance = new KeepAliveManager();
        }
        return instance;
    }

    public void init(Activity activity) {
        if (weakReference == null) {
            weakReference = new WeakReference<>(activity);
        }
        // 开启前台服务

        KeepAliveJobManager.startJobScheduler(activity);

    }

    public void destroy() {
        unregisterScreenListener();
    }

    public void setKeepAliveActivity(KeepAliveActivity activity) {
        keepAliveActivity = activity;
        // 注册锁屏和解锁的广播监听
        registerScreenListener();
    }



    // 注册屏幕广播监听器
    private void registerScreenListener() {
        if (weakReference != null) {
            Activity activity = weakReference.get();
            if (activity != null) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
                intentFilter.addAction(Intent.ACTION_USER_PRESENT);
                activity.registerReceiver(screenReceiver = new ScreenReceiver(), intentFilter);
            }
        }
    }

    // 反注册屏幕广播监听器
    private void unregisterScreenListener() {
        if (weakReference != null) {
            Activity activity = weakReference.get();
            if (activity != null && screenReceiver != null) {
                activity.unregisterReceiver(screenReceiver);
            }
        }
    }

    // 锁屏和解锁的广播监听器，该广播只能通过代码动态注册，不能配置在AndroidManifest中
    private class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            abortBroadcast();
            String action = intent.getAction();
            if (Intent.ACTION_USER_PRESENT.equals(action)) {
                // 屏幕解锁，关闭KeepAliveActivity
                LogUtil.d("KeepAliveManager1", "KeepAliveManager");
                closeKeepAliveActivity();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                // 屏幕关闭，开启KeepAliveActivity
                LogUtil.d("KeepAliveManager", "KeepAliveManager");
                openKeepAliveActivity();
            }



        }
    }

    private void openKeepAliveActivity() {
        if (keepAliveActivity != null) {
            keepAliveActivity.finish();
            keepAliveActivity = null;
        }
        if (weakReference != null) {
            Activity activity = weakReference.get();
            if (activity != null) {
                activity.startActivity(new Intent(activity, KeepAliveActivity.class));
            }
        }
    }

    private void closeKeepAliveActivity() {
        if (keepAliveActivity != null) {
            keepAliveActivity.finish();
            keepAliveActivity = null;
        }
    }

}
