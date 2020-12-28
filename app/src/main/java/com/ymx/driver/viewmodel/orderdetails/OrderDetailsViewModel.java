package com.ymx.driver.viewmodel.orderdetails;

import android.app.Application;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.CancelOrderEntity;
import com.ymx.driver.entity.app.ConfirmOrderEntity;
import com.ymx.driver.entity.app.DetailsCostListEntity;
import com.ymx.driver.entity.app.OrderDetailsEntity;
import com.ymx.driver.entity.app.UpdateOrderStatusEntity;
import com.ymx.driver.entity.app.mqtt.PhoneOrderSuccessEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.travel.TravelViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

import static com.ymx.driver.util.UIUtils.getString;

public class OrderDetailsViewModel extends BaseViewModel {
    public ObservableList<OrderItemViewModel> orderList = new ObservableArrayList<>();
    public ObservableField<String> startAddress = new ObservableField<>();
    public ObservableField<String> endAddress = new ObservableField<>();
    public ObservableField<String> orderId = new ObservableField<>();
    public ObservableField<String> headPicUrl = new ObservableField<>();
    public ObservableField<String> etPrice = new ObservableField<>();
    public ObservableField<String> totalFee = new ObservableField<>();
    public ObservableField<String> passengerCall = new ObservableField<>();
    public ObservableField<Boolean> isCall = new ObservableField<>();
    public ObservableField<Integer> orderStatus = new ObservableField<>();
    public ObservableField<String> buttonText = new ObservableField<>();
    public ObservableField<Integer> orderSource = new ObservableField<>();
    public ObservableField<String> passengerInfo = new ObservableField<>();
    public ObservableField<String> orderTime = new ObservableField<>();
    public ObservableField<String> payOrder = new ObservableField<>();
    public ObservableField<Integer> isVip = new ObservableField<>();
    public ObservableField<Boolean> isLoad = new ObservableField<>();
    public ObservableField<Integer> businessType = new ObservableField<>();
    public ObservableField<Integer> driverType = new ObservableField<>();
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucCallPhone = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> back = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucUpdateButtonText = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucInitButtonText = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCanOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGrabOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> ucGrabOrderStatus = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucLoadQcode = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucQcodeVisible = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucPay = new SingleLiveEvent<>();
        public SingleLiveEvent<PhoneOrderSuccessEntity> ucPaySuccess = new SingleLiveEvent<>();
    }

    public BindingCommand callPhone = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isCall.set(false);
            uc.ucCallPhone.call();
        }
    });
    public BindingCommand ucPassengerCall = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isCall.set(true);
            uc.ucCallPhone.call();
        }
    });
    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.back.call();
        }
    });


    public BindingCommand qCode = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucQcodeVisible.call();
        }
    });

    public BindingCommand pay = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucPay.call();
        }
    });


    //给RecyclerView添加ItemBinding
    public ItemBinding<OrderItemViewModel> itembinding = ItemBinding.of(BR.viewModel, R.layout.item_order);

    public OrderDetailsViewModel(@NonNull Application application) {
        super(application);
    }


    public TextWatcher priceWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            etPrice.set(s.toString());
        }
    };


    public BindingCommand load = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            getOrderDetails(orderId.get());


        }
    });


    public void getOrderList(List<DetailsCostListEntity> list) {
        if (list.isEmpty() || list.size() <= 0) {
            return;
        }
        for (DetailsCostListEntity entity : list) {
            orderList.add(new OrderItemViewModel(this, entity));
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
                        isLoad.set(true);
                        businessType.set(orderDetailsEntity.getBusinessType());
                        driverType.set(orderDetailsEntity.getDriverType());
                        startAddress.set(orderDetailsEntity.getStartAddress());
                        orderStatus.set(orderDetailsEntity.getOrderState());
                        endAddress.set(orderDetailsEntity.getEndAddress());
                        orderId.set(orderDetailsEntity.getOrderNo());
                        headPicUrl.set(orderDetailsEntity.getHeadPicUrl());
                        passengerCall.set(orderDetailsEntity.getMobile());
                        orderSource.set(orderDetailsEntity.getChannel());
                        orderTime.set(orderDetailsEntity.getTime());
                        isVip.set(orderDetailsEntity.getIsVip());
                        if (orderDetailsEntity.getOrderState() == TravelViewModel.DRIVER_STATE_CONFIRM_COST) {
                            totalFee.set(orderDetailsEntity.getTotalFee());
                        } else if (orderDetailsEntity.getOrderState() == TravelViewModel.DRIVER_STATE_TO_PAY) {
                            totalFee.set(orderDetailsEntity.getUnPayFee());
                        }
                        uc.ucInitButtonText.call();

                        if (orderDetailsEntity.getChannel() == 5&&orderDetailsEntity.getBusinessType()!=10) {
                            buttonText.set(getString(R.string.pay_order_details_button_text));
                        } else {
                            buttonText.set(getString(R.string.order_details_button_text));
                        }
                        if (!TextUtils.isEmpty(orderDetailsEntity.getMobile())) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("尾号");
                            sb.append(orderDetailsEntity.getMobile().substring(7));
                            passengerInfo.set(sb.toString());
                        }


                        getOrderList(orderDetailsEntity.getCostList());

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public void updateOrderDetailsStatus(String orderNo, String currentCoord, String actionType, String otherCharge) {
        RetrofitFactory.sApiService.updateOrderDetailsStatus(orderNo, currentCoord, actionType, otherCharge)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<UpdateOrderStatusEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(UpdateOrderStatusEntity updateOrderStatusEntity) {
                        orderStatus.set(updateOrderStatusEntity.getDriverState());
                        orderId.set(updateOrderStatusEntity.getOrderNo());
                        buttonText.set(updateOrderStatusEntity.getButtonText());
                        orderSource.set(updateOrderStatusEntity.getChannel());
                        uc.ucUpdateButtonText.call();

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public BindingCommand ucCanCalOrder = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucCanOrder.call();
        }
    });
    public BindingCommand ucGrabOrder = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucGrabOrder.call();
        }
    });


    // 取消订单
    public void cancelOrder(String orderNo) {
        RetrofitFactory.sApiService.cancelOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<CancelOrderEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(CancelOrderEntity s) {
                        uc.ucGrabOrderStatus.setValue(false);
                        UIUtils.showToast(s.getMessage());
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    // 抢订单
    public void grabOrder(String orderNo) {
        RetrofitFactory.sApiService.confirmOrder(orderNo, 0)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<ConfirmOrderEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(ConfirmOrderEntity s) {
                        uc.ucGrabOrderStatus.setValue(true);
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GRAB_GOOD_FIREND_ORDER_SUCCESS, s.getOrderNo()));

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public void orderPay(String orderNo) {
        RetrofitFactory.sApiService.orderPay(1, orderNo,2)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<String>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(String s) {
//                        payOrderInfo.set(s);
                        uc.ucLoadQcode.setValue(s);


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
            case MessageEvent.MSG_PHONE_ORDER_PAY_SUCCESS:
                PhoneOrderSuccessEntity phoneOrderSuccessEntity = (PhoneOrderSuccessEntity) event.src;
                if (phoneOrderSuccessEntity != null) {
                    uc.ucPaySuccess.setValue(phoneOrderSuccessEntity);
                }

                break;

        }
    }


}
