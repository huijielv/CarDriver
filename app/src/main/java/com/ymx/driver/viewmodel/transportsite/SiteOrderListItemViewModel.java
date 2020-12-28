package com.ymx.driver.viewmodel.transportsite;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.SiteOrderListItem;

import org.greenrobot.eventbus.EventBus;

public class SiteOrderListItemViewModel extends ItemViewModel<TranSportSiteViewModelUpdateStateViewModel> {
    public ObservableField<SiteOrderListItem> entity = new ObservableField<>();
    public SiteOrderListItemViewModel(@NonNull TranSportSiteViewModelUpdateStateViewModel viewModel ,SiteOrderListItem siteOrderListItem) {
        super(viewModel);
        this.entity.set(siteOrderListItem);
    }

    public BindingCommand goOrderActivity = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GO_TO_TRANSFER_SIDE_ORDER_CODE , entity.get().getSiteId()));
        }
    });


}
