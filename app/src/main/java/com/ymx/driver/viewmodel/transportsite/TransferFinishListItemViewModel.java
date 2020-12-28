package com.ymx.driver.viewmodel.transportsite;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.RangDriverPassgerFinishOederDetailsListItem;
import com.ymx.driver.viewmodel.longrangdriving.LongRangDrivingFinishViewModel;

import org.greenrobot.eventbus.EventBus;

public class TransferFinishListItemViewModel extends ItemViewModel<LongRangDrivingFinishViewModel> {

    public ObservableField<RangDriverPassgerFinishOederDetailsListItem> entity = new ObservableField();


    public TransferFinishListItemViewModel(@NonNull LongRangDrivingFinishViewModel viewModel, RangDriverPassgerFinishOederDetailsListItem item) {
        super(viewModel);
        this.entity.set(item);

    }

    public BindingCommand qCode = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_QUERY_LONG_DRIVIER_FINISH_DETAILS_PAY, entity.get().getOrderNo()));
        }
    });

}
