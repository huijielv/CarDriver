package com.ymx.driver.viewmodel.chartercar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.CharterCancalOrder;
import com.ymx.driver.entity.app.GetCharteredInfoEntity;
import com.ymx.driver.entity.app.UpdateCharteredInfoEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class CharterDrivingViewModel extends BaseViewModel {


    public ObservableField<String> drivingCity = new ObservableField<>();
    public ObservableField<Integer> remoteState = new ObservableField<>();
    public ObservableField<Boolean> isLoad = new ObservableField<>(true);
    public UIChangeObservable uc = new UIChangeObservable();

    public CharterDrivingViewModel(@NonNull Application application) {
        super(application);
    }


    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();


    }


    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });


    public BindingCommand stopWork = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (remoteState != null && remoteState.get() != null & remoteState.get() == 1) {
                updateFerryRemoteInfo(0);
            } else if (remoteState != null && remoteState.get() != null & remoteState.get() == 2) {

                UIUtils.showToast("行程中");
            }

        }
    });


    public BindingCommand load = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            getCharteredInfo();


        }
    });


    public void getCharteredInfo() {
        RetrofitFactory.sApiService.getCharteredInfo()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<GetCharteredInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(GetCharteredInfoEntity getCharteredInfoEntity) {
                        drivingCity.set(getCharteredInfoEntity.getDrivingCity());
                        remoteState.set(getCharteredInfoEntity.getCarState());
                        isLoad.set(true);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });


    }

    public void updateFerryRemoteInfo(int state) {
        RetrofitFactory.sApiService.updateCharteredInfo(state)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<UpdateCharteredInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(UpdateCharteredInfoEntity updateCharteredInfoEntity) {
                        drivingCity.set(updateCharteredInfoEntity.getDrivingCity());
                        remoteState.set(updateCharteredInfoEntity.getCarState());


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
            case MessageEvent.MSG_DRIVER_LOCK_CODE:
                getCharteredInfo();
                break;

        }
    }


}
