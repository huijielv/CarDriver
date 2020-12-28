package com.ymx.driver.ui.integral;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseFragment;
import com.ymx.driver.databinding.FramentMyIntergralOrderListBinding;
import com.ymx.driver.viewmodel.driverinterral.MyIntegralOrderListViewModel;

public class MyIntegralOrderListFrament extends BaseFragment<FramentMyIntergralOrderListBinding, MyIntegralOrderListViewModel> {
    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.frament_my_intergral_order_list;
    }

    @Override
    public int initVariableId() {
        return com.ymx.driver.BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void onStart() {
        super.onStart();
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .statusBarColor(R.color.white)
                .init();
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.pagerIndex.set(1);
                viewModel.refresh.set(true);
                viewModel.itemList.clear();
                viewModel.getIntegralExchangeList();
            }
        });

        binding.refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                refreshLayout.finishLoadMore(1000);
                viewModel.pagerIndex.set(viewModel.pagerIndex.get() + 1);
                viewModel.getIntegralExchangeList();

            }
        });
    }

    @Override
    public void initData() {
        viewModel.getIntegralExchangeList();
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucRefresh.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                binding.refreshLayout.finishRefresh();

            }
        });
    }
}
