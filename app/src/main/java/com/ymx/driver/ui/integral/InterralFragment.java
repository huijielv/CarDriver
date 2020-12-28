package com.ymx.driver.ui.integral;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseFragment;
import com.ymx.driver.databinding.FramentInterralBinding;
import com.ymx.driver.entity.app.InterralCommodityListItem;
import com.ymx.driver.viewmodel.driverinterral.InterralViewModel;


public class InterralFragment extends BaseFragment<FramentInterralBinding, InterralViewModel> {



    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.frament_interral;
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


    }

    @Override
    public void initView(Bundle savedInstanceState) {

        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.pagerIndex.set(1);
                viewModel.refresh.set(true);
                viewModel.itemList.clear();
                viewModel.getInterralCommodityList();
            }
        });

        binding.refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.refresh.set(false);
                refreshLayout.finishLoadMore(1000);
                viewModel.pagerIndex.set(viewModel.pagerIndex.get() + 1);
                viewModel.getInterralCommodityList();

            }
        });
    }

    @Override
    public void initData() {
        viewModel.getInterralCommodityList();
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucCanLoadmore.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.refreshLayout.setEnableLoadMore(aBoolean);
            }
        });
        viewModel.uc.ucGoIntegralDetailList.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                nav().navigate(R.id.action_integraldetails_fragment);

            }
        });
        viewModel.uc.ucGoMyOrder.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                nav().navigate(R.id.action_integral_order_fragment);

            }
        });

        viewModel.uc.ucShowDialog.observe(this, new Observer<InterralCommodityListItem>() {
            @Override
            public void onChanged(InterralCommodityListItem listItem) {

                Bundle bundle = new Bundle();
                bundle.putSerializable(ConfirmExchangeCommodityDialog.EXCHANGE_INFO, listItem);
                bundle.putString(ConfirmExchangeCommodityDialog.EXCHANGE_NUM, viewModel.integral.get());
                ConfirmExchangeCommodityDialog.newInstance(bundle).show(getChildFragmentManager(), ConfirmExchangeCommodityDialog.class.getSimpleName());

            }
        });

        viewModel.uc.ucRefresh.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                binding.refreshLayout.finishRefresh();

            }
        });
    }
}
