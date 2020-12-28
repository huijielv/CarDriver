package com.ymx.driver.viewmodel.transportsite;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.TransferStationTripOrderListItem;

import org.greenrobot.eventbus.EventBus;

public class TransferStationTripOrderListItemViewModel extends ItemViewModel<TransferStationTripOrderListViewModel> {
    public ObservableField<TransferStationTripOrderListItem> entity = new ObservableField<>();
    public ObservableField<Boolean> isShow = new ObservableField<>();

    public TransferStationTripOrderListItemViewModel(@NonNull TransferStationTripOrderListViewModel viewModel, TransferStationTripOrderListItem transferStationTripOrderListItem, boolean isShow) {
        super(viewModel);
        this.entity.set(transferStationTripOrderListItem);
        this.isShow.set(isShow);
    }
    public BindingCommand joinOrder = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent. MSG_GO_TO_TRANSFER_JOIN_ORDER_CODE, entity.get().getId()));
        }
    });

    public BindingCommand goToOrderTetails = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GOTO_TRANSFER_ORDER_DETAILS_CODE, entity.get().getOrderNo()));
        }
    });



}
