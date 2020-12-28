package com.ymx.driver.ui.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityResetPwdBinding;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.mine.PwdResetViewModel;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xuweihua
 * 2020/5/9
 */
public class PasswordResetActivity extends BaseActivity<ActivityResetPwdBinding, PwdResetViewModel> {


    private Timer timer;
    private CountdownTask countdownTask;
    public static final int Default_Interval = 60;

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, PasswordResetActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_reset_pwd;
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

        binding.etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) {
                    return;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.phone.set(s.toString());
                viewModel.formatPhone.set(binding.etPhoneNum.getText().toString().length() == 11);
            }
        });

        binding.etSetPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) {
                    return;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.newWd.set(s.toString());
            }
        });

        binding.etConfirmPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) {
                    return;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.confirmWd.set(s.toString());
            }
        });


    }

    @Override
    public void initData() {
        timer = new Timer();

        UIUtils.postDelayTask(new Runnable() {
            @Override
            public void run() {

            }
        }, 200);
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.ucGetVerifyCodeSuccess.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                countdownTimer(Default_Interval);
            }
        });

        viewModel.uc.resetPassWordSuccess.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                if (LoginHelper.isLogin()) {
                    viewModel.driverLogout();
                } else {
                    LoginHelper.logout();
                }

            }
        });

    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

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
                mTime--;
                viewModel.formatPhone.set(false);
                weakText.get().setText(mTime + "s");
                if (mTime < 0) {
                    cancel();
                    viewModel.formatPhone.set(true);
                    weakText.get().setText(UIUtils.getString(R.string.verify_code));
                }
            });
        }
    }

    public void countdownTimer(int interval) {
        if (countdownTask != null) {
            countdownTask.cancel();
        }

        countdownTask = new CountdownTask(interval, binding.verifyCode);
        timer.schedule(countdownTask, 0, 1000);
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
}
