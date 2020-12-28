package com.ymx.driver.ui.my.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityMyWalletBinding;
import com.ymx.driver.ui.travel.activity.TravelActivity;
import com.ymx.driver.viewmodel.mywallet.MyWalletViewModel;

import static com.ymx.driver.ui.my.wallet.MyWalletBalanceActivity.MY_WALLET_BANLANCE;

public class MyWalletActivity extends BaseActivity<ActivityMyWalletBinding, MyWalletViewModel> {


    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, MyWalletActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_my_wallet;
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
        viewModel.getWithdrawInfo();
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.ucGoMyWalletBanlance.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                Intent intent = new Intent();
                intent.putExtra(MY_WALLET_BANLANCE, viewModel.balacne.get());
                MyWalletBalanceActivity.start(activity, intent);
            }
        });

        viewModel.uc.ucGoMonthIncomeDetails.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                MonthIncomeDetailActivity.start(activity);
            }
        });

        viewModel.uc.ucHotleOrderGoMonthIncomeDetails .observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                Intent intent = new Intent();
                intent.putExtra(MonthIncomeDetailActivity.WALLET_TYPE, 1);
                MonthIncomeDetailActivity.start( activity ,intent);
            }
        });


    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }
}
