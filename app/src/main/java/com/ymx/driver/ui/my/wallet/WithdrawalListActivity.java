package com.ymx.driver.ui.my.wallet;

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
import com.ymx.driver.databinding.ActivityWithdrawalListBinding;
import com.ymx.driver.entity.app.WithdrawListModel;
import com.ymx.driver.viewmodel.mywallet.WithdrawalListViewModel;

public class WithdrawalListActivity extends BaseActivity<ActivityWithdrawalListBinding, WithdrawalListViewModel> {
    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, WithdrawalListActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_withdrawal_list;
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
        binding.refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                viewModel.pagerIndex.set(viewModel.pagerIndex.get() + 1);
                viewModel.getWithdrawListInfo(10, viewModel.pagerIndex.get());

            }
        });
    }

    @Override
    public void initData() {
        viewModel.itemList.clear();
        viewModel.pagerIndex.set(1);
        viewModel.getWithdrawListInfo(10, viewModel.pagerIndex.get());
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.ucWithdrawListModel.observe(this, new Observer<WithdrawListModel>() {
            @Override
            public void onChanged(WithdrawListModel withdrawListModel) {
                Intent intent = new Intent();
                intent.putExtra(WithdrawalProgressActivity.ID, withdrawListModel.getId());
                WithdrawalProgressActivity.start(activity, intent);
            }
        });

    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }
}
