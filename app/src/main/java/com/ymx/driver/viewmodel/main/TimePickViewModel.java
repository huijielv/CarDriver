package com.ymx.driver.viewmodel.main;

import android.app.Application;

import androidx.annotation.NonNull;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;

public class TimePickViewModel  extends BaseViewModel {
    public TimePickViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucClose = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucConfirm = new SingleLiveEvent<>();
    }

    public BindingCommand close = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucClose.call();
        }
    });

    public BindingCommand confirm = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucConfirm.call();
        }
    });
}

