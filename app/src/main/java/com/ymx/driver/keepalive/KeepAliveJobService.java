package com.ymx.driver.keepalive;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;


import com.ymx.driver.mqtt.MQTTService;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.NotificationUtil;


import java.util.List;

/**
 * 保活用的JobService
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class KeepAliveJobService extends JobService {

    private final static String TAG = "KeepAliveJobService";

    // 告知编译器，这个变量不能被优化
    private volatile static Service mKeepAliveService = null;

    public static boolean isJobServiceAlive() {
        return mKeepAliveService != null;
    }

    private static final int MESSAGE_ID_TASK = 0x01;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // 具体任务逻辑
            if (isAppAlive(KeepAliveJobService.this, getPackageName())) {
                LogUtil.e(TAG, "handleMessage: app is alive");
            } else {
                MQTTService.start(getApplication(), MQTTService.Action);
                LogUtil.e(TAG, "handleMessage: app is not running!!!");

            }

            LogUtil.e(TAG, "handleMessage");
            // 通知系统任务执行结束
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                jobFinished((JobParameters) msg.obj, true);
            } else {
                jobFinished((JobParameters) msg.obj, false);
            }

            return true;
        }
    });

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtil.d(TAG, "onStartJob");
        mKeepAliveService = this;
        // 返回false，系统假设这个方法返回时任务已经执行完毕；
        // 返回true，系统假定这个任务正要被执行
        Message msg = Message.obtain(mHandler, MESSAGE_ID_TASK, params);
        mHandler.sendMessage(msg);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mHandler.removeMessages(MESSAGE_ID_TASK);
        LogUtil.d(TAG, "onStopJob");
        return false;
    }

    // 判断APP是否在运行
    private boolean isAppAlive(Context mContext, String packageName) {
        boolean isAPPRunning = false;
        // 获取activity管理对象
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            // 获取所有正在运行的app
            List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
            // 遍历，进程名即包名
            if(appProcessInfoList!=null){
                for (ActivityManager.RunningAppProcessInfo appInfo : appProcessInfoList) {
                    if (appInfo != null) {
                        if (packageName.equals(appInfo.processName)) {
                            isAPPRunning = true;
                            break;
                        }
                    }

                }
            }

            return isAPPRunning;
        }
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        try {
            startForeground(111, NotificationUtil.buildNotification(this, NotificationUtil.CHANNELID
                    , NotificationUtil.CHANNELNAME));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return START_STICKY;
    }

}
