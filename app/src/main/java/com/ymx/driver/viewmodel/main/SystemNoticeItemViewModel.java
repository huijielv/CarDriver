package com.ymx.driver.viewmodel.main;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.SystemNoticeEntity;

public class SystemNoticeItemViewModel  extends ItemViewModel<SystemNoticeViewModel> {
    public ObservableField<SystemNoticeEntity> entity = new ObservableField<>();

    public SystemNoticeItemViewModel(@NonNull SystemNoticeViewModel viewModel, SystemNoticeEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }

    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (entity.get() == null || entity.get().getSkipType() == 0
                    || TextUtils.isEmpty(entity.get().getHrefUrl())) {
                return;
            }
            viewModel.uc.ucToSystemNoticeDetail.setValue(entity.get());
        }
    });
}

