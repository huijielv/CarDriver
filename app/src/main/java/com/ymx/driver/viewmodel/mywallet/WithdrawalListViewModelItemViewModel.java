package com.ymx.driver.viewmodel.mywallet;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.WithdrawListModel;
import com.ymx.driver.viewmodel.triporderlist.TripNotFinishOrderListItemViewModel;

import org.greenrobot.eventbus.EventBus;

public class WithdrawalListViewModelItemViewModel extends ItemViewModel<WithdrawalListViewModel> {
    public ObservableField<WithdrawListModel> entity = new ObservableField<>();

    public WithdrawalListViewModelItemViewModel(@NonNull WithdrawalListViewModel viewModel, WithdrawListModel entity) {
        super(viewModel);
        this.entity.set(entity);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> goTo = new SingleLiveEvent<>();

    }

    public BindingCommand go = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_WIDLLET_CODE, entity.get()));
        }
    });

}
