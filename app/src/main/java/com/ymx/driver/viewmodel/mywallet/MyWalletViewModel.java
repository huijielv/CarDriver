package com.ymx.driver.viewmodel.mywallet;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.R;
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

import static com.ymx.driver.util.UIUtils.getString;

public class MyWalletViewModel extends BaseViewModel {

    public ObservableField<String> balacne = new ObservableField<>();
    public ObservableField<String> monthIncome = new ObservableField<>();
    public ObservableField<String> weekIncome = new ObservableField<>();
    public ObservableField<String> withdrawDesc = new ObservableField<>();
    public ObservableField<String> withdrawTips = new ObservableField<>(getString((R.string.month_withdrawTips)));
    public ObservableField<Boolean> isClickWeek = new ObservableField<>(false);
    public ObservableField<Boolean> isClickMoth = new ObservableField<>(true);
    public ObservableField<String> income = new ObservableField<>();
    public ObservableField<Integer> monthPayOrderNumber = new ObservableField<>();
    public ObservableField<String>  monthPayOrderNumberDesc = new ObservableField<>();


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {

        public SingleLiveEvent<Void> back = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGoMyWalletBanlance = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGoMonthIncomeDetails = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucHotleOrderGoMonthIncomeDetails = new SingleLiveEvent<>();
    }


    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.back.call();
        }
    });

    public BindingCommand ucGoMyWalletBanlance = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucGoMyWalletBanlance.call();
        }
    });

    public BindingCommand goMonthIncome = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucGoMonthIncomeDetails.call();
        }
    });

    public BindingCommand goHotleOrderMonthIncome = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucHotleOrderGoMonthIncomeDetails .call();
        }
    });



    public BindingCommand clickWeek = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            withdrawTips.set(getString((R.string.week_withdrawTips)));
            isClickMoth.set(false);
            isClickWeek.set(true);

            if (weekIncome.get() != null) {
                income.set(weekIncome.get());
            }


        }
    });

    public BindingCommand clickMonth = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            withdrawTips.set(getString((R.string.month_withdrawTips)));
            isClickMoth.set(true);
            isClickWeek.set(false);
            if (monthIncome.get() != null) {
                income.set(monthIncome.get());
            }
        }
    });


    public MyWalletViewModel(@NonNull Application application) {
        super(application);
    }


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
                        monthIncome.set(walletInfoEntity.getMonthIncome());
                        weekIncome.set(walletInfoEntity.getWeekIncome());
                        withdrawDesc.set(walletInfoEntity.getWithdrawDesc());
                        if(!TextUtils.isEmpty(walletInfoEntity.getMonthPayOrderNumber())){
                            monthPayOrderNumber.set(Integer.parseInt(walletInfoEntity.getMonthPayOrderNumber()));
                        }

                        monthPayOrderNumberDesc.set(walletInfoEntity.getMonthPayOrderNumberDesc());
                        income.set(walletInfoEntity.getMonthIncome());
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
