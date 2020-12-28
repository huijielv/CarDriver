package com.ymx.driver.viewmodel.main;


import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.MineActionEntity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wuwei
 * 2020/4/21
 * 佛祖保佑       永无BUG
 */
public class MineActionItemViewModel extends ItemViewModel<MineViewModel> {
    public ObservableField<MineActionEntity> entity = new ObservableField<>();

    public MineActionItemViewModel(@NonNull MineViewModel viewModel, MineActionEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }

    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {


            if (entity.get() == null || entity.get().getCls() == null) {
                return;
            }

            if (getPosition() == 3) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GO_TO_LONG_DRVING));
            } else if (getPosition() == 4) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GOTO_SCAN_CODE));
            } else if (getPosition() == 5) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GOTO_CHARTERCAR_CODE));
            } else if (getPosition() == 7) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_MINE_GOTO_TRANSFER_ORDER_CODE));

            } else {
                viewModel.uc.ucItemClick.setValue(entity.get().getCls());
            }


        }
    });


    public int getPosition() {
        return viewModel.getItemPosition(this);
    }


}
