package com.ymx.driver.ui.trip.activity.frament;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseFragment;
import com.ymx.driver.databinding.FramentNotFinishTripBinding;
import com.ymx.driver.viewmodel.triporderlist.NotFinishTripViewModel;

//待优化同名ViewModel
public class NoFinishTripOrderFrament extends BaseFragment<FramentNotFinishTripBinding, NotFinishTripViewModel> {
    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.frament_not_finish_trip;
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

        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.pagerIndex.set(1);
                viewModel.itemList.clear();
                viewModel.refreshOrder(1, 10, viewModel.pagerIndex.get());
            }
        });

        binding.refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                viewModel.pagerIndex.set(viewModel.pagerIndex.get() + 1);
                viewModel.getTripOrderList(1, 10, viewModel.pagerIndex.get());

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void initData() {


        viewModel.pagerIndex.set(1);
        viewModel.itemList.clear();
        viewModel.refreshOrder(1, 10, viewModel.pagerIndex.get());
    }

    @Override
    public void initViewObservable() {


        viewModel.uc.ucCanLoadmore.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.refreshLayout.setEnableLoadMore(aBoolean);
            }
        });

        viewModel.uc.ucRefreshOrder.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.refreshLayout.finishRefresh();
            }
        });

        viewModel.uc.ucTripRefreshData.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                viewModel.pagerIndex.set(1);
                viewModel.itemList.clear();
                viewModel.refreshOrder(1, 10, viewModel.pagerIndex.get());
            }
        });

    }
}
