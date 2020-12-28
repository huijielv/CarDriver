package com.ymx.driver.viewmodel.driverinterral;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.InterralCommodityListItem;

import org.greenrobot.eventbus.EventBus;


public class InterralCommodityListItemViewModel  extends ItemViewModel<InterralViewModel> {
    public ObservableField<InterralCommodityListItem> entity = new ObservableField<>();

    public InterralCommodityListItemViewModel(@NonNull InterralViewModel viewModel , InterralCommodityListItem  entity ) {
        super(viewModel);
        this.entity.set(entity);
    }

    public BindingCommand show= new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (entity.get().getStocksNumber()<=0){

            }else {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_SHOW_CONFIRM_EXCHANGE_DIALOG_CODE, entity.get()));
            }

        }
    });
}
