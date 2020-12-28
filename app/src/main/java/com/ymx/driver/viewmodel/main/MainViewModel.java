package com.ymx.driver.viewmodel.main;

import android.app.Application;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.NetChangeEntity;
import com.ymx.driver.entity.SystemNoticeEntity;
import com.ymx.driver.entity.app.CancelOrderEntity;
import com.ymx.driver.entity.app.UpdateEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.UIUtils;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xuweihua
 * 2020/4/20
 */
public class MainViewModel extends BaseViewModel {
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<UpdateEntity> ucUpdate = new SingleLiveEvent<>();
        public SingleLiveEvent<CancelOrderEntity> canCanOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<List<SystemNoticeEntity>> ucGetSystemNotice = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucTransfer0order = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCancalDialogDiss = new SingleLiveEvent<>();
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_PASSENGER_CANCEL_ORDER:
                CancelOrderEntity cancelOrderEntity = (CancelOrderEntity) event.src;
                uc.canCanOrder.setValue(cancelOrderEntity);
                break;
            case MessageEvent.MSG_TRANSFER_ORDER_SUCCESS_CODE:
                uc.ucTransfer0order.setValue((String) event.src);

                break;
            case MessageEvent.MSG_MAIN_CANCAL_DIALOG_DISS_CODE :

                uc.ucCancalDialogDiss.call();

                break;

        }
    }

    public void update(String bundleId) {
        RetrofitFactory.sApiService.update(bundleId)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<UpdateEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(UpdateEntity entity) {

                        uc.ucUpdate.setValue(entity);
                    }

                    @Override
                    protected void onFailure(String message) {

                    }
                });
    }


    public void getSystemNoticeList() {
        RetrofitFactory.sApiService.getSystemNoticeList()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<List<SystemNoticeEntity>>() {
                    @Override
                    protected void onRequestStart() {

                    }

                    @Override
                    protected void onRequestEnd() {

                    }

                    @Override
                    protected void onSuccees(List<SystemNoticeEntity> list) {
                        if (list == null || list.size() <= 0) {
                            return;
                        }
                        uc.ucGetSystemNotice.setValue(list);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

}
