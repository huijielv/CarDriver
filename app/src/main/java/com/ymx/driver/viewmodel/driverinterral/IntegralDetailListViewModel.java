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
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class IntegralDetailListViewModel extends BaseViewModel {

    public ObservableField<Integer> pagerIndex = new ObservableField<>(1);
    public ObservableField<String> integral = new ObservableField<>();
    public ObservableField<Boolean> refresh = new ObservableField<>(false);
    public ObservableList<IntegralDetailListItemViewModel> itemList = new ObservableArrayList<>();
    public ObservableField<Boolean> more = new ObservableField<>(false);
    public ItemBinding<IntegralDetailListItemViewModel> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.integral_detail_list_item);
    public UIChangeObservable uc = new UIChangeObservable();


    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> ucCanLoadmore = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucMyIntegral = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucRefresh = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucShow = new SingleLiveEvent<>();
    }

    private void onListSuccess(List<IntegralDetailListItem> list) {
        if (list == null || list.size() <= 0) {
            uc.ucCanLoadmore.setValue(false);
            if (itemList.size()<=0){
                more.set(true);
            }
            return;
        }
        if (list.size() < 10) {
            uc.ucCanLoadmore.setValue(false);
        } else {
            uc.ucCanLoadmore.setValue(true);
        }

        for (IntegralDetailListItem entity : list) {
            itemList.add(new IntegralDetailListItemViewModel(this, entity));
        }

    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_INTEGRAL_BACK_CODE));

        }
    });
    public BindingCommand myIntegral = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucMyIntegral.call();
        }
    });



    public IntegralDetailListViewModel(@NonNull Application application) {
        super(application);
    }

    public void getIntegralDetailList() {
        RetrofitFactory.sApiService.getIntegralDetailList(10 ,pagerIndex.get())
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<IntegralDetailModel>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(IntegralDetailModel integralDetailModel) {
                        integral.set(String.valueOf(integralDetailModel.getIntegral()));
                        if (refresh.get()) {
                            uc.ucRefresh.call();
                        }
                        onListSuccess(integralDetailModel.getIntegralList());
                        uc.ucShow.call();

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                        more.set(true);
                        uc.ucRefresh.call();
                        uc.ucShow.call();

                    }
                });
    }



}
