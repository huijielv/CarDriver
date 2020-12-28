package com.ymx.driver.ui.integral;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;

import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseDialogFragment;
import com.ymx.driver.databinding.FramentConfirmExchangInterralBinding;
import com.ymx.driver.entity.app.InterralCommodityListItem;
import com.ymx.driver.viewmodel.driverinterral.ConfirmExchangInterralViewModel;

public class ConfirmExchangeCommodityDialog extends BaseDialogFragment<FramentConfirmExchangInterralBinding, ConfirmExchangInterralViewModel> {

    public static final String EXCHANGE_INFO = "exchange_info";
    public static final String EXCHANGE_NUM = "exchange_num";


    public static ConfirmExchangeCommodityDialog newInstance() {
        return newInstance(null);
    }

    public static ConfirmExchangeCommodityDialog newInstance(Bundle bundle) {
        ConfirmExchangeCommodityDialog fragment = new ConfirmExchangeCommodityDialog();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.frament_confirm_exchang_interral;
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
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            InterralCommodityListItem interralCommodityListItem = (InterralCommodityListItem) bundle.getSerializable(EXCHANGE_INFO);
            viewModel.commodityDesc.set(interralCommodityListItem.getCommodityDesc());
            viewModel.commodityUrl.set(interralCommodityListItem.getCommodityUrl());
            viewModel.commodityId.set(interralCommodityListItem.getCommodityId());
            viewModel.commodityName.set(interralCommodityListItem.getCommodityName());
            viewModel.exchangeIntegral.set(interralCommodityListItem.getExchangeIntegral());
            if (!TextUtils.isEmpty(bundle.getString(EXCHANGE_NUM)) && !TextUtils.isEmpty(interralCommodityListItem.getExchangeIntegral())) {

                viewModel.isConfirmShow.set((Integer.parseInt(bundle.getString(EXCHANGE_NUM)) < Integer.parseInt(interralCommodityListItem.getExchangeIntegral()) ? true : false));
                viewModel.integralDesc.set(viewModel.isConfirmShow.get() ? "您的积分不足" : "确定兑换吗?");
            }

        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucCancal.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                dismiss();


            }
        });

        viewModel.uc.ucConfirm.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                viewModel.confirmExchangeCommodity(viewModel.commodityId.get());

            }
        });
    }

    @Override
    protected int setGravity() {
        return Gravity.BOTTOM;
    }
}
