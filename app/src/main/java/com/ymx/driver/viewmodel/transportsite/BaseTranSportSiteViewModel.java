package com.ymx.driver.viewmodel.transportsite;

import android.app.Application;

import androidx.annotation.NonNull;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;

public class BaseTranSportSiteViewModel extends BaseViewModel {
    public BaseTranSportSiteViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> ucSelectType = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> ucRefreshOrder = new SingleLiveEvent<>();
    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            //
            case MessageEvent.MSG_SELECT_TYPE_CODE:
                int type = (int) event.src;
                uc.ucSelectType.setValue(type);
                break;
        }
    }
}
