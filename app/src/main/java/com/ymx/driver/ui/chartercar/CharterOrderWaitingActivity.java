package com.ymx.driver.ui.chartercar;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityCharterWaitingBinding;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.tts.BaiduSpeech;
import com.ymx.driver.ui.travel.activity.TravelActivity;
import com.ymx.driver.util.DateUtils;
import com.ymx.driver.util.NavUtil;
import com.ymx.driver.util.NavigationMapUtils;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.SwipeButton;
import com.ymx.driver.viewmodel.chartercar.CharterDetailsViewModel;
import com.ymx.driver.viewmodel.mine.NavSettingViewModel;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class CharterOrderWaitingActivity extends BaseActivity<ActivityCharterWaitingBinding, CharterDetailsViewModel> {
    public static final String ORDERI_ID = "orderNo";
    private String orderNo;
    private Timer timer;
    private CountdownTask countdownTask;


    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, CharterOrderWaitingActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_charter_waiting;
    }

    @Override
    public int initVariableId() {
        return com.ymx.driver.BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.btnSubmit.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {

            }

            @Override
            public void onSwipeCancel() {

            }

            @Override
            public void onSwipeConfirm() {
                new DefaultStyleDialog(activity)
                        .setBody("是否确认继续行程?")
                        .setNegativeText("取消")
                        .setPositiveText("确定")
                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                            @Override
                            public void negative(Dialog dialog) {
                                dialog.dismiss();

                            }

                            @Override
                            public void positive(Dialog dialog) {
                                dialog.dismiss();
                                viewModel.CharterOrderStartLocation(viewModel.orderId.get(), 3, "");
                            }
                        }).show();
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(ORDERI_ID)) {
            orderNo = intent.getStringExtra(ORDERI_ID);
            viewModel.orderId.set(orderNo);
            viewModel.charteredOrderDetails(orderNo);
        }
        initTime();
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.ucCall.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                requestPermission(PermissionConfig.PC_CALL_PHONE, false, Manifest.permission.CALL_PHONE);
            }
        });

        viewModel.uc.ucNavigate.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (viewModel.lat != null && viewModel.lat.get() != null && viewModel.lng != null && viewModel.lng.get() != null) {
                    navigation(0.0, 0.0, viewModel.startName.get());
                }else {
                    UIUtils.showToast("");
                }

            }
        });


        viewModel.uc.ucOrderDetails.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                if (viewModel.time != null && viewModel.time.get() != null) {
                    countdownTimer(viewModel.time.get());
                }

            }
        });
        viewModel.uc.ucUpdateOrderStatus.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
                Intent intent = new Intent();
                intent.putExtra(TravelActivity.ORDERI_ID, orderNo);
                CharterOrderDetailsActivity.start(activity, intent);

            }
        });


    }


    public void navigation(double lat, double log, String addressName) {
        if (YmxCache.getNavStatus() == 0) {
            if (YmxCache.getNavMode() == NavSettingViewModel.NAV_MODE_GAODE) {
                NavigationMapUtils.goGaodeMap(getApplicationContext(), lat, log, addressName);
            } else if (YmxCache.getNavMode() == NavSettingViewModel.NAV_MODE_BAIDU) {
                NavUtil.openBaiDuNavi(getApplicationContext(), "", LocationManager.getInstance(getApplicationContext()).getLatitude(), LocationManager.getInstance(getApplicationContext()).getLongitude(), addressName, lat, log);
            } else if (YmxCache.getNavMode() == NavSettingViewModel.NAV_MODE_TENCENT) {
                NavUtil.openTencentMap(getApplicationContext(), "", LocationManager.getInstance(getApplicationContext()).getLatitude(), LocationManager.getInstance(getApplicationContext()).getLongitude(), addressName, lat, log);
            }
        } else {
            NavUtil.neizhiGaodeNavi(getApplicationContext(), LocationManager.getInstance(getApplicationContext()).getLatitude(), LocationManager.getInstance(getApplicationContext()).getLongitude(), lat, log, addressName);
        }
    }

    @Override
    protected void permissionGranted(int requestCode) {
        if (requestCode == PermissionConfig.PC_CALL_PHONE) {
            try {
                if (TextUtils.isEmpty(viewModel.phoneNum.get())) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + viewModel.phoneNum.get());
                intent.setData(data);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initTime() {
        timer = new Timer();
        UIUtils.postDelayTask(new Runnable() {
            @Override
            public void run() {

            }
        }, 200);

    }


    public class CountdownTask extends TimerTask {
        private int mTime;
        private WeakReference<TextView> weakText;

        public CountdownTask(int time, TextView textView) {
            mTime = time;
            weakText = new WeakReference<>(textView);
        }

        @Override
        public void run() {
            UIUtils.postTask(() -> {
                if (viewModel.isTimeout != null && viewModel.isTimeout.get() != null) {
                    if (viewModel.isTimeout.get() == 1) {
                        mTime++;
                    } else if (viewModel.isTimeout.get() == 0) {
                        mTime--;
                    }
                }


                weakText.get().setText(DateUtils.toTime((long) mTime));
                if (mTime < 0) {
                    cancel();


                } else if (mTime == 0) {
                    viewModel.isTimeout.set(1);
                    viewModel.feeText.set("已产生超时等待费用：0.0元");
                    viewModel.timeText.set("已超时");

                }
            });
        }
    }

    public void countdownTimer(int interval) {
        if (countdownTask != null) {
            countdownTask.cancel();
        }

        countdownTask = new CountdownTask(interval, binding.charterTime);
        timer.schedule(countdownTask, 0, 1000);
    }


    public void cancalTimeTask() {
        if (countdownTask != null) {
            countdownTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
    }


    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_TRIP_REFRESH_DATA_CODE));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_HOME_FRAGMENT_DATA_CODE));
        cancalTimeTask();

    }
}
