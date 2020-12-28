package com.ymx.driver.viewmodel.longrangdriving;

import android.app.Application;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.LongRangCustomerPremiumStatus;
import com.ymx.driver.entity.app.PassengerItemInfo;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LongRangePremiumViewmodel extends BaseViewModel {
    public ObservableField<PassengerItemInfo> passenger = new ObservableField<>();
    public ObservableField<String> passengerInfo = new ObservableField<>();
    public ObservableField<String> priceInfo = new ObservableField<>();
    public ObservableField<Integer> status = new ObservableField<>(2);

    public LongRangePremiumViewmodel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {

        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCancal = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucConfirm = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucUpdate = new SingleLiveEvent<>();

    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    public TextWatcher withdrawalWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };

    public BindingCommand cancel = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucCancal.call();
        }
    });

    public BindingCommand confirm = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucConfirm.call();
        }
    });

    public BindingCommand update = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucUpdate.call();
        }
    });


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_ORDER_LONG_RANGE_COMFIRM_PRICE_CODE:
                uc.ucBack.call();
                break;

            case MessageEvent.MSG_ORDER_LONG_RANGE_CANCAL_PRICE_CODE:
                uc.ucBack.call();
                break;
            case  MessageEvent.MSG_ORDER_LONG_RANGE_CUSTOMER_TRIPS_CODE :
                LongRangCustomerPremiumStatus  longRangCustomerPremiumStatus =(LongRangCustomerPremiumStatus) event.src;

                status.set(longRangCustomerPremiumStatus.getState());

                break;

        }
    }


    public void confirmationPrice(String orderNo, String price) {
        RetrofitFactory.sApiService.confirmationPrice(orderNo, price)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<PassengerInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(PassengerInfoEntity passengerInfoEntity) {

                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_ORDER_LONG_RANGE_COMFIRM_PRICE_CODE, passengerInfoEntity));


                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public void canCalPrice(String orderNo) {
        RetrofitFactory.sApiService.cancelPrice(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<PassengerInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(PassengerInfoEntity passengerInfoEntity) {

                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_ORDER_LONG_RANGE_CANCAL_PRICE_CODE));


                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

}
