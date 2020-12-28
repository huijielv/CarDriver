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
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.RangDriverPassgerFinishOederDetails;
import com.ymx.driver.entity.RangDriverPassgerFinishOederDetailsListItem;
import com.ymx.driver.entity.app.LongDrivingPaySuccessEntigy;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.UIUtils;


import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class LongRangDrivingFinishViewModel extends BaseViewModel {

    public ObservableField<String> startName = new ObservableField<>();
    public ObservableField<String> endName = new ObservableField<>();
    public ObservableField<String> payDsc = new ObservableField<>();
    public ObservableField<String> orderNo = new ObservableField<>();
    public ObservableField<Boolean> isLoad = new ObservableField<>();
    public ObservableField<Integer> businessOrderType = new ObservableField<>(6);
    public ObservableField<Integer> driverState = new ObservableField<>();
    public ObservableField<String> address = new ObservableField<>();

    public ObservableList<LongRangDrivingFinishListItemViewModel> list = new ObservableArrayList<>();
    public ObservableField<String> billingRulesUrl = new ObservableField<>();
    public ItemBinding<LongRangDrivingFinishListItemViewModel> itembinding = ItemBinding.of(BR.viewModel, R.layout.long_range_driving_finish_details_item_view);

    public UIChangeObservable uc = new UIChangeObservable();

    public LongRangDrivingFinishViewModel(@NonNull Application application) {
        super(application);
        isLoad.set(false);
    }

    public void getList(List<RangDriverPassgerFinishOederDetailsListItem> orderList) {

        if (orderList == null || orderList.isEmpty() || orderList.size() <= 0) {
            return;
        }

        for (RangDriverPassgerFinishOederDetailsListItem entity : orderList) {
            this.list.add(new LongRangDrivingFinishListItemViewModel(this, entity));
        }


    }

    public void transferOrderFinishDetails(String orderNo) {
        RetrofitFactory.sApiService.transferStationOrderDetails(orderNo)
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
                    protected void onSuccees(RangDriverPassgerFinishOederDetails transferDetails) {
                        isLoad.set(true);
                        driverState.set(transferDetails.getDriverState());
                        businessOrderType.set(transferDetails.getBusinessType());
                        address.set(transferDetails.getAddress());
                        payDsc.set(transferDetails.getPayDsc());
                        StringBuffer billingRulesUrlSb = new StringBuffer();

                        billingRulesUrlSb.append(HttpConfig.BillingRulesUrl)
                                .append("&token=")
                                .append(LoginHelper.getUserEntity().getToken())
                                .append("&businessType=").append("11")
                                .append("&orderNo=").append(transferDetails.getOrderNo());
                        billingRulesUrl.set(billingRulesUrlSb.toString());
                        getList(transferDetails.getPassengerList());

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
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
                        startName.set(orderDetailsEntity.getStartAddress());
                        endName.set(orderDetailsEntity.getEndAddress());
                        getList(orderDetailsEntity.getPassengerList());
                        driverState.set(orderDetailsEntity.getDriverState());
                        payDsc.set(orderDetailsEntity.getPayDsc());
                        isLoad.set(true);

                        StringBuffer billingRulesUrlSb = new StringBuffer();

                        billingRulesUrlSb.append(HttpConfig.BillingRulesUrl)
                                .append("&token=")
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
        public SingleLiveEvent<String> ucPayDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucQrCodeDialog = new SingleLiveEvent<>();
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


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_QUERY_SHOW_PAY_DIALOG:

                String order = (String) event.src;
                uc.ucQrCodeDialog.setValue(order);
                break;

            case MessageEvent.MSG_QUERY_LONG_DRIVIER_FINISH_DETAILS_PAY:
                String orderId = (String) event.src;
                uc.ucPayDialog.setValue(orderId);
                break;
            case MessageEvent.MSG_QUERY_LONG_DRIVIER_PAY_SUCCESS:

                LongDrivingPaySuccessEntigy paySuccessEntigy = (LongDrivingPaySuccessEntigy) event.src;
                if (businessOrderType.get() == 11) {
                    if (paySuccessEntigy != null && paySuccessEntigy.getDriverOrderNo() != null && paySuccessEntigy.getDriverOrderNo().equals(orderNo.get())) {
                        list.clear();
                        transferOrderFinishDetails(orderNo.get());
                    }

                } else {
                    if (paySuccessEntigy != null && paySuccessEntigy.getDriverOrderNo() != null && paySuccessEntigy.getDriverOrderNo().equals(orderNo.get())) {
                        list.clear();
                        rangeDrivingOrderFinishDetails(orderNo.get());
                    }
                }


                break;


        }
    }


}
