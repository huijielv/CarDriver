package com.ymx.driver.viewmodel.mine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.app.OrderCirculationEntity;
import com.ymx.driver.entity.app.OrderCirculationNoticeEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ScanViewModel extends BaseViewModel {

    public ObservableField<OrderCirculationNoticeEntity> orderCirculation = new ObservableField<>();
    public ScanViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucOpenAlbum = new SingleLiveEvent<>();
        public SingleLiveEvent<OrderCirculationNoticeEntity> ucSuccess = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucFaile = new SingleLiveEvent<>();
    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    public BindingCommand openAlbum = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucOpenAlbum.call();
        }
    });




    public void scanCode(String orderNo) {
        RetrofitFactory.sApiService.scanCode(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<OrderCirculationNoticeEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(OrderCirculationNoticeEntity s) {

                        uc.ucSuccess.setValue(s);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                        uc.ucFaile.setValue(message);
                    }
                });
    }
}