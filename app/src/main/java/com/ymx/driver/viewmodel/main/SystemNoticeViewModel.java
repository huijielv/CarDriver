package com.ymx.driver.viewmodel.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;


import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.SystemNoticeEntity;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class SystemNoticeViewModel extends BaseViewModel {
    public SystemNoticeViewModel(@NonNull Application application) {
        super(application);
    }

    //给ViewPager添加ObservableList
    public ObservableList<SystemNoticeItemViewModel> items = new ObservableArrayList<>();
    //给ViewPager添加ItemBinding
    public ItemBinding<SystemNoticeItemViewModel> itemBinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.item_system_notice);

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucDismiss = new SingleLiveEvent<>();
        public SingleLiveEvent<SystemNoticeEntity> ucToSystemNoticeDetail = new SingleLiveEvent<>();
    }

    public BindingCommand dismiss = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucDismiss.call();
        }
    });
}
