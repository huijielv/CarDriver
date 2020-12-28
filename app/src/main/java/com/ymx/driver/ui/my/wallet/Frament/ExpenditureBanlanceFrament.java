package com.ymx.driver.ui.my.wallet.Frament;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseFragment;
import com.ymx.driver.databinding.FramentWalletBalanceBinding;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.viewmodel.mywallet.WalletBalanceFramentViewModel;

public class ExpenditureBanlanceFrament  extends BaseFragment<FramentWalletBalanceBinding, WalletBalanceFramentViewModel> {

    public static ExpenditureBanlanceFrament newInstance() {
        return newInstance(null);
    }

    public static ExpenditureBanlanceFrament newInstance(Bundle bundle) {
        ExpenditureBanlanceFrament fragment = new ExpenditureBanlanceFrament();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.frament_wallet_balance;
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
        binding.refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                viewModel.pagerIndex.set(viewModel.pagerIndex.get() + 1);
                viewModel.getIncomeDetailList(String.valueOf(2), 10, viewModel.pagerIndex.get());

            }
        });
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void initData() {

        viewModel.itemList.clear();
        viewModel.pagerIndex.set(1);
        viewModel.getIncomeDetailList(String.valueOf(2), 10, viewModel.pagerIndex.get());
    }

    @Override
    public void initViewObservable() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
