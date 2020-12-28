package com.ymx.driver.viewmodel.chartercar;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;


import com.amap.api.location.AMapLocation;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.HttpConfig;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.CharterCancalOrder;
import com.ymx.driver.entity.app.CharterOrderDetailsEntity;
import com.ymx.driver.entity.app.CharteredOrderListItem;
import com.ymx.driver.entity.app.TravelListEntity;
import com.ymx.driver.entity.app.UpdateCharterOrderBody;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.map.ServerLocation;
import com.ymx.driver.map.ServerLocationManager;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.util.UIUtils;

import com.ymx.driver.viewmodel.travel.TravelViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class CharterDetailsViewModel extends BaseViewModel {

    public CharterDetailsViewModel(@NonNull Application application) {
        super(application);
        isLoad.set(false);
        flipData.set(true);

    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCall = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucNavigate = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucUpdateOrderStatus = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucOrderDetails = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucQrcode = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucPayDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucPaySuccess = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucBillingRules = new SingleLiveEvent<>();

    }


    public ObservableField<Boolean> isLoad = new ObservableField<>();
    public ObservableField<String> phoneNum = new ObservableField<>("");
    public ObservableField<String> passengerInfo = new ObservableField<>();
    public ObservableField<String> orderId = new ObservableField<>();
    public ObservableField<String> endName = new ObservableField<>();
    public ObservableField<String> startName = new ObservableField<>();
    public ObservableField<Integer> orderStatus = new ObservableField<>();
    public ObservableField<Double> lat = new ObservableField<>();
    public ObservableField<Double> lng = new ObservableField<>();
    public ObservableInt mode = new ObservableInt(TravelViewModel.DRIVER_STATE_TO_PASSENGERS);

    public ObservableField<Integer> payState = new ObservableField<>();
    public ObservableField<Integer> isNeedBack = new ObservableField<>();
    public ObservableField<Integer> orderType = new ObservableField<>();
    public ObservableField<String> numberDay = new ObservableField<>();
    public ObservableField<String> charteredCity = new ObservableField<>();
    public ObservableField<String> charteredDurationMileage = new ObservableField<>();
    public ObservableField<String> titleText = new ObservableField<>();
    public ObservableField<Integer> businessType = new ObservableField<>();
    public ObservableField<Integer> isLastDay = new ObservableField<>();
    public ObservableField<String> orderTimeDesc = new ObservableField<>();

    public ObservableField<String> startSite = new ObservableField<>();
    public ObservableField<String> endStie = new ObservableField<>();
    public ObservableField<String> appointmentTime = new ObservableField<>();

    public ObservableField<Integer> isTimeout = new ObservableField<>();
    public ObservableField<Integer> time = new ObservableField<>();
    public ObservableField<String> timeText = new ObservableField<>();
    public ObservableField<String> feeText = new ObservableField<>();
    public ObservableField<String> startAddress = new ObservableField<>();
    public ObservableField<Boolean> flipData = new ObservableField<>();
    public ObservableField<String> billingRulesUrl = new ObservableField<>();

    public ObservableList<CharterDayFinishItem> itemList = new ObservableArrayList<>();
    public ItemBinding<CharterDayFinishItem> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.charter_day_finish_list_item);
    public ObservableList<CharterCostListItemViewModel> itemCostList = new ObservableArrayList<>();
    public ItemBinding<CharterCostListItemViewModel> itemCostbinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.charter_cost_item);

    public BindingCommand load = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            charteredOrderDetails(orderId.get());

        }
    });


    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    public BindingCommand call = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucCall.call();
        }
    });

    public BindingCommand navigate = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucNavigate.call();
        }
    });


    public BindingCommand qRcode = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucQrcode.call();
        }
    });

    public BindingCommand flipTurn = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            flipData.set(!flipData.get());
        }
    });

    public BindingCommand goBillingRules = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBillingRules.call();
        }
    });


    public void charteredOrderDetails(String orderNo) {
        RetrofitFactory.sApiService.charteredOrderDetails(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<CharterOrderDetailsEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(CharterOrderDetailsEntity orderDetailsEntity) {
                        isLoad.set(true);
                        mode.set(orderDetailsEntity.getDriverState());
                        orderStatus.set(orderDetailsEntity.getDriverState());
                        endName.set(orderDetailsEntity.getDesName());
                        startName.set(orderDetailsEntity.getSrcName());
                        phoneNum.set(orderDetailsEntity.getPhone());
                        lat.set(orderDetailsEntity.getSrcLat());
                        lng.set(orderDetailsEntity.getSrcLng());
                        orderId.set(orderDetailsEntity.getOrderNo());
                        if (!TextUtils.isEmpty(orderDetailsEntity.getPhone())) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("尾号");
                            sb.append(orderDetailsEntity.getPhone().substring(7));
                            passengerInfo.set(sb.toString());
                        }

                        payState.set(orderDetailsEntity.getPayState());
                        orderType.set(orderDetailsEntity.getOrderType());
                        numberDay.set(orderDetailsEntity.getNumberDay());
                        isNeedBack.set(orderDetailsEntity.getIsNeedBack());
                        charteredCity.set(orderDetailsEntity.getCharteredCity());
                        charteredDurationMileage.set(orderDetailsEntity.getCharteredDurationMileage());
                        titleText.set(orderDetailsEntity.getTitleText());
                        businessType.set(orderDetailsEntity.getBusinessType());

                        isLastDay.set(orderDetailsEntity.getIsLastDay());
                        startSite.set(orderDetailsEntity.getStartSite());
                        endStie.set(orderDetailsEntity.getEndStie());
                        appointmentTime.set(orderDetailsEntity.getAppointmentTime());
                        isTimeout.set(orderDetailsEntity.getIsTimeout());
                        time.set(orderDetailsEntity.getTime());
                        timeText.set(orderDetailsEntity.getTimeText());
                        feeText.set(orderDetailsEntity.getFeeText());
                        startAddress.set(orderDetailsEntity.getStartAddress());
                        initListSuccess(orderDetailsEntity.getTravelList());
                        initCostListSuccess(orderDetailsEntity.getCostList());
                        StringBuffer billingRulesUrlSb = new StringBuffer();

                        billingRulesUrlSb.append(HttpConfig.BillingRulesUrl)
                                .append("token=")
                                .append(LoginHelper.getUserEntity().getToken())
                                .append("&businessType=").append("9")
                                .append("&orderNo=").append(orderDetailsEntity.getOrderNo())
                                .append("&lineType=")
                                .append(String.valueOf((orderDetailsEntity.getOrderType() == 1 ? 5 : 7)));


                        billingRulesUrl.set(billingRulesUrlSb.toString());
                        uc.ucOrderDetails.call();

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public void findTimeOutFee(String orderNo) {
        RetrofitFactory.sApiService.findTimeOutFee(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<CharterOrderDetailsEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(CharterOrderDetailsEntity orderDetailsEntity) {

                        isTimeout.set(orderDetailsEntity.getIsTimeout());
                        time.set(orderDetailsEntity.getTime());
                        timeText.set(orderDetailsEntity.getTimeText());
                        feeText.set(orderDetailsEntity.getFeeText());
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public void updateOrderDetailsStatus(String orderNo, String lng, String lat, int actionType, String currentAddress, String appointmentTime) {
        RetrofitFactory.sApiService.updateCharterOrderStatus(new UpdateCharterOrderBody(orderNo, String.valueOf(lng), String.valueOf(lat), String.valueOf(actionType), currentAddress, appointmentTime))
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<CharterOrderDetailsEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(CharterOrderDetailsEntity updateOrderStatusEntity) {
                        isNeedBack.set(updateOrderStatusEntity.getIsNeedBack());
                        orderStatus.set(updateOrderStatusEntity.getDriverState());
                        orderId.set(updateOrderStatusEntity.getOrderNo());
                        isLastDay.set(updateOrderStatusEntity.getIsLastDay());
                        titleText.set(updateOrderStatusEntity.getTitleText());

                        uc.ucUpdateOrderStatus.call();
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    public void CharterOrderStartLocation(String orderNo, int actionType, String appointmentTime) {
        getUC().getShowDialogEvent().call();
        ServerLocationManager.getInstance(YmxApp.getInstance()).startLocation(new ServerLocation() {
            @Override
            public void success(AMapLocation amapLocation) {
                getUC().getDismissDialogEvent().call();
                if (amapLocation != null) {
                    updateOrderDetailsStatus(orderNo, String.valueOf(amapLocation.getLongitude()), String.valueOf(amapLocation.getLatitude()), actionType, amapLocation.getAddress(), appointmentTime);
                } else {
                    AMapLocation serviceLocation = LocationManager.getInstance(YmxApp.getInstance()).getAMapLocation();
                    if (serviceLocation != null) {
                        updateOrderDetailsStatus(orderNo, String.valueOf(serviceLocation.getLongitude()), String.valueOf(serviceLocation.getLatitude()), actionType, serviceLocation.getAddress(), appointmentTime);
                    } else {
                        UIUtils.showToast("GPS信号弱，请检查定位");

                    }

                }


            }

            @Override
            public void fails() {
                getUC().getDismissDialogEvent().call();
                AMapLocation serviceLocation = LocationManager.getInstance(YmxApp.getInstance()).getAMapLocation();
                if (serviceLocation != null) {
                    updateOrderDetailsStatus(orderNo, String.valueOf(serviceLocation.getLongitude()), String.valueOf(serviceLocation.getLatitude()), actionType, serviceLocation.getAddress(), appointmentTime);
                } else {
                    UIUtils.showToast("GPS信号弱，请检查定位");
                }


            }
        });
    }

    public void initListSuccess(List<TravelListEntity> travelList) {
        itemList.clear();
        if (travelList == null || travelList.size() <= 0) {

            return;
        }

        for (TravelListEntity tripOrderList : travelList) {
            itemList.add(new CharterDayFinishItem(this, tripOrderList));
        }
    }


    public void initCostListSuccess(List<CharteredOrderListItem> costlList) {
        itemCostList.clear();
        if (costlList == null || costlList.size() <= 0) {

            return;
        }

        for (CharteredOrderListItem charteredOrderListItem : costlList) {
            itemCostList.add(new CharterCostListItemViewModel(this, charteredOrderListItem));
        }
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_QUERY_SHOW_PAY_DIALOG:

                String orderPayId = (String) event.src;
                uc.ucPayDialog.setValue(orderPayId);
                break;
            case MessageEvent.MSG_QUERY_LONG_DRIVIER_PAY_SUCCESS:

                uc.ucPaySuccess.call();

                break;

            case MessageEvent.MSG_CHARTTER_ORDER_TIME_OUT_FEE_CODE:

                CharterOrderDetailsEntity orderDetailsEntity = (CharterOrderDetailsEntity) event.src;
                isTimeout.set(orderDetailsEntity.getIsTimeout());
                time.set(orderDetailsEntity.getTime());
                timeText.set(orderDetailsEntity.getTimeText());
                feeText.set(orderDetailsEntity.getFeeText());
                break;
            case MessageEvent.MSG_UPDATE_ORDER_INFO:
                charteredOrderDetails(orderId.get());
                break;
        }

    }


}
