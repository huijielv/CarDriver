package com.ymx.driver.viewmodel.driverinterral;

import android.app.Application;

import androidx.annotation.NonNull;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.config.MessageEvent;

public class BaseInterralViewModel  extends BaseViewModel {
    public BaseInterralViewModel(@NonNull Application application) {
        super(application);
    }

    public  UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> close = new SingleLiveEvent<>();

    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_INTEGRAL_BACK_CODE:

                uc.close.call();
                break;

        }
    }

}
