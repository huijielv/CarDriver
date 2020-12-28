package com.ymx.driver.ui.mine.head;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityTodayIncomeBinding;
import com.ymx.driver.ui.main.fragment.TodayIncomeSelectTimeFrament;
import com.ymx.driver.util.DateUtils;


import com.ymx.driver.viewmodel.mywallet.DayIncomeViewModel;


public class TodayIncomeActivity extends BaseActivity<ActivityTodayIncomeBinding, DayIncomeViewModel> {
    private TodayIncomeSelectTimeFrament selectTimeFrament;

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, TodayIncomeActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_today_income;
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
        viewModel.loadDay.set(DateUtils.getDate());
        viewModel.getDayIncomeDetail(viewModel.loadDay.get(), 10, viewModel.pagerIndex.get(),0);

    }

    @Override
    public void initViewObservable() {


        binding.refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                viewModel.pagerIndex.set(viewModel.pagerIndex.get() + 1);
                viewModel.getDayIncomeDetail(viewModel.loadDay.get(), 10, viewModel.pagerIndex.get(),0);
            }
        });

        viewModel.uc.ucCanLoadmore.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.refreshLayout.setEnableLoadMore(aBoolean);
            }
        });
        viewModel.uc.ucShow.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                selectTimeFrament = new TodayIncomeSelectTimeFrament();
                selectTimeFrament.show(getSupportFragmentManager(), "dialog");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (selectTimeFrament != null && selectTimeFrament.getDialog() != null) {
            if (selectTimeFrament.getDialog().isShowing()) {
                selectTimeFrament.dismiss();
            }
            selectTimeFrament.activity=null;
            selectTimeFrament = null;

        }

    }
}
