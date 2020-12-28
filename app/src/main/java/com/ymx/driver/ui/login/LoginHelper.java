package com.ymx.driver.ui.login;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.AppConfig;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.entity.app.UserEntityDao;
import com.ymx.driver.mqtt.MQTTService;
import com.ymx.driver.mqtt.MqttManager;
import com.ymx.driver.ui.login.activity.LoginActivity;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.NewOrderTTSController;
import com.ymx.driver.util.PlayMediaPlayerManager;
import com.ymx.driver.util.SPUtils;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.util.VoicePlayMannager;

/**
 * Created by wuwei
 * 2019/12/12
 * 佛祖保佑       永无BUG
 */
public class LoginHelper {
    private static final String TAG = "LoginHelper";
    private Context context;

    private static volatile LoginHelper sLoginHelper;

    private LoginHelper(Context context) {
        this.context = context;
    }

    public static LoginHelper getInstance(Context context) {
        if (sLoginHelper == null) {
            synchronized (LoginHelper.class) {
                if (sLoginHelper == null) {
                    sLoginHelper = new LoginHelper(context);
                }
            }
        }
        return sLoginHelper;
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(YmxCache.getUserAccount());
    }

    public static boolean isIntro() {
        return !YmxCache.getIntro();
    }

    public static void logout() {
        try {
//            SPUtils.saveString(AppConfig.KET_DRIVET_SIDEIDLIST, "");
            PlayMediaPlayerManager.getInstance(UIUtils.getContext()).stop();
            NewOrderTTSController.getInstance(UIUtils.getContext()).stopSpeaking();
            VoicePlayMannager.getInstance(UIUtils.getContext()).stopSpeaking();
            MQTTService.stop(UIUtils.getContext());
            YmxApp.getInstance().closeDB();
            YmxCache.setOrderId("");
            YmxCache.setTranferCarState(0);
            SPUtils.remove(AppConfig.KEY_UID);
            SPUtils.remove(AppConfig.KEY_TOKEN);
            SPUtils.remove(AppConfig.KEY_DRIVERID);
            SPUtils.remove(AppConfig.KET_DRIVET_TYPE);
            SPUtils.remove(AppConfig.KET_DRIVET_SIDEIDLIST);
            AppManager.getAppManager().appExit();
            LoginActivity.start(UIUtils.getContext());
        } catch (Exception e) {

        }
    }

    public static UserEntity getUserEntity() {
        if (YmxApp.getInstance().getDaoSession() == null
                || YmxApp.getInstance().getDaoSession().getUserEntityDao() == null) {
            return null;
        }
        return YmxApp.getInstance().getDaoSession().getUserEntityDao().queryBuilder()
                .where(UserEntityDao.Properties.Uuid.eq(YmxCache.getUserAccount())).unique();
    }

    public void loginSuccess(UserEntity userEntity) {


        YmxCache.setUserAccount(userEntity.getUuid());
        YmxCache.setUserToken(userEntity.getToken());
        YmxCache.setDriverId(userEntity.getDriverId());
        YmxCache.setCarState(userEntity.getCarState());
        YmxCache.setDriverType(userEntity.getDriverType());
        YmxCache.setDriverType(userEntity.getLockState());
        YmxCache.setDriverIntegral(userEntity.getIntegral());
        //当用户使用自有账号登录时，可以这样统计
        MobclickAgent.onProfileSignIn(userEntity.getUuid());


        // 初始化数据库
        YmxApp.getInstance().initGreenDao(userEntity.getUuid());
        YmxApp.getInstance().getDaoSession().getUserEntityDao().insertOrReplace(userEntity);

        MqttManager.getInstance(context).startMqttService();
    }
}
