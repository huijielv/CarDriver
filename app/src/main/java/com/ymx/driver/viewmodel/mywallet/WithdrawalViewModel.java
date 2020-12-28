package com.ymx.driver.viewmodel.mywallet;

import android.app.Application;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.app.ConfirmWithdrawalEntity;
import com.ymx.driver.entity.app.WithdrawInfoEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WithdrawalViewModel extends BaseViewModel {

    public ObservableField<String> bankCardNumber = new ObservableField<>();
    public ObservableField<String> belongBank = new ObservableField<>();
    public ObservableField<Double> balance = new ObservableField<>();
    public ObservableField<String> withdrawFee = new ObservableField<>();
    public ObservableField<String> cardholder = new ObservableField<>();
    public ObservableField<Boolean> withdrawalState = new ObservableField<>();
    public ObservableField<String> tips = new ObservableField<>();
    public ObservableField<String> moneyLimit = new ObservableField<>();

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {

        public SingleLiveEvent<Void> back = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucAllMoney = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucConfirmWithdrawal = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucConfirmWithdrawalSuccess = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGo = new SingleLiveEvent<>();
    }


    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.back.call();
        }
    });

    public BindingCommand allMoney = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucAllMoney.call();
        }
    });

    public BindingCommand confirmWithdrawal = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucConfirmWithdrawal.call();
        }
    });

    public BindingCommand goTo = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucGo .call();
        }
    });


    public WithdrawalViewModel(@NonNull Application application) {
        super(application);
    }


    public void getWithdrawInfo() {
        RetrofitFactory.sApiService.getWithdrawInfo()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<WithdrawInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(WithdrawInfoEntity walletInfoEntity) {
                        bankCardNumber.set(walletInfoEntity.getBankCardNumber());
                        belongBank.set(walletInfoEntity.getBankName());
                        balance.set(walletInfoEntity.getBalance());
                        withdrawFee.set(walletInfoEntity.getWithdrawFee());
                        cardholder.set(walletInfoEntity.getCardholder());
                        withdrawalState.set(walletInfoEntity.isWithdrawalState());
                        tips.set(walletInfoEntity.getTips());


                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    public TextWatcher withdrawalWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!TextUtils.isEmpty(s)&&balance.get()!=null) {

                if (Double.parseDouble(s.toString()) > balance.get()) {
                    moneyLimit.set("输入金额超过本次可提现上限");
                } else if (Double.parseDouble(s.toString()) <= balance.get()) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("本次可提现");
                    sb.append(s.toString());
                    sb.append("元");
                    moneyLimit.set(sb.toString());
                }
            } else {
                moneyLimit.set("");
            }


        }
    };

    public void confirmWithdrawal(String money) {
        RetrofitFactory.sApiService.confirmWithdrawal(money)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<ConfirmWithdrawalEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(ConfirmWithdrawalEntity confirmWithdrawalEntity) {
                        if (confirmWithdrawalEntity.getNumber() == 0) {
                            withdrawalState.set(false);
                        } else if (confirmWithdrawalEntity.getNumber() > 0) {
                            withdrawalState.set(true);
                        }
                        uc.ucConfirmWithdrawalSuccess.call();

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


}
