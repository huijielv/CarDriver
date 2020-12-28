package com.ymx.driver.viewmodel.longrangdriving;

import android.app.Application;

import androidx.annotation.NonNull;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.LongDrivingPaySuccessEntigy;
import com.ymx.driver.entity.app.mqtt.LongDriverSysTemCancalOrder;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.finishorderdetails.TripOrderDetailsFinshViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RangeDrivingPayMoneyQrcodeViewModel extends BaseViewModel {

    public RangeDrivingPayMoneyQrcodeViewModel(@NonNull Application application) {
        super(application);
    }


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {

        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucLoadQcode = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucShowPayDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucPaySuccess = new SingleLiveEvent<>();
    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    public BindingCommand ucPay = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucShowPayDialog.call();
        }
    });


    public void orderPay(String orderNo) {
        RetrofitFactory.sApiService.orderPay(1, orderNo,2)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<String>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(String s) {
//                        payOrderInfo.set(s);
                        uc.ucLoadQcode.setValue(s);


                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {


            case MessageEvent.MSG_QUERY_LONG_DRIVIER_PAY_SUCCESS:

                LongDrivingPaySuccessEntigy paySuccessEntigy = (LongDrivingPaySuccessEntigy) event.src;

                uc.ucPaySuccess.call();
                break;


        }
    }
}
