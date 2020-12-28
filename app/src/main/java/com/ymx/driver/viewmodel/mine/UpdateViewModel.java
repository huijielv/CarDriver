package com.ymx.driver.viewmodel.mine;

import android.app.Application;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.app.UpdateEntity;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

/**
 * Created by wuwei
 * 2020/4/22
 * 佛祖保佑       永无BUG
 */
public class UpdateViewModel extends BaseViewModel {
    public UpdateViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<UpdateEntity> entity = new ObservableField<>();

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucNext = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucUpdate = new SingleLiveEvent<>();
    }

    public BindingCommand next = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucNext.call();
        }
    });

    public BindingCommand update = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucUpdate.call();
        }
    });
}
