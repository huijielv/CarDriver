package com.ymx.driver.ui.longrange.driving.frament;


import android.os.Bundle;
import android.text.TextUtils;
import androidx.lifecycle.Observer;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseDialogFragment;
import com.ymx.driver.databinding.FramentLongRangeConfirmPriceBinding;
import com.ymx.driver.entity.app.PassengerItemInfo;
import com.ymx.driver.viewmodel.longrangdriving.LongRangePremiumViewmodel;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class LongRangeConfirmFrament extends BaseDialogFragment<FramentLongRangeConfirmPriceBinding, LongRangePremiumViewmodel> {

    public static final String PASSENGER_INFO = "passenger_info";
    public static final String PASSENGER_PRICE = "passenger_price";
    private int rideNumber;
    private BigDecimal totlePrice;

    public static LongRangeConfirmFrament newInstance() {
        return newInstance(null);
    }

    public static LongRangeConfirmFrament newInstance(Bundle bundle) {
        LongRangeConfirmFrament fragment = new LongRangeConfirmFrament();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.frament_long_range_confirm_price;
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
            PassengerItemInfo passengerItemInfo = (PassengerItemInfo) bundle.getSerializable(PASSENGER_INFO);
            viewModel.passenger.set(passengerItemInfo);


            rideNumber = Integer.parseInt(passengerItemInfo.getRideNumber());
            StringBuffer priceSb = new StringBuffer();

            DecimalFormat format = new DecimalFormat("0.00");
            String formatMoney = format.format(new BigDecimal(bundle.getString(PASSENGER_PRICE)));
            BigDecimal peoplePrice = new BigDecimal(formatMoney);
            totlePrice = peoplePrice.multiply(new BigDecimal(rideNumber));



            priceSb.append("乘车人数： ").append(passengerItemInfo.getRideNumber() + "人 ").append(formatMoney + " 元/人").append(" 总价钱 " + totlePrice.toString() + "元");
            viewModel.priceInfo.set(priceSb.toString());


            if (!TextUtils.isEmpty(passengerItemInfo.getPhone())) {
                StringBuffer sb = new StringBuffer();
                sb.append("尾号");
                sb.append(passengerItemInfo.getPhone().substring(7));
                viewModel.passengerInfo.set(sb.toString());
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
                viewModel.confirmationPrice(viewModel.passenger.get().getOrderNo(), totlePrice.toString());

            }
        });

        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                dismiss();
            }
        });


    }
}


