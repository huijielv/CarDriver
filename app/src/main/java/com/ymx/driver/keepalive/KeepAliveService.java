package com.ymx.driver.keepalive;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.ymx.driver.util.NotificationUtil;
import java.util.Random;



/**
 * 应用保活使用的Service
 */
public class KeepAliveService extends Service {

    private static Random random = new Random();
    private static int _id;

    public KeepAliveService() {
        _id = random.nextInt(1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startForeground(_id, new Notification());
        } else {
            // 用InnerService去除通知
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                startForeground(110, NotificationUtil.buildNotification(this, NotificationUtil.CHANNELID
                        , NotificationUtil.CHANNELNAME));

            }else {
                startForeground(_id, new Notification());
            }

            startService(new Intent(this, InnerService.class));
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // 用这个内部Service去除通知栏的提示
    public static class InnerService extends Service {

        public InnerService() {}

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                startForeground(110, NotificationUtil.buildNotification(this, NotificationUtil.CHANNELID
                        , NotificationUtil.CHANNELNAME));
            }else {
                startForeground(_id, new Notification());
                stopForeground(true);
            }

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.cancel(_id);
            }
            stopSelf();
            return START_NOT_STICKY;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

    }

}
