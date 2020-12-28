package com.ymx.driver.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.view.GrabOrderView;

public class GrapOrderDailog extends DefaultStyleDialog {

    private PassengerInfoEntity passengerInfoEntity;

    public GrapOrderDailog(@NonNull Context context) {
        super(context);
    }

    public GrapOrderDailog(@NonNull Context context, PassengerInfoEntity passengerInfoEntity) {
        super(context);
        this.passengerInfoEntity = passengerInfoEntity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GrabOrderView grabOrderView = new GrabOrderView(getContext());
        binding.titleLl.setVisibility(View.GONE);
        binding.contentLl.addView(grabOrderView);
        if (passengerInfoEntity != null) {
            grabOrderView.setStartAddress(passengerInfoEntity.getSrcName());
            grabOrderView.setEndAddress(passengerInfoEntity.getDesName());
            grabOrderView.setOrderTime(passengerInfoEntity.getAppointmentTime());
            grabOrderView.setOrderType(passengerInfoEntity.getOrderType());
        }

    }


}
