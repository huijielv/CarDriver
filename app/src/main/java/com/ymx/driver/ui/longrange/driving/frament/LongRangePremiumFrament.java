package com.ymx.driver.ui.longrange.driving.frament;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import androidx.lifecycle.Observer;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseDialogFragment;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.databinding.FramentLongRangePremiumBinding;
import com.ymx.driver.entity.app.PassengerItemInfo;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.CashierInputFilter;
import com.ymx.driver.viewmodel.longrangdriving.LongRangePremiumViewmodel;


public class LongRangePremiumFrament extends BaseDialogFragment<FramentLongRangePremiumBinding, LongRangePremiumViewmodel> {

    public static final String PASSENGER_INFO = "passenger_info";

    public static LongRangePremiumFrament newInstance() {
        return newInstance(null);
    }

    public static LongRangePremiumFrament newInstance(Bundle bundle) {
        LongRangePremiumFrament fragment = new LongRangePremiumFrament();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.frament_long_range_premium;
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
        InputFilter[] filters = {new CashierInputFilter()};
        binding.amountEt.setFilters(filters);
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            PassengerItemInfo passengerItemInfo = (PassengerItemInfo) bundle.getSerializable(PASSENGER_INFO);
            viewModel.passenger.set(passengerItemInfo);

            if (!TextUtils.isEmpty(passengerItemInfo.getPhone())) {
                StringBuffer sb = new StringBuffer();
                sb.append("尾号");
                sb.append(passengerItemInfo.getPhone().substring(7));
                viewModel.passengerInfo.set(sb.toString());
            }
            binding.priceNum.setText("乘车人数：" +passengerItemInfo.getRideNumber()+"人");

            if (passengerItemInfo.getPriceMarkupState() == 2) {
                binding.amountEt.setText(passengerItemInfo.getPriceMarkup());
            }

        }


    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucCancal.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                if (viewModel.status.get() == 2 || viewModel.status.get() == 1) {
                    new DefaultStyleDialog(activity)
                            .setBody(
                                    "确定撤回么?")
                            .setNegativeText("取消")
                            .setPositiveText("确定")
                            .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                                @Override
                                public void negative(Dialog dialog) {
                                    dialog.dismiss();

                                }

                                @Override
                                public void positive(Dialog dialog) {
                                    dialog.dismiss();
                                    viewModel.canCalPrice(viewModel.passenger.get().getOrderNo());
                                }
                            }).show();
                } else {
                    UIUtils.showToast("乘客已确认加价");
                }


            }
        });
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                dismiss();
            }
        });
        viewModel.uc.ucConfirm.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {


                if (TextUtils.isEmpty(binding.amountEt.getText().toString())) {
                    UIUtils.showToast("请输入价格");
                    return;
                }

                if (viewModel.status.get() == 2 || viewModel.status.get() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(LongRangeConfirmFrament.PASSENGER_INFO, viewModel.passenger.get());
                    bundle.putString(LongRangeConfirmFrament.PASSENGER_PRICE, binding.amountEt.getText().toString());
                    LongRangeConfirmFrament.newInstance(bundle).show(getChildFragmentManager(),
                            LongRangeConfirmFrament.class.getSimpleName());
                } else {
                    UIUtils.showToast("乘客已确认加价");
                }


            }
        });

        viewModel.uc.ucUpdate.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (TextUtils.isEmpty(binding.amountEt.getText().toString())) {
                    UIUtils.showToast("请输入价格");
                    return;
                }

                if (viewModel.status.get() == 2 || viewModel.status.get() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(LongRangeConfirmFrament.PASSENGER_INFO, viewModel.passenger.get());
                    bundle.putString(LongRangeConfirmFrament.PASSENGER_PRICE, binding.amountEt.getText().toString());
                    LongRangeConfirmFrament.newInstance(bundle).show(getChildFragmentManager(),
                            LongRangeConfirmFrament.class.getSimpleName());
                } else {
                    UIUtils.showToast("乘客已确认加价");
                }
            }
        });


    }
}
