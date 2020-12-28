package com.ymx.driver.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ymx.driver.R;
import com.ymx.driver.util.UIUtils;

public class GrabOrderView extends LinearLayout {
    private TextView startAddressTv;
    private TextView endAddressTv;
    private TextView orderTime;
    private TextView orderTypeTv;

    public GrabOrderView(Context context) {
        super(context);
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.grab_order_view, null);
        orderTime = myView.findViewById(R.id.orderTime);
        startAddressTv = myView.findViewById(R.id.grabOrderStartAddressTV);
        endAddressTv = myView.findViewById(R.id.grabOrdeEndAddressTV);
        orderTypeTv = myView.findViewById(R.id.orderType);
        addView(myView);
    }

    public void setStartAddress(String address) {
        startAddressTv.setText(address);
    }

    public void setEndAddress(String address) {
        endAddressTv.setText(address);
    }

    public void setOrderType(int orderType) {
        if (orderType == 1) {
            orderTypeTv.setText("好友司机实时单");
            orderTypeTv.setBackground(UIUtils.getDrawable(R.drawable.bg_order_type_2));

        } else if (orderType == 2) {
            orderTypeTv.setText("好友司机预约单");
            orderTypeTv.setBackground(UIUtils.getDrawable(R.drawable.bg_order_type));
        }

    }

    public void setOrderTime(String time) {
        orderTime.setText(time);
    }

}
