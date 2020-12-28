package com.ymx.driver.viewmodel.mine;

import android.app.Application;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.app.UpdateEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xuweihua
 * 2020/4/26
 */
public class AboutViewModel extends BaseViewModel {
    public AboutViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucUserTerms = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucVersion = new SingleLiveEvent<>();
        public SingleLiveEvent<UpdateEntity> ucUpdate = new SingleLiveEvent<>();
    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    public BindingCommand userTerms = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucUserTerms.call();
        }
    });

    public BindingCommand version = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucVersion.call();
        }
    });

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
                        UIUtils.showToast(message);
                    }
                });
    }
}
