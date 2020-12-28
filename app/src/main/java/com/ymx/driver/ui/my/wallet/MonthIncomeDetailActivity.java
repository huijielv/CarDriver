package com.ymx.driver.ui.my.wallet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityMonthIncomedetailsBinding;
import com.ymx.driver.entity.app.MonthIncomeDayItemEntity;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.view.shadowlayout.ShadowLayout;
import com.ymx.driver.viewmodel.mywallet.MonthIncomeDetailViewModel;

public class MonthIncomeDetailActivity extends BaseActivity<ActivityMonthIncomedetailsBinding, MonthIncomeDetailViewModel> {
    public static final String WALLET_TYPE = "TYPE";


    private  int type =0;

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, MonthIncomeDetailActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_month_incomedetails;
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

        if (getIntent().hasExtra(WALLET_TYPE)) {
            viewModel.title.set("月付订单查询");
            type =  getIntent().getIntExtra(WALLET_TYPE,0) ;
            viewModel.incomeType.set(type);
        } else {
            viewModel.title.set("钱包");
        }
        viewModel.getMonthIncomeDetail("", "",type);



    }

    @Override
    public void initViewObservable() {
        viewModel.uc.back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });
        viewModel.uc.ucMonthIncomeDayItemEntity.observe(this, new Observer<MonthIncomeDayItemEntity>() {
            @Override
            public void onChanged(MonthIncomeDayItemEntity monthIncomeDayItemEntity) {
                Intent intent = new Intent();
                if (type==1) {
                    intent.putExtra(DayIncomeActivity.WALLET_TYPE, 1);
                }

                intent.putExtra(DayIncomeActivity.DAY, monthIncomeDayItemEntity.getDayTime());
                DayIncomeActivity.start(activity, intent);
            }
        });
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }
}
