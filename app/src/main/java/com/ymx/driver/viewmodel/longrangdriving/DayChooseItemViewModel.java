package com.ymx.driver.viewmodel.longrangdriving;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;

import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.RangDrivingDayChoose;

import org.greenrobot.eventbus.EventBus;

public class DayChooseItemViewModel extends ItemViewModel<LongRangeDrivingViewModel> {

    public ObservableField<RangDrivingDayChoose> rangDrivingDayChoose = new ObservableField();


    public DayChooseItemViewModel(@NonNull LongRangeDrivingViewModel viewModel, RangDrivingDayChoose rangDrivingDayChoose) {
        super(viewModel);
        this.rangDrivingDayChoose.set(rangDrivingDayChoose);
    }


    public BindingCommand selectDay = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_SELECT_RANGGE_DRIVING_DAY_CONFIRM_CODE, rangDrivingDayChoose.get()));
        }
    });
}
