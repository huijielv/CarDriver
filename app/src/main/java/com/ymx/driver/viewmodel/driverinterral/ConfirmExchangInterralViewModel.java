package com.ymx.driver.viewmodel.driverinterral;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.ConfirmExchangeCommodityEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ConfirmExchangInterralViewModel extends BaseViewModel {


    public ObservableField<Integer> commodityId = new ObservableField<>();
    public ObservableField<String> commodityUrl = new ObservableField<>();
    public ObservableField<String> commodityName = new ObservableField<>();
    public ObservableField<String> exchangeIntegral = new ObservableField<>();
    public ObservableField<String> commodityDesc = new ObservableField<>();
    public ObservableField<String> integralDesc = new ObservableField<>();
    public ObservableField<Boolean> isConfirmShow = new ObservableField<>();

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {

        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCancal = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucConfirm = new SingleLiveEvent<>();


    }


    public ConfirmExchangInterralViewModel(@NonNull Application application) {
        super(application);
    }

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

    public void confirmExchangeCommodity(int commodityId) {
        RetrofitFactory.sApiService.confirmExchangeCommodity(commodityId)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<ConfirmExchangeCommodityEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(ConfirmExchangeCommodityEntity interralCommodityEntity) {
                        uc.ucCancal.call();
                        YmxCache.setDriverIntegral(interralCommodityEntity.getUsableIntegral());
                        UIUtils.showToast("兑换成功");
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_SHOW_CONFIRM_EXCHANGE_SUCCESS_CODE, interralCommodityEntity.getUsableIntegral()));
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

}
