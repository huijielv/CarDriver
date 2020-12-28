package com.ymx.driver.viewmodel.orderdetails;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.OrderDetailsEntity;
import com.ymx.driver.entity.app.UpdateOrderStatusEntity;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.entity.app.mqtt.PhoneOrderSuccessEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.util.VoicePlayMannager;
import com.ymx.driver.viewmodel.travel.TravelViewModel;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PhoneOrderPayViewModel extends BaseViewModel {
    public ObservableField<String> headPicUrl = new ObservableField<>();
    public ObservableField<String> totalFee = new ObservableField<>();
    public ObservableField<Boolean> isCall = new ObservableField<>();
    public ObservableField<String> startAddress = new ObservableField<>();
    public ObservableField<String> endAddress = new ObservableField<>();
    public ObservableField<String> orderId = new ObservableField<>();
    public ObservableField<String> passengerCall = new ObservableField<>();
    public ObservableField<String> passengerInfo = new ObservableField<>();
    public ObservableField<Integer> paySuccess = new ObservableField<>();
    public ObservableField<Integer> orderStatus= new ObservableField<>();
    public ObservableField<Boolean> buttonVisity= new ObservableField<>();


    public PhoneOrderPayViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucCallPhone = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> back = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucLoadImage = new SingleLiveEvent<>();
        public SingleLiveEvent<PhoneOrderSuccessEntity> ucPhoneOrderSuccess = new SingleLiveEvent<PhoneOrderSuccessEntity>();
        public SingleLiveEvent<Void> ucShowPayDialog = new SingleLiveEvent<>();
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_PHONE_ORDER_PAY_SUCCESS:
                PhoneOrderSuccessEntity phoneOrderSuccessEntity = (PhoneOrderSuccessEntity) event.src;
                uc.ucPhoneOrderSuccess.setValue(phoneOrderSuccessEntity);
                break;

        }
    }


    public BindingCommand callPhone = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isCall.set(false);
            uc.ucCallPhone.call();
        }
    });
    public BindingCommand ucPassengerCall = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isCall.set(true);
            uc.ucCallPhone.call();
        }
    });

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.back.call();
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

                        uc.ucLoadImage.setValue(s);

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    public void getOrderDetails(String orderNo) {
        RetrofitFactory.sApiService.getOrderDetails(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<OrderDetailsEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(OrderDetailsEntity orderDetailsEntity) {
                        startAddress.set(orderDetailsEntity.getStartAddress());
                        endAddress.set(orderDetailsEntity.getEndAddress());
                        orderStatus.set(orderDetailsEntity.getOrderState());
                        orderId.set(orderDetailsEntity.getOrderNo());
                        headPicUrl.set(orderDetailsEntity.getHeadPicUrl());
                        if (orderDetailsEntity.getOrderState()== TravelViewModel.DRIVER_STATE_CONFIRM_COST) {
                            totalFee.set(orderDetailsEntity.getTotalFee());
                        }else  if ( orderDetailsEntity.getOrderState()== TravelViewModel.DRIVER_STATE_TO_PAY ) {
                            totalFee.set(orderDetailsEntity.getUnPayFee());
                        }

                        passengerCall.set(orderDetailsEntity.getMobile());
                        if (!TextUtils.isEmpty(orderDetailsEntity.getMobile())) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("尾号");
                            sb.append(orderDetailsEntity.getMobile().substring(7));
                            passengerInfo.set(sb.toString());
                        }


                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    public void updateOrderDetailsStatus(String orderNo, String currentCoord, String actionType, String otherCharge) {
        RetrofitFactory.sApiService.updateOrderDetailsStatus(orderNo, currentCoord, actionType, otherCharge)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<UpdateOrderStatusEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(UpdateOrderStatusEntity updateOrderStatusEntity) {


                        uc.back.call();

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

}
