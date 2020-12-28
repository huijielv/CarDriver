
package com.ymx.driver.keepalive;
import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.ymx.driver.util.LogUtil;


/**

 */

public class KeepAliveJobManager {

    private static final String TAG = "KeepAliveJobManager";
    private static JobScheduler mJobScheduler;
    private static final long DURATION = 60 * 1000;


    @TargetApi(21)
    public static void startJobScheduler(Context context) {
        // 如果JobService已经启动或API<21，返回

        if (isBelowLOLLIPOP()) {

            return;
        }
        if (KeepAliveJobService.isJobServiceAlive()) {

            LogUtil.e(TAG, "JobService is running...");
            return;
        }
        mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (mJobScheduler == null) {


            return;
        }


        // 构建JobInfo对象，传递给JobSchedulerService
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(context, KeepAliveJobService.class));
        // 设置执行任务的时间间隔

        if (Build.VERSION.SDK_INT >= 24){

            builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS); //执行的最小延迟时间
            builder.setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);  //执行的最长延时时间
            builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
            builder.setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案

        }else {
            builder.setPeriodic(DURATION);
        }

        // 设置设备重启时，执行该任务
        builder.setPersisted(true);
        // 当插入充电器，执行该任务
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresCharging(true);
        JobInfo info = builder.build();
        mJobScheduler.schedule(info);
    }

    @TargetApi(21)
    public static void stopJobScheduler() {
        if (isBelowLOLLIPOP())
            return;
        if (mJobScheduler != null) {
            mJobScheduler.cancelAll();
        }
    }

    private static boolean isBelowLOLLIPOP() {
        // API < 21
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

}
