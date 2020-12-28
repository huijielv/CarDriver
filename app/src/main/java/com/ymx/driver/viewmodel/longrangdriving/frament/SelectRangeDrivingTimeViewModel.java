package com.ymx.driver.viewmodel.longrangdriving.frament;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.RangeDrivingSelectTimeEntity;
import com.ymx.driver.entity.app.RemoteInfoEntity;
import com.ymx.driver.entity.app.TripOrderList;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.StatusView;
import com.ymx.driver.viewmodel.longrangdriving.LongRangeDrivingViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SelectRangeDrivingTimeViewModel extends BaseViewModel {

    public ObservableField<String> startTime = new ObservableField<>();
    public ObservableField<String> endTime = new ObservableField<>();


    public SelectRangeDrivingTimeViewModel(@NonNull Application application) {
        super(application);
    }


    public BindingCommand startSelectTime = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucStartSelectTime.call();
        }
    });

    public BindingCommand endSelectTime = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(startTime.get())) {

                UIUtils.showToast("请先选择开始时间");
                return;
            }

            uc.ucEndSelectTime.call();
        }
    });

    public BindingCommand confirm = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucConfirm.call();
        }
    });


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucStartSelectTime = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucEndSelectTime = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucClose = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucConfirm = new SingleLiveEvent<>();
    }

    public BindingCommand ucClose = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucClose.call();
        }
    });


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_SELECT_RANGGE_DRIVING_TIME_CODE:

                RangeDrivingSelectTimeEntity rangeDrivingSelectTimeEntity = (RangeDrivingSelectTimeEntity) event.src;
                if (rangeDrivingSelectTimeEntity.getSelectType() == 1) {
                    endTime.set(rangeDrivingSelectTimeEntity.getTime());
                } else {
                    startTime.set(rangeDrivingSelectTimeEntity.getTime());
                }

                break;
        }
    }


}
