package com.ymx.driver.viewmodel.launch;

import android.app.Application;

import androidx.annotation.NonNull;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;

/**
 * Created by wuwei
 * 2020/4/21
 * 佛祖保佑       永无BUG
 */
public class AdvertisingViewModel extends BaseViewModel {
    public AdvertisingViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucSkip = new SingleLiveEvent<>();
    }

    public BindingCommand skip = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucSkip.call();
        }
    });
}
