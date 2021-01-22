package com.ymx.driver.viewmodel.finishorderdetails;

import android.app.Application;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.HttpConfig;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.NetChangeEntity;
import com.ymx.driver.entity.app.DetailsCostListEntity;
import com.ymx.driver.entity.app.OrderDetailsEntity;
import com.ymx.driver.entity.app.UpdateOrderStatusEntity;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.orderdetails.OrderItemViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class TripOrderDetailsFinshViewModel extends BaseViewModel {

    public TripOrderDetailsFinshViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<Integer> businessType = new ObservableField<>();
    public ObservableList<FinishOrderDetailsListItem> orderList = new ObservableArrayList<>();
    public ObservableField<String> startAddress = new ObservableField<>();
    public ObservableField<String> endAddress = new ObservableField<>();
    public ObservableField<String> orderId = new ObservableField<>();
    public ObservableField<String> headPicUrl = new ObservableField<>();
    public ObservableField<String> passengerCall = new ObservableField<>();
    public ObservableField<Boolean> isCall = new ObservableField<>();
    public ObservableField<Integer> orderStatus = new ObservableField<>();
    public ObservableField<String> stateName = new ObservableField<>();
    public ObservableField<String> passengerInfo = new ObservableField<>();
    public ObservableField<Integer> orderSource = new ObservableField<>();
    public ObservableField<String> billingRulesUrl = new ObservableField<>();
    public ObservableField<Integer> driverType = new ObservableField<>();
    public ObservableField<Integer> categoryType = new ObservableField<>();
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucCallPhone = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> back = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucQrCode = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucBillingRules = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucOrderDetails = new SingleLiveEvent<>();
    }

    public BindingCommand callPhone = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            uc.ucCallPhone.call();
        }
    });
    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.back.call();
        }
    });
    public BindingCommand goQrCode = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucQrCode.call();
        }
    });

    public BindingCommand goBillingRules = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBillingRules.call();
        }
    });


    //给RecyclerView添加ItemBinding
    public ItemBinding<FinishOrderDetailsListItem> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.finish_order_details_list_item);


    public void getOrderList(List<DetailsCostListEntity> list) {
        if (list.isEmpty() || list.size() <= 0) {
            return;
        }
        for (DetailsCostListEntity entity : list) {
            orderList.add(new FinishOrderDetailsListItem(this, entity));
        }

    }

    public void getOrderDetails(String orderNo) {
        RetrofitFactory.sApiService.getOrderDetails(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<OrderDetailsEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(OrderDetailsEntity orderDetailsEntity) {
                        driverType.set(orderDetailsEntity.getDriverType());
                        categoryType.set(orderDetailsEntity.getCategoryType());
                        businessType.set(orderDetailsEntity.getBusinessType());
                        startAddress.set(orderDetailsEntity.getStartAddress());
                        endAddress.set(orderDetailsEntity.getEndAddress());
                        orderId.set(orderDetailsEntity.getOrderNo());
                        headPicUrl.set(orderDetailsEntity.getHeadPicUrl());
                        passengerCall.set(orderDetailsEntity.getMobile());
                        orderStatus.set(orderDetailsEntity.getOrderState());
                        stateName.set(orderDetailsEntity.getStateName());
                        orderSource.set(orderDetailsEntity.getChannel());
                        if (!TextUtils.isEmpty(orderDetailsEntity.getMobile())) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("尾号");
                            sb.append(orderDetailsEntity.getMobile().substring(7));
                            passengerInfo.set(sb.toString());

                        }

                        StringBuffer billingRulesUrlSb = new StringBuffer();

                        billingRulesUrlSb.append(HttpConfig.BillingRulesUrl).
                                append("areaCode=").
                                append(orderDetailsEntity.getAreaCode()).append("&token=").append(LoginHelper.getUserEntity().getToken())
                                .append("&orderNo=").append(orderDetailsEntity.getOrderNo())
                                .append("&driverType=").append(orderDetailsEntity.getDriverType())
                                .append("&businessType=").append(orderDetailsEntity.getBusinessType())
                                .append("&chargeRuleId=").append(orderDetailsEntity.getChargeRuleId())
                                .append("&categoryType=").append(orderDetailsEntity.getCategoryType());

                        billingRulesUrl.set(billingRulesUrlSb.toString());
                        uc.ucOrderDetails.call();

                        getOrderList(orderDetailsEntity.getCostList());

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

            case MessageEvent.MSG_TRAVEL_ORDERS_FINSH_DATA_CODE:
                orderList.clear();
                if (!TextUtils.isEmpty(orderId.get())) {
                    getOrderDetails(orderId.get());
                }

                break;


        }
    }

}
