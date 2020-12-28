package com.ymx.driver.viewmodel.mywallet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.WalletInfoEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyWalletBalanceViewModel extends BaseViewModel {

    public ObservableField<String> balacne = new ObservableField<>();
    public MyWalletBalanceViewModel(@NonNull Application application) {
        super(application);
    }


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {

        public SingleLiveEvent<Void> back = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> goWithdrawal = new SingleLiveEvent<>();

    }


    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.back.call();
        }
    });
    public BindingCommand goToWithdrawal = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           uc.goWithdrawal.call();
        }
    });




    public void getWithdrawInfo() {
        RetrofitFactory.sApiService.getWalletInfo()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<WalletInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(WalletInfoEntity walletInfoEntity) {
                        balacne.set(walletInfoEntity.getBalacne());

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

            case MessageEvent.MSG_WITHDRAWAL_DATA_CODE:
                getWithdrawInfo();

                break;

        }
    }

}
