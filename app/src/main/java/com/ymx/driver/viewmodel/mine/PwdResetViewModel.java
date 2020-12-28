package com.ymx.driver.viewmodel.mine;

import android.app.Application;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.app.DriverLogoutEntity;
import com.ymx.driver.entity.app.SmsCodeEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.util.MD5;
import com.ymx.driver.util.UIUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xuweihua
 * 2020/5/9
 */
public class PwdResetViewModel extends BaseViewModel {
    public PwdResetViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public ObservableField<String> phone = new ObservableField<>();
    public ObservableBoolean formatPhone = new ObservableBoolean(false);
    public ObservableField<String> verifyMsg = new ObservableField<>();
    public ObservableField<String> newWd = new ObservableField<>();
    public ObservableField<String> confirmWd = new ObservableField<>();
    public ObservableInt pwdShow = new ObservableInt(0);

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGetCode = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucPwdReset = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGetVerifyCodeSuccess = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> resetPassWordSuccess = new SingleLiveEvent<>();

    }

    public BindingCommand showPwd = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            pwdShow.set(pwdShow.get() == 0 ? 1 : 0);
        }
    });


    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    public BindingCommand getCode = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            getUC().getHideSoftKeyBoardEvent().call();
            if (TextUtils.isEmpty(phone.get()) || phone.get().length() != 11) {
                UIUtils.showToast(UIUtils.getString(R.string.login_phone_error_tip));
                return;
            }

            SmsCode(phone.get(), 1);

        }
    });

    public BindingCommand pwdReset = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (!TextUtils.isEmpty(newWd.get()) && !TextUtils.isEmpty(confirmWd.get())) {
                if (!newWd.get().equals(confirmWd.get())) {
                    UIUtils.showToast(UIUtils.getString(R.string.new_pass_work_hiht));
                    return;
                }
                if (newWd.get().length() < 6) {
                    UIUtils.showToast(UIUtils.getString(R.string.new_pass_work_lenght_hiht));
                    return;
                }
                if (verifyMsg.get().length() != 6) {
                    UIUtils.showToast(UIUtils.getString(R.string.verify_code_hiht));
                    return;
                }

                resetPassWord(phone.get(), MD5.md5(newWd.get()), verifyMsg.get());
            }
        }
    });


    public TextWatcher verifyMsgWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            verifyMsg.set(s.toString());
        }
    };


    public void resetPassWord(String userPhone, String password, String authCode) {
        RetrofitFactory.sApiService.resetPassWord(userPhone, password, authCode)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<SmsCodeEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(SmsCodeEntity smsCodeEntity) {

                        uc.resetPassWordSuccess.call();
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public void SmsCode(String userPhone, int smsType) {
        RetrofitFactory.sApiService.getSmsCode(userPhone, smsType)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<SmsCodeEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(SmsCodeEntity smsCodeEntity) {

                        uc.ucGetVerifyCodeSuccess.call();
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

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

                        LoginHelper.logout();
                    }

                    @Override
                    protected void onFailure(String message) {
                        LoginHelper.logout();
                        UIUtils.showToast(message);
                    }
                });
    }

}
