package com.ymx.driver.ui.integral;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseFragment;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.FramentIntegralDetaiListBinding;
import com.ymx.driver.viewmodel.driverinterral.IntegralDetailListViewModel;

import org.greenrobot.eventbus.EventBus;

public class IntegralDetailListFrament extends BaseFragment<FramentIntegralDetaiListBinding, IntegralDetailListViewModel> {
    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.frament_integral_detai_list;
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
                viewModel.getIntegralDetailList();
            }
        });

        binding.refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                viewModel.pagerIndex.set(viewModel.pagerIndex.get() + 1);
                viewModel.getIntegralDetailList();

            }
        });
    }

    @Override
    public void initData() {
        viewModel.getIntegralDetailList();
    }

    @Override
    public void initViewObservable() {


        viewModel.uc.ucCanLoadmore.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.refreshLayout.setEnableLoadMore(aBoolean);
            }
        });
        viewModel.uc.ucMyIntegral.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_INTEGRAL_BACK_CODE));

            }
        });
        viewModel.uc.ucRefresh.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                binding.refreshLayout.finishRefresh();

            }
        });
        viewModel.uc.ucShow.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                if (viewModel.more != null && viewModel.more.get() != null) {
                    if (viewModel.more.get()) {
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding. interralTv.setVisibility(View.GONE);
                    } else {
                        binding.emptyView.setVisibility(View.GONE);
                        binding. interralTv.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }
}
