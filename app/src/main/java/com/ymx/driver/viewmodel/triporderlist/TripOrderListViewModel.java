package com.ymx.driver.viewmodel.triporderlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.TripOrderListItem;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.util.LogUtil;

public class TripOrderListViewModel extends BaseViewModel {

    /**
     * 0-待确认
     */

    public static final int DRIVER_ORDER_N0_CONFIRN = 0;
    /**
     * 1-行程待开始
     */
    public static final int DRIVER_STATE_TO_START = 1;
    /**
     * 2-去接乘客
     */
    public static final int DRIVER_STATE_TO_PASSENGERS = 2;
    /**
     * 3-准备出发
     */
    public static final int DRIVER_STATE_READY_TO_GO = 3;
    /**
     * 4-行程中
     */
    public static final int DRIVER_STATE_ROADING = 4;
    /**
     * 5-确认费用
     */
    public static final int DRIVER_STATE_CONFIRM_COST = 5;
    /**
     * 6-待付款
     */
    public static final int DRIVER_STATE_TO_PAY = 6;

    /**
     * 7 已完成
     */
    public static final int DRIVER_ORDER_FINISH = 7;

    /**
     * 8 已关闭
     */
    public static final int DRIVER_ORDER_CLOSED = 8;


    public ObservableField<TripOrderListItem> tripOrderListItemField = new ObservableField<>();


    public TripOrderListViewModel(@NonNull Application application) {
        super(application);
    }


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> goTo = new SingleLiveEvent<>();


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
            case MessageEvent.MSG_GOTO_ORDER_ACTIVITY:
                TripOrderListItem tripOrderListItem = (TripOrderListItem) event.src;
                tripOrderListItemField.set(tripOrderListItem);
                uc.goTo.call();

                break;

        }
    }
}
