package com.ymx.driver.ui.launch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityAdvertisingBinding;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.ui.login.activity.LoginActivity;
import com.ymx.driver.ui.main.activity.MainActivity;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.launch.AdvertisingViewModel;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wuwei
 * 2020/4/21
 * 佛祖保佑       永无BUG
 */
public class AdvertisingActivity extends BaseActivity<ActivityAdvertisingBinding, AdvertisingViewModel> {

    private Timer timer;
    private CountdownTask countdownTask;


    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, AdvertisingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_advertising;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        timer = new Timer();
        countdownTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countdownTask != null) {
            countdownTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    public void countdownTimer() {
        if (countdownTask != null) {
            countdownTask.cancel();
        }

        countdownTask = new CountdownTask(3, binding.tvCutTime);
        timer.schedule(countdownTask, 1000, 1000);
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
                weakText.get().setText(UIUtils.getString(R.string.skip) + "(" + mTime + "s)");
                weakText.get().setVisibility(View.VISIBLE);
                if (mTime < 1) {
                    cancel();
                    toMain();
                }
                mTime--;
            });
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucSkip.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (countdownTask != null) {
                    countdownTask.cancel();
                }
                toMain();
            }
        });
    }

    private void toMain() {
        if (LoginHelper.isLogin()) {
            MainActivity.start(activity);
        } else {
            if (LoginHelper.isIntro()) {
                IntroActivity.start(activity);
            } else {
                LoginActivity.start(activity);
            }


        }
        doSthIsExit();


    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected boolean isExit() {
        return false;
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }
}
