package com.ymx.driver.viewmodel.main;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.WechatPayEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayViewModel extends BaseViewModel {


    public PayViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<Integer> payType = new ObservableField<>(0);
    public ObservableField<String> orderNo = new ObservableField<>();
    public ObservableField<String> totalPrice = new ObservableField<>();

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucDismiss = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucGetAliPayOrderInfoSuccess = new SingleLiveEvent<>();
        public SingleLiveEvent<WechatPayEntity> ucGetWechatPayOrderInfoSuccess = new SingleLiveEvent<>();
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_QUERY_PAY_RESULT:
                uc.ucDismiss.call();
                break;
            case MessageEvent.MSG_PHONE_ORDER_PAY_SUCCESS:
                uc.ucDismiss.call();
                break;
        }
    }

    public BindingCommand payTypeWechat = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            payType.set(0);
        }
    });

    public BindingCommand payTypeAli = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            payType.set(1);
        }
    });

    public BindingCommand payConfirm = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(orderNo.get())) {
                return;
            }
            if (payType.get() == 0) {
                getWechatPayOrderInfo(0, orderNo.get(), 2);
            } else if (payType.get() == 1) {
                getAliPayOrderInfo(orderNo.get(), 2);
            }
        }
    });

    public void getWechatPayOrderInfo(int wxPayType, String orderNo, int clientType) {
        RetrofitFactory.sApiService.getWechatPayOrderInfo(wxPayType, orderNo, clientType)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<WechatPayEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(WechatPayEntity entity) {
                        uc.ucGetWechatPayOrderInfoSuccess.setValue(entity);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    public void getAliPayOrderInfo(String orderNo, int clientType) {
        RetrofitFactory.sApiService.getAliPayOrderInfo(orderNo, clientType)
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
                        uc.ucGetAliPayOrderInfoSuccess.setValue(s);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }
}
