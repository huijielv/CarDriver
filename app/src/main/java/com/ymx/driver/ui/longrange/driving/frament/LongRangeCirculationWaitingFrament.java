package com.ymx.driver.ui.longrange.driving.frament;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseDialogFragment;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.FramentLongRangCirculationWaitBinding;

import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.longrangdriving.LongRangeCirculationWaitingViewModel;

import org.greenrobot.eventbus.EventBus;

public class LongRangeCirculationWaitingFrament extends BaseDialogFragment<FramentLongRangCirculationWaitBinding, LongRangeCirculationWaitingViewModel> {

    public static final String DRIVER_NAME = "driver_name";
    public static final String DRIVER_ID = "driver_id";
    public static final String DRIVER_TIME = "driver_time";
    private String driverName;
    private String driverId;
    private long driverTime;


    Runnable driverTimeRunnable = new Runnable() {
        @Override
        public void run() {

            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_ORDER_LONG_RANGE_RECOVER_ORDER_DETAILSCODE ));


            dismiss();
        }
    };


    public static LongRangeCirculationWaitingFrament newInstance() {
        return newInstance(null);
    }

    public static LongRangeCirculationWaitingFrament newInstance(Bundle bundle) {
        LongRangeCirculationWaitingFrament fragment = new LongRangeCirculationWaitingFrament();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.frament_long_rang_circulation_wait;
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
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().setCancelable(false);
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
        }
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();

            driverName = bundle.getString(DRIVER_NAME);
            driverId = bundle.getString(DRIVER_ID);
            driverTime = bundle.getLong(DRIVER_TIME);

            binding.driverName.setText(driverName);
            binding.driverId.setText(driverId);

        }

        UIUtils.postDelayTask(driverTimeRunnable
                , driverTime * 1000);
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                dismiss();


            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UIUtils.removeTask(driverTimeRunnable);
    }
}
