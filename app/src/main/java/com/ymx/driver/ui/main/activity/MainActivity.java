package com.ymx.driver.ui.main.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;


import com.google.gson.Gson;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.tencent.bugly.crashreport.CrashReport;
import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.DefaultStyleDialog;

import com.ymx.driver.broadcastreceiver.NetBroadcastReceiver;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.config.PushConfig;
import com.ymx.driver.databinding.ActivityMainBinding;
import com.ymx.driver.entity.BasePushEntity;
import com.ymx.driver.entity.SystemNoticeEntity;
import com.ymx.driver.entity.app.CancelOrderEntity;
import com.ymx.driver.entity.app.UpdateEntity;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.keepalive.KeepAliveManager;
import com.ymx.driver.mqtt.MQTTService;

import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.ui.longrange.driving.LongRangeDrivingActivity;
import com.ymx.driver.ui.main.fragment.HomeFragment;
import com.ymx.driver.ui.main.fragment.MineFragment;
import com.ymx.driver.ui.main.fragment.MsgFragment;
import com.ymx.driver.ui.main.fragment.SystemNoticeDialogFragment;

import com.ymx.driver.ui.my.wallet.MyWalletActivity;
import com.ymx.driver.util.ScreenUtils;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.main.MainViewModel;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {
    private static String TAG = "MainActivity";
    private int[] mainIndicatorTitleIds = {R.string.mine, R.string.home, R.string.message};
    private List<Fragment> mainFragmentLists = new ArrayList<>();
    private NetBroadcastReceiver netBroadcastReceiver;
    private long autoUpdateTime = 3 * 60 * 1000;
    private ScreenUtils screenUtils;
    public static final String EXTRA_DATA = "extra_data";
    private DefaultStyleDialog defaultStyleCancalDialog;

    public static void start(Context context) {
        start(context, null);
    }


    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {


            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_HOME_FRAGMENT_DATA_CODE));
            handler.postDelayed(this, autoUpdateTime);
        }
    };


    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(context, MainActivity.class);


        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        binding.mainViewpager.setCurrentItem(1);
        parseIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (immersionBarEnabled()) {
            if (isFullScreen()) {
                ImmersionBar.with(this)
                        .fullScreen(true)
                        .navigationBarColor(R.color.rx_transparent)
                        .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                        .init();
            } else {
                ImmersionBar.with(this)
                        .fitsSystemWindows(true)
                        .statusBarDarkFont(statusBarDarkFont())
                        .statusBarColor(R.color.rx_ff8008)
                        .init();
            }
        }
    }

    private void parseIntent(Intent intent) {
        try {
            if (intent == null) {
                return;
            }
            if (intent.hasExtra(EXTRA_DATA)) {
                String custom = intent.getStringExtra(EXTRA_DATA);
                intent.removeExtra(EXTRA_DATA);
                if (TextUtils.isEmpty(custom)) {
                    return;
                }
                BasePushEntity basePushEntity = new Gson().fromJson(custom, BasePushEntity.class);

                if (TextUtils.equals(PushConfig.PUSH_CODE_GO_APP_MAIN,
                        basePushEntity.getCode())) {//仅启动app，进首页，不解析数据

                } else if (TextUtils.equals(PushConfig.PUSH_CODE_GO_ORDER,
                        basePushEntity.getCode())) {//进入
                } else if (TextUtils.equals(PushConfig.PUSH_CODE_GO_GOOD_FIREND_ORDER_RECOVERY,
                        basePushEntity.getCode())) {
                } else if (TextUtils.equals(PushConfig.PUSH_CODE_MYWALLET,
                        basePushEntity.getCode())) {
                    MyWalletActivity.start(activity);


                } else if ((TextUtils.equals(PushConfig.PUSH_CODE_LONG_DRIVING_SELECTTIME,
                        basePushEntity.getCode()))) {
                    LongRangeDrivingActivity.start(activity);
                } else if ((TextUtils.equals(PushConfig.PUSH_CODE_LONG_DRIVING_DETAILS,
                        basePushEntity.getCode()))) {

                }


            }
        } catch (Exception e) {

        }
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        mainFragmentLists.add(new MineFragment());
        mainFragmentLists.add(new HomeFragment());
        mainFragmentLists.add(new MsgFragment());
        initMainViewPager();
        initMagicIndicator();
        binding.mainViewpager.setCurrentItem(1);
//        viewModel.mainFragmentIndex.set(1);
        Activity topActivity = AppManager.getAppManager().currentActivity();
        if (topActivity != null) {
//             new DefaultStyleDialog(topActivity)
//                    .setBody(getString(R.string.order_details_dialog_desc))
//                    .setNegativeText(getString(R.string.order_details_dialog_cancal))
//                    .setPositiveText(getString(R.string.order_details_dialog_confirm))
//                    .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
//                        @Override
//                        public void negative(Dialog dialog) {
//                            dialog.dismiss();
//
//                        }
//
//                        @Override
//                        public void positive(Dialog dialog) {
//                            dialog.dismiss();
//
//
//                        }
//                    }).show();
        }

    }

    private void initMainViewPager() {
        binding.mainViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mainFragmentLists.get(position);
            }

            @Override
            public int getCount() {
                return mainFragmentLists.size();
            }
        });
    }

    private void initMagicIndicator() {
        binding.mainIndicator.setBackgroundColor(UIUtils.getColor(R.color.rx_ff8008));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mainIndicatorTitleIds == null ? 0 : mainIndicatorTitleIds.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mainIndicatorTitleIds[index]);
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setNormalColor(UIUtils.getColor(R.color.rx_f2f2f2));
                simplePagerTitleView.setSelectedColor(UIUtils.getColor(R.color.rx_ffffff));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.mainViewpager.setCurrentItem(index);
                        if (index == 1) {
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_HOME_FRAGMENT_DATA_CODE));
                        } else if (index == 2) {
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_HOME_MESSAGE_DATA_CODE));
                        }
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(UIUtils.dip2px(2));
                indicator.setColors(UIUtils.getColor(R.color.white));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(UIUtils.dip2px(38));
                return indicator;
            }
        });
        binding.mainIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(binding.mainIndicator, binding.mainViewpager);
        LocationManager locationManager
                = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGps) {
            showGpsDialog();
        }
    }


    @Override
    public void initData() {

        if (LoginHelper.isLogin()) {

            try {
                UserEntity userEntity = LoginHelper.getUserEntity();
                if (userEntity != null) {
                    String phoneNumber = "";
                    phoneNumber = userEntity.getPhone();
                    CrashReport.putUserData(this, "phone", phoneNumber);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        initNetReceiver();
        viewModel.update(getPackageName());
        viewModel.getSystemNoticeList();
        if (handler != null) {
            handler.postDelayed(runnable, autoUpdateTime);
        }


        try {
            KeepAliveManager.getInstance().init(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

        initscreenListen();

    }


    public void initscreenListen() {
        screenUtils = new ScreenUtils(MainActivity.this);
        screenUtils.begin(new ScreenUtils.ScreenStateListener() {
            @Override
            public void onScreenOn() {


            }

            @Override
            public void onScreenOff() {
                if (!MQTTService.isConnecting()) {

                    MQTTService.start(getApplication(), MQTTService.initService);
                } else {
                    MQTTService.start(getApplication(), MQTTService.Action);
                }

            }

            @Override
            public void onUserPresent() {


                if (!MQTTService.isConnecting()) {

                    MQTTService.start(getApplication(), MQTTService.initService);
                } else {
                    MQTTService.start(getApplication(), MQTTService.Action);
                }

            }
        });
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucUpdate.observe(this, new Observer<UpdateEntity>() {
            @Override
            public void onChanged(UpdateEntity updateEntity) {
                if (updateEntity.getUpgradeMode() == 0) {
                    return;
                }
                if (!isFinishing()) {
                    try {
                        Intent mIntent = new Intent(MainActivity.this, UpdateActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable(UpdateActivity.UPDATE_ENTITY, updateEntity);
                        mIntent.putExtras(mBundle);
                        startActivity(mIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        viewModel.uc.canCanOrder.observe(this, new Observer<CancelOrderEntity>() {
            @Override
            public void onChanged(CancelOrderEntity updateEntity) {
                defaultStyleCancalDialog = new DefaultStyleDialog(activity)
                        .setNegativeText("取消")
                        .setBody(updateEntity.getMessage())
                        .setPositiveText("确定")
                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                            @Override
                            public void negative(Dialog dialog) {
                                dialog.dismiss();

                            }

                            @Override
                            public void positive(Dialog dialog) {
                                dialog.dismiss();

                            }
                        });
                defaultStyleCancalDialog.show();
            }
        });


        viewModel.uc.ucGetSystemNotice.observe(this, new Observer<List<SystemNoticeEntity>>() {
            @Override
            public void onChanged(List<SystemNoticeEntity> list) {
                if (list == null || list.size() <= 0) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(SystemNoticeDialogFragment.SYSTEM_NOTICE_LIST, (Serializable) list);
                SystemNoticeDialogFragment.newInstance(bundle).show(getSupportFragmentManager(),
                        SystemNoticeDialogFragment.class.getSimpleName());
            }
        });

        viewModel.uc.ucTransfer0order.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                new DefaultStyleDialog(activity)
                        .setBody(s)
                        .setPositiveText("确定")
                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                            @Override
                            public void negative(Dialog dialog) {
                                dialog.dismiss();

                            }

                            @Override
                            public void positive(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        viewModel.uc.ucCancalDialogDiss.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (defaultStyleCancalDialog != null && defaultStyleCancalDialog.isShowing()) {
                    defaultStyleCancalDialog.dismiss();
                }
            }
        });


    }


    @Override
    protected boolean isExit() {
        return false;
    }


    public void initNetReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            netBroadcastReceiver = new NetBroadcastReceiver();
            //注册广播接收
            registerReceiver(netBroadcastReceiver, filter);
        }
    }

    public void showGpsDialog() {


        new DefaultStyleDialog(activity)
                .setNegativeText("取消")
                .setBody(getString(R.string.open_gps_body))
                .setPositiveText("确定")
                .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                    @Override
                    public void negative(Dialog dialog) {
                        dialog.dismiss();

                    }

                    @Override
                    public void positive(Dialog dialog) {
                        dialog.dismiss();

                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {

                            intent.setAction(Settings.ACTION_SETTINGS);
                            try {
                                startActivity(intent);
                            } catch (Exception e) {
                            }
                        }


                    }
                }).show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (defaultStyleCancalDialog != null && defaultStyleCancalDialog.isShowing()) {
            defaultStyleCancalDialog.dismiss();
        }

        if (netBroadcastReceiver != null) {
            unregisterReceiver(netBroadcastReceiver);
        }
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit()) {
                doSthIsExit();
            } else {
                exit();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
