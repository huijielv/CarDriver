package com.ymx.driver.viewmodel.main;

import com.ymx.driver.R;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.app.MsgEntity;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

/**
 * Created by xuweihua
 * 2020/5/26
 */
public class MsgItemViewModel extends ItemViewModel<MsgViewModel> {
    public ObservableField<MsgEntity> entity = new ObservableField<>();

    public MsgItemViewModel(@NonNull MsgViewModel viewModel, MsgEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }

    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (entity.get() == null) {
                return;
            }
            viewModel.uc.ucToMsgDetail.setValue(entity.get());
        }
    });

    public int getPlaceholderRes() {
        return R.drawable.icon_message_info;
    }
}
