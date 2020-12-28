package com.ymx.driver.viewmodel.mine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;

import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.TodayOrderEntity;


import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.StatusView;


import org.greenrobot.eventbus.EventBus;

import java.util.List;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class TodayOrderViewModel extends BaseViewModel {
    public TodayOrderViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<Integer> pagerIndex = new ObservableField<>(1);
    //给RecyclerView添加ObservableList
    public ObservableList<TodayOrderItemViewModel> todayOrderList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<TodayOrderItemViewModel> itembinding = ItemBinding.of(BR.viewModel, R.layout.item_today_order);
    public ObservableInt status = new ObservableInt(StatusView.STATUS_NORMAL);

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> ucCanLoadmore = new SingleLiveEvent<>();
        public SingleLiveEvent<TodayOrderEntity> ucToTodayOrder = new SingleLiveEvent<>();
    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    private void onTodayOrderLoadSuccess(List<TodayOrderEntity> list) {
        if (list == null || list.size() <= 0) {
            if (pagerIndex.get() == 1) {
                if (todayOrderList.size() == 0) {
                    status.set(StatusView.STATUS_EMPTY);
                }
            }

            uc.ucCanLoadmore.setValue(false);
            return;
        }
        if (list.size() < 10) {
            uc.ucCanLoadmore.setValue(false);
        } else {
            uc.ucCanLoadmore.setValue(true);
        }
        for (TodayOrderEntity entity : list) {
            todayOrderList.add(new TodayOrderItemViewModel(this, entity));
        }
    }


    public void getOrderTodayList() {
        RetrofitFactory.sApiService.getOrderTodayList(10, pagerIndex.get())
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<List<TodayOrderEntity>>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(List<TodayOrderEntity> list) {
                        onTodayOrderLoadSuccess(list);
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
            case MessageEvent.MSG_GO_TODAY_ORDER_CODE:
                TodayOrderEntity todayOrderEntity = (TodayOrderEntity) event.src;
                uc.ucToTodayOrder.setValue(todayOrderEntity);
                break;

            case MessageEvent.MSG_TRIP_REFRESH_DATA_CODE:
                pagerIndex.set(1);
                todayOrderList.clear();
                getOrderTodayList();

                break;
        }

    }


}
