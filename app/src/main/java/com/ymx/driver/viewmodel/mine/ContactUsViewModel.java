package com.ymx.driver.viewmodel.mine;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.ui.login.LoginHelper;

/**
 * Created by xuweihua
 * 2020/4/25
 */
public class ContactUsViewModel extends BaseViewModel {
    public ContactUsViewModel(@NonNull Application application) {
        super(application);

        UserEntity userEntity = LoginHelper.getUserEntity();
        if (!TextUtils.isEmpty(userEntity.getServicePhone())) {
            phoneNum.set(userEntity.getServicePhone());
        }


    }

    public ObservableField<String> phoneNum = new ObservableField<>();

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCall = new SingleLiveEvent<>();
    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    public BindingCommand call = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucCall.call();
        }
    });
}
