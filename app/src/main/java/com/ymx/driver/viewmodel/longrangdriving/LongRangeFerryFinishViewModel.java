package com.ymx.driver.viewmodel.longrangdriving;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.HttpConfig;
import com.ymx.driver.entity.RangDriverPassgerFinishOederDetails;
import com.ymx.driver.entity.RangDriverPassgerFinishOederDetailsListItem;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.util.UIUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class LongRangeFerryFinishViewModel extends BaseViewModel {
    public LongRangeFerryFinishViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<String> startName = new ObservableField<>();
    public ObservableField<String> endName = new ObservableField<>();
    public ObservableField<String> orderNo = new ObservableField<>();
    public ObservableField<Boolean> isLoad = new ObservableField<>();
    public ObservableField<String> travelMileage = new ObservableField<>();
    public ObservableField<String> rideNumber  = new ObservableField<>();
    public ObservableField<Integer> driverState = new ObservableField<>();
    public ObservableList<LongRangDrivingFerryFinishListItemViewModel> list = new ObservableArrayList<>();
    public ObservableField<String> billingRulesUrl = new ObservableField<>();
    public ItemBinding<LongRangDrivingFerryFinishListItemViewModel> itembinding = ItemBinding.of(BR.viewModel, R.layout.long_range_driving_ferry_finish_details_item_view);

    public UIChangeObservable uc = new UIChangeObservable();



    public void getList(List<RangDriverPassgerFinishOederDetailsListItem> list) {
        if (list.isEmpty() || list.size() <= 0) {
            return;
        }
        for (RangDriverPassgerFinishOederDetailsListItem entity : list) {
            this.list.add(new LongRangDrivingFerryFinishListItemViewModel(this, entity));
        }

    }


    public void rangeDrivingOrderFinishDetails(String orderNo) {
        RetrofitFactory.sApiService.rangeDrivingOrderDetails(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<RangDriverPassgerFinishOederDetails>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(RangDriverPassgerFinishOederDetails orderDetailsEntity) {
                        travelMileage.set(orderDetailsEntity.getTravelMileage());
                        rideNumber.set(orderDetailsEntity.getRideNumber());
                        startName.set(orderDetailsEntity.getStartAddress());
                        endName.set(orderDetailsEntity.getEndAddress());
                        getList(orderDetailsEntity.getPassengerList());
                        driverState.set(orderDetailsEntity.getDriverState());
                        isLoad.set(true);

                        StringBuffer billingRulesUrlSb = new StringBuffer();

                        billingRulesUrlSb.append(HttpConfig.BillingRulesUrl)
                                .append(LoginHelper.getUserEntity().getToken())
                                .append("&businessType=").append("6")
                                .append("&orderNo=").append(orderDetailsEntity.getOrderNo());
                        billingRulesUrl.set(billingRulesUrlSb.toString());

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCallPhone = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucBillingRules = new SingleLiveEvent<>();
    }


    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });


    public BindingCommand callPhone = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            uc.ucCallPhone.call();
        }
    });

    public BindingCommand load = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            if (orderNo != null && !TextUtils.isEmpty(orderNo.get())) {
                rangeDrivingOrderFinishDetails(orderNo.get());
            }


        }
    });

    public BindingCommand goBillingRules = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBillingRules.call();
        }
    });


}
