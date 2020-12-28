package com.ymx.driver.viewmodel.mine;

import android.app.Application;

import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;

/**
 * Created by wuwei
 * 2020/4/24
 * 佛祖保佑       永无BUG
 */
public class NavSettingViewModel extends BaseViewModel {
    public NavSettingViewModel(@NonNull Application application) {
        super(application);
    }

    public static final int NAV_MODE_GAODE = 0;
    public static final int NAV_MODE_BAIDU = 1;
    public static final int NAV_MODE_TENCENT = 2;

    public ObservableInt select = new ObservableInt(0);

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    public BindingCommand navSetGaode = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            select.set(NAV_MODE_GAODE);
            YmxCache.setNavMode(NAV_MODE_GAODE);
        }
    });

    public BindingCommand navSetBaidu = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            select.set(NAV_MODE_BAIDU);
            YmxCache.setNavMode(NAV_MODE_BAIDU);
        }
    });

    public BindingCommand navSetTencent = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            select.set(NAV_MODE_TENCENT);
            YmxCache.setNavMode(NAV_MODE_TENCENT);
        }
    });
}
