package com.ymx.driver.viewmodel.launch;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.app.CancelOrderEntity;
import com.ymx.driver.entity.app.QcodeEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class QcodeViewModel extends BaseViewModel {

    public ObservableField<String> driverHeadPicUrl = new ObservableField<>();
    public ObservableField<String> driverName = new ObservableField<>();
    public ObservableField<String> belongCompany = new ObservableField<>();
    public ObservableField<String> driverNo = new ObservableField<>();
    public ObservableField<String> qrcode = new ObservableField<>();


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucloadQcode = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> back = new SingleLiveEvent<>();
    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.back.call();
        }
    });


    public QcodeViewModel(@NonNull Application application) {
        super(application);
    }


    public void queryDriverQCode() {
        RetrofitFactory.sApiService.driverQrcode()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<QcodeEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(QcodeEntity qcodeEntity) {
                        driverHeadPicUrl.set(qcodeEntity.getDriverHeadPicUrl());
                        driverName.set(qcodeEntity.getDriverName());
                        belongCompany.set(qcodeEntity.getBelongCompany());
                        if (!TextUtils.isEmpty(qcodeEntity.getQrcode())) {
                            driverNo.set("编号：" + qcodeEntity.getDriverNo());
                        }
                        qrcode.set(qcodeEntity.getQrcode());

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

}
