package com.ymx.driver.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymx.driver.R;

public class ConfirmWithdrawalView extends LinearLayout {
    private TextView withdrawalPrice;
    private TextView proceduresPrice;
    private TextView toAccountPrice;

    public ConfirmWithdrawalView(Context context) {
        super(context);
        LayoutInflater mInflater = LayoutInflater.from(context);

        View myView = mInflater.inflate(R.layout.dialog_confirm_withdrawal, null);
        withdrawalPrice = myView.findViewById(R.id.withdrawalPrice);
        proceduresPrice = myView.findViewById(R.id.proceduresPrice);
        toAccountPrice = myView.findViewById(R.id.toAccountPrice);
        addView(myView);

    }

    public void setWithdrawalPrice(String price) {
        withdrawalPrice.setText(price);
    }

    public void setProceduresPrice(String price) {
        proceduresPrice.setText(price);
    }

    public void setToAccountPrice(String price) {
        toAccountPrice.setText(price);
    }


}
