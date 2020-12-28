package com.ymx.driver.viewmodel.login;

import android.app.Application;
import android.text.Editable;
import android.text.TextWatcher;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.entity.app.postbody.BodyLoginEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.MD5;
import com.ymx.driver.util.UIUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wuwei
 * 2020/5/6
 * 佛祖保佑       永无BUG
 */
public class LoginViewModel extends BaseViewModel {
    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<String> phone = new ObservableField<>();
    public ObservableField<String> pwd = new ObservableField<>();
    public ObservableInt pwdShow = new ObservableInt(0);

    public LoginViewModel.UIChangeObservable uc = new LoginViewModel.UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucFogetPwd = new SingleLiveEvent<>();
        public SingleLiveEvent<UserEntity> ucLoginSuccess = new SingleLiveEvent<>();
    }

    public TextWatcher phoneWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            phone.set(s.toString());
        }
    };

    public TextWatcher pwdWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            pwd.set(s.toString());
        }
    };

    public BindingCommand clearAccount = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            phone.set("");
        }
    });

    public BindingCommand clearPwd = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            pwd.set("");
        }
    });

    public BindingCommand showPwd = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            pwdShow.set(pwdShow.get() == 0 ? 1 : 0);
        }
    });

    public BindingCommand fogetPwd = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucFogetPwd.call();
        }
    });

    public BindingCommand login = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            login(phone.get(), MD5.md5(pwd.get()));
        }
    });

    public void login(String mobile, String password) {
        RetrofitFactory.sApiService.login(new BodyLoginEntity(mobile, password, YmxCache.getDeviceToken()))
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<UserEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(UserEntity userEntity) {
                        uc.ucLoginSuccess.setValue(userEntity);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }
}
