package com.ymx.driver.viewmodel.mine;

import android.app.Application;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.app.DriverLogoutEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wuwei
 * 2020/4/24
 * 佛祖保佑       永无BUG
 */
public class SettingViewModel extends BaseViewModel {
    public SettingViewModel(@NonNull Application application) {
        super(application);
    }


    public ObservableField<Integer> navStatus = new ObservableField<>();

    public UIChangeObservable uc = new UIChangeObservable();

    @Override
    public void onCreate() {
        super.onCreate();
        navStatus.set(YmxCache.getNavStatus());

    }

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucPwdMOdify = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucNavSet = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucAbout = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucLogout = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucLogoutSucecess = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucLogoutFail = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucNavStatusSet = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucPowerSettingsSet = new SingleLiveEvent<>();

    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    public BindingCommand pwdMOdify = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucPwdMOdify.call();
        }
    });

    public BindingCommand navSet = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucNavSet.call();
        }
    });

    public BindingCommand navStatusSet = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucNavStatusSet.call();
        }
    });

    public BindingCommand about = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucAbout.call();
        }
    });

    public BindingCommand logout = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucLogout.call();
        }
    });

    public BindingCommand powerSettings = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucPowerSettingsSet.call();
        }
    });


    public void driverLogout() {
        RetrofitFactory.sApiService.driverLogout()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<DriverLogoutEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(DriverLogoutEntity driverLogoutEntity) {

                        uc.ucLogoutSucecess.call();
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                        uc.ucLogoutFail.call();
                    }
                });
    }

}
