package com.ymx.driver.ui.my.wallet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityWithdrawalProgressBinding;
import com.ymx.driver.viewmodel.mywallet.WithdrawalProgressViewModel;

public class WithdrawalProgressActivity extends BaseActivity<ActivityWithdrawalProgressBinding, WithdrawalProgressViewModel> {
    public static final String ID = "id";

    public static void start(Activity activity) {
        start(activity, null);
    }


    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, WithdrawalProgressActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_withdrawal_progress;
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

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(ID)) {
            int id = intent.getIntExtra(ID, 0);
            viewModel.getWithdrawListInfo(id);
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }
}
