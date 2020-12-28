package com.ymx.driver.viewmodel.longrangdriving;

import android.app.Application;

import androidx.annotation.NonNull;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.config.MessageEvent;


public class LongRangeCirculationWaitingViewModel extends BaseViewModel {
    public LongRangeCirculationWaitingViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();
    public class UIChangeObservable {

        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();


    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_ORDER_CIRCULATION_FAILE_NOTICE:
                uc.ucBack.call();
                break;
            case MessageEvent.MSG_ORDER_CIRCULATION_SUCCESS_NOTICE:
                uc.ucBack.call();
                break;

        }
    }

}
