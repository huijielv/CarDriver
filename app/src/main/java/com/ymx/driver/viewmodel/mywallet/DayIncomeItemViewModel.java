package com.ymx.driver.viewmodel.mywallet;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.MonthIncomeDayItemEntity;

import org.greenrobot.eventbus.EventBus;


public class DayIncomeItemViewModel extends ItemViewModel<MonthIncomeDetailViewModel> {
    public ObservableField<MonthIncomeDayItemEntity> entity = new ObservableField<>();


    public DayIncomeItemViewModel(@NonNull MonthIncomeDetailViewModel viewModel, MonthIncomeDayItemEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }

    public BindingCommand go = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_MYMSG_DAY_INCOME_DETAILS_CODE, entity.get()));
        }
    });


}