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
import com.ymx.driver.entity.app.IntegralDetailListItem;
import com.ymx.driver.entity.app.IntegralDetailModel;
import com.ymx.driver.entity.app.MyIntegralOrderListItemEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class MyIntegralOrderListViewModel extends BaseViewModel {
    public MyIntegralOrderListViewModel(@NonNull Application application) {
        super(application);
        more.set(false);
    }

    public ObservableField<Integer> pagerIndex = new ObservableField<>(1);
    public ObservableField<Boolean> refresh = new ObservableField<>(false);
    public ObservableList<MyIntegralOrderListItemViewModel> itemList = new ObservableArrayList<>();
    public ItemBinding<MyIntegralOrderListItemViewModel> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.intergral_exchange_order_list_item);
    public UIChangeObservable uc = new UIChangeObservable();
    public ObservableField<Boolean> more = new ObservableField<>();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> ucCanLoadmore = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucRefresh = new SingleLiveEvent<>();
    }

    private void onListSuccess(List<MyIntegralOrderListItemEntity> list) {
        if (list == null || list.size() <= 0) {
            uc.ucCanLoadmore.setValue(false);
            return;
        }
        if (list.size() < 10) {
            uc.ucCanLoadmore.setValue(false);
        } else {
            uc.ucCanLoadmore.setValue(true);
        }

        for (MyIntegralOrderListItemEntity entity : list) {
            itemList.add(new MyIntegralOrderListItemViewModel(this, entity));
        }

        if (itemList.size() > 0) {
            more.set(true);
        }
    }
    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_INTEGRAL_BACK_CODE));

        }
    });

    public void getIntegralExchangeList() {
        RetrofitFactory.sApiService.getIntegralExchangeList(pagerIndex.get(),10)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<List<MyIntegralOrderListItemEntity>>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(List<MyIntegralOrderListItemEntity> list) {
                        if (refresh.get()) {
                            uc.ucRefresh.call();
                        }
                        onListSuccess(list);

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                        uc.ucRefresh.call();
                    }
                });
    }

}
