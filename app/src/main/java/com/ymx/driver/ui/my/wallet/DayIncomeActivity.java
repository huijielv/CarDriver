package com.ymx.driver.ui.my.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityDayIncomeBinding;
import com.ymx.driver.viewmodel.mywallet.DayIncomeViewModel;

public class DayIncomeActivity extends BaseActivity<ActivityDayIncomeBinding, DayIncomeViewModel> {

    public static final String DAY = "day";
    public static final String WALLET_TYPE = "TYPE";

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, DayIncomeActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_day_income;
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

        if (getIntent().hasExtra(WALLET_TYPE)){
            viewModel.title.set("每日月付订单明细");
            viewModel.incomeType.set(1);
        }else {
            viewModel.title.set("钱包");
        }

        if (intent.hasExtra(DAY)) {
            String day = intent.getStringExtra(DAY);
            viewModel.loadDay.set(day);
            viewModel.getDayIncomeDetail(day, 10, 1,viewModel.incomeType.get());
        }



    }

    @Override
    public void initViewObservable() {
        binding.refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                viewModel.pagerIndex.set(viewModel.pagerIndex.get() + 1);
                viewModel.getDayIncomeDetail(viewModel.loadDay.get(), 10, viewModel.pagerIndex.get(),viewModel.incomeType.get());
            }
        });

        viewModel.uc.ucCanLoadmore.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.refreshLayout.setEnableLoadMore(aBoolean);
            }
        });
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
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
