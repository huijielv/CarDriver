package com.ymx.driver.viewmodel.driverinterral;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.InterralCommodityEntity;
import com.ymx.driver.entity.app.InterralCommodityListItem;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class InterralViewModel extends BaseViewModel {
    public ObservableField<String> integral = new ObservableField<>();
    public ObservableField<Boolean> refresh = new ObservableField<>(false);
//    public ObservableList<InterralCommodityListItemViewModel> itemList = new ObservableArrayList<>();
//    public ItemBinding<InterralCommodityListItemViewModel> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.interral_commodity_list_item);
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> ucCanLoadmore = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGoIntegralDetailList = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGoMyOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucRefresh = new SingleLiveEvent<>();
        public SingleLiveEvent<InterralCommodityListItem> ucShowDialog = new SingleLiveEvent<>();
        public SingleLiveEvent< List< InterralCommodityListItem>> ucList = new SingleLiveEvent<>();
    }


    public ObservableField<Integer> pagerIndex = new ObservableField<>(1);

    public BindingCommand clickIntegralDetailList = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucGoIntegralDetailList.call();
        }
    });

    public BindingCommand clickMyOrder = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucGoMyOrder.call();
        }
    });


    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_INTEGRAL_BACK_CODE));

        }
    });


    public InterralViewModel(@NonNull Application application) {
        super(application);
//        itemList.clear();
    }

    private void onListSuccess(List<InterralCommodityListItem> list) {
        if (list == null || list.size() <= 0) {
            uc.ucCanLoadmore.setValue(false);
            return;
        }
        if (list.size() < 50) {
            uc.ucCanLoadmore.setValue(false);
        } else {
            uc.ucCanLoadmore.setValue(true);
        }

//        for (InterralCommodityListItem entity : list) {
//            itemList.add(new InterralCommodityListItemViewModel(this, entity));
//        }
    }


    public void getInterralCommodityList() {
        RetrofitFactory.sApiService.getInterralCommodityList(pagerIndex.get(), 50)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<InterralCommodityEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(InterralCommodityEntity interralCommodityEntity) {
                        integral.set(String.valueOf(interralCommodityEntity.getIntegral()));
                        if (refresh.get()) {
                            uc.ucRefresh.call();
                        }
//                        onListSuccess(interralCommodityEntity.getExchangeList());
                        uc.ucList.setValue(interralCommodityEntity.getExchangeList());
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                        uc.ucRefresh.call();
                    }
                });
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_SHOW_CONFIRM_EXCHANGE_DIALOG_CODE:
                uc.ucShowDialog.setValue((InterralCommodityListItem) event.src);

                break;
            case MessageEvent.MSG_SHOW_CONFIRM_EXCHANGE_SUCCESS_CODE:
                integral.set((String) event.src);
//                itemList.clear();
                pagerIndex.set(1);
                getInterralCommodityList();
                break;

        }
    }

}