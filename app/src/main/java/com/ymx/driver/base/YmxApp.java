package com.ymx.driver.base;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.dgrlucky.log.LogX;
import com.meituan.android.walle.WalleChannelReader;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.ymx.driver.BuildConfig;
import com.ymx.driver.config.AppConfig;
import com.ymx.driver.entity.app.DaoMaster;
import com.ymx.driver.entity.app.DaoSession;
import com.ymx.driver.entity.app.MySQLiteOpenHelper;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.mqtt.MqttManager;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.ui.main.activity.MainActivity;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.MyLifecycleHandler;
import com.ymx.driver.util.NavUtil;
import com.ymx.driver.util.NotificationUtil;
import com.ymx.driver.util.RomUtil;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.mine.NavSettingViewModel;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.oppo.OppoRegister;
import org.android.agoo.vivo.VivoRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

import me.jessyan.autosize.AutoSize;

import static com.ymx.driver.config.AppConfig.BUGLY_KEY;

/**
 * Created by wuwei
 * 2019/12/5
 * 佛祖保佑       永无BUG
 */
public class YmxApp extends MultiDexApplication {
    private final String TAG = this.getClass().getSimpleName();
    private static YmxApp ymxApp;
    private DaoSession daoSession;
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private int appCount;
    public boolean isRunInBackground;

    @Override
    public void onCreate() {
        super.onCreate();
        // 主项目配置
        setApplication(this);

        // 崩溃抓取
//        CrashHandler.getInstance().init(this);

        // 适配配置
        AutoSize.initCompatMultiProcess(this);

        // 友盟配置
        initUmeng();

        //腾讯x5内核浏览器配置
        initTbsX5();

        if (TextUtils.equals(UIUtils.getProcessName(this), getPackageName())) {
            ymxApp = this;

            //配置导航模式
            setNavMode(this);

            // 启动数据库
            if (LoginHelper.isLogin()) {
                initGreenDao(YmxCache.getUserAccount());
                // Mqtt配置
                MqttManager.getInstance(this).startMqttService();
            }

            // 启动清除所有通知
            NotificationUtil.deleteAllNotification(this);

            // 定位及相关服务配置
            back2App(true);
            // 内存检查工具
          if (BuildConfig.DEBUG) {
              LeakCanary.install(this);
        }

        }
    }

    /**
     * 当主工程没有继承BaseApplication时，可以使用setApplication方法初始化BaseApplication
     *
     * @param application
     */
    private synchronized void setApplication(@NonNull YmxApp application) {
        //初始化工具类
        UIUtils.init(application);
        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                AppManager.getAppManager().addActivity(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                appCount++;
                if (isRunInBackground) {
                    //应用从后台回到前台 需要做的操作
                    back2App(false);
                }

                NotificationUtil.deleteAllNotification(activity.getApplication());
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                appCount--;
                if (appCount == 0) {
                    //应用进入后台 需要做的操作
                    leaveApp();
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                AppManager.getAppManager().removeActivity(activity);
            }
        });
    }

    private void initUmeng() {
        UMConfigure.init(getApplicationContext(), AppConfig.UM_KEY,
                WalleChannelReader.getChannel(getApplicationContext(), "default"),
                UMConfigure.DEVICE_TYPE_PHONE, AppConfig.UM_PUSH_SECRET);
        UMConfigure.setEncryptEnabled(true);
        MobclickAgent.setCatchUncaughtExceptions(false);
        PlatformConfig.setWeixin(AppConfig.WEXIN_KEY, AppConfig.WEXIN_VALUE);
        PlatformConfig.setQQZone(AppConfig.QQ_KEY, AppConfig.QQ_VALUE);
        UMShareAPI.get(getApplicationContext());
        //获取消息推送代理示例
        PushAgent pushAgent = PushAgent.getInstance(this);

        pushAgent.setMessageHandler(new UmengMessageHandler() {
            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);
                LogUtil.i(TAG, "--收到消息: ");
                LogX.i(uMessage);
            }

            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                LogUtil.i(TAG, "--通知栏: ");
                LogX.i(uMessage);
                return super.getNotification(context, uMessage);
            }
        });
        pushAgent.setNotificationClickHandler(new UmengNotificationClickHandler() {
            @Override
            public void launchApp(Context context, UMessage uMessage) {
                super.launchApp(context, uMessage);
                LogUtil.i(TAG, "--启动app: ");
                LogX.i(uMessage);
            }

            @Override
            public void openUrl(Context context, UMessage uMessage) {
                super.openUrl(context, uMessage);
                LogUtil.i(TAG, "--打开url: ");
                LogX.i(uMessage);
            }

            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);
                LogUtil.i(TAG, "--打开activity: ");
                LogX.i(uMessage);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                LogUtil.i(TAG, "--自定义action: " + uMessage.after_open + "  " + uMessage.custom);
                try {
                    if (!TextUtils.isEmpty(uMessage.custom)) {
                        MainActivity.start(context, new Intent()
                                .putExtra(MainActivity.EXTRA_DATA, uMessage.custom));

                    } else {
                        MainActivity.start(context);
                    }
                } catch (Exception e) {
                    MainActivity.start(context);
                }
                LogX.i(uMessage);
            }
        });


        pushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                LogUtil.i(TAG, "注册成功：deviceToken：-------->  " + deviceToken);

                YmxCache.setDeviceToken(deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtil.e(TAG, "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });
        initBugly();
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
        if (RomUtil.isXiaomi()) {
            MiPushRegistar.register(this, AppConfig.XIAOMI_ID, AppConfig.XIAOMI_KEY);
        } else if (RomUtil.isHuawei()) {
            HuaWeiRegister.register(this);
        } else if (RomUtil.isOppo()) {
            OppoRegister.register(this, AppConfig.OPPO_KEY, AppConfig.OPPO_SECRET);
        } else if (RomUtil.isVivo()) {
            VivoRegister.register(this);
        }
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), BUGLY_KEY, false);

    }

    private void initTbsX5() {
        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtil.i(TAG, "腾讯x5内核浏览器：-------->  " + arg0);
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    /**
     * 从后台回到前台需要执行的逻辑
     */
    private void back2App(boolean fromOnCreate) {
        isRunInBackground = fromOnCreate;
        LocationManager.getInstance(this).setBackground(fromOnCreate);
    }

    /**
     * 离开应用 压入后台或者退出应用
     */
    private void leaveApp() {
        isRunInBackground = true;
        LocationManager.getInstance(this).setBackground(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void setNavMode(Context context) {
        if (YmxCache.getNavMode() != -1) {
            return;
        }
        if (NavUtil.isGdMapInstalled(context)) {
            YmxCache.setNavMode(NavSettingViewModel.NAV_MODE_GAODE);
            return;
        }

        if (NavUtil.isBaiduMapInstalled(context)) {
            YmxCache.setNavMode(NavSettingViewModel.NAV_MODE_BAIDU);
            return;
        }

        if (NavUtil.isTencentMapInstalled(context)) {
            YmxCache.setNavMode(NavSettingViewModel.NAV_MODE_TENCENT);
            return;
        }
    }

    public static YmxApp getInstance() {
        return ymxApp;
    }

    public void initGreenDao(String uid) {
        // 初始化数据库
        helper = new MySQLiteOpenHelper(this, "ymx-" + uid + ".db", null);
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void closeDB() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (db != null) {
            db.close();
            db = null;
        }
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }
}
