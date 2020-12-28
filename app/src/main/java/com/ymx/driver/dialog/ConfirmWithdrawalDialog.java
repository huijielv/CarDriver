package com.ymx.driver.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.ymx.driver.base.DefaultStyleDialog;

import com.ymx.driver.view.ConfirmWithdrawalView;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class ConfirmWithdrawalDialog extends DefaultStyleDialog {

    private String money;
    private String withdrawFee;

    public ConfirmWithdrawalDialog(@NonNull Context context) {
        super(context);
    }

    public ConfirmWithdrawalDialog(@NonNull Context context, String money, String withdrawFee) {
        super(context);
        this.money = money;
        this.withdrawFee = withdrawFee;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfirmWithdrawalView confirmWithdrawalView = new ConfirmWithdrawalView(getContext());
        binding.titleLl.setVisibility(View.GONE);
        binding.contentLl.addView(confirmWithdrawalView);
        DecimalFormat format = new DecimalFormat("0.00");
        String formatMoney = format.format(new BigDecimal(money));
        confirmWithdrawalView.setWithdrawalPrice(formatMoney);
        BigDecimal drawcashRule = new BigDecimal(money);

        BigDecimal withdrawFee1 = drawcashRule == null ? BigDecimal.ZERO : drawcashRule.multiply(new BigDecimal(withdrawFee)).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);


        confirmWithdrawalView.setProceduresPrice(String.valueOf(withdrawFee1));
        confirmWithdrawalView.setToAccountPrice(String.valueOf(drawcashRule.subtract(withdrawFee1)));

//        confirmWithdrawalView.setToAccountPrice(String.valueOf(ArithUtil.sub(money, withdrawFee)));


    }
}
