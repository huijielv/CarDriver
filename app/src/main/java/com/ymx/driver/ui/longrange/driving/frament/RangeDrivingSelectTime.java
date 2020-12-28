package com.ymx.driver.ui.longrange.driving.frament;

import android.os.Bundle;
import android.text.TextUtils;
import androidx.lifecycle.Observer;
import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseDialogFragment;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.FramentRangeDrivingSelecttimeBinding;
import com.ymx.driver.entity.app.SelectRangeDrivingConFirmEntity;
import com.ymx.driver.ui.mine.Frament.TimePickDialogFragment;
import com.ymx.driver.viewmodel.longrangdriving.frament.SelectRangeDrivingTimeViewModel;

import org.greenrobot.eventbus.EventBus;

public class RangeDrivingSelectTime extends BaseDialogFragment<FramentRangeDrivingSelecttimeBinding, SelectRangeDrivingTimeViewModel> {
    private String startTime;
    private String endTime;

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.frament_range_driving_selecttime;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
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
            startTime = bundle.getString(TimePickDialogFragment.SELECT_START_TIME);
            endTime = bundle.getString(TimePickDialogFragment.SELECT_END_TIME);

        }

    }

    @Override
    public void initViewObservable() {


        viewModel.uc.ucStartSelectTime.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {


                Bundle bundle = new Bundle();
                bundle.putString(TimePickDialogFragment.SELECT_START_TIME,
                        startTime);
                bundle.putString(TimePickDialogFragment.SELECT_END_TIME,
                        endTime);
                bundle.putInt(TimePickDialogFragment.SELECT_TIME_TYPE,
                        0);
                TimePickDialogFragment.newInstance(bundle).show(getChildFragmentManager(), TimePickDialogFragment.class.getName());


            }
        });

        viewModel.uc.ucEndSelectTime.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                Bundle bundle = new Bundle();
                if(!TextUtils.isEmpty(viewModel.startTime.get())){
                    bundle.putString(TimePickDialogFragment.SELECT_START_TIME,
                            viewModel.startTime.get());
                }else {
                    bundle.putString(TimePickDialogFragment.SELECT_START_TIME,
                            startTime);
                }


                bundle.putString(TimePickDialogFragment.SELECT_END_TIME,
                        endTime);
                bundle.putInt(TimePickDialogFragment.SELECT_TIME_TYPE,
                        1);
                TimePickDialogFragment.newInstance(bundle).show(getChildFragmentManager(), TimePickDialogFragment.class.getName());

            }
        });


        viewModel.uc.ucClose.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                dismiss();

            }
        });


        viewModel.uc.ucConfirm.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                dismiss();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_SELECT_RANGGE_DRIVING_CONFIRM_CODE ,new SelectRangeDrivingConFirmEntity(viewModel.startTime.get() ,viewModel.endTime.get()) ) );
            }
        });

    }

    public static RangeDrivingSelectTime newInstance() {
        return newInstance(null);
    }

    public static RangeDrivingSelectTime newInstance(Bundle bundle) {
        RangeDrivingSelectTime fragment = new RangeDrivingSelectTime();
        fragment.setArguments(bundle);
        return fragment;
    }


}
