package com.ymx.driver.viewmodel.chartercar;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.CharterCancalOrder;
import com.ymx.driver.entity.app.CharterOrderDetailsEntity;
import com.ymx.driver.entity.app.GetCharterSystemTime;
import com.ymx.driver.entity.app.UpdateCharterOrderBody;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.map.ServerLocation;
import com.ymx.driver.map.ServerLocationManager;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.travel.TravelViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChartercarOrderDetailsViewModel extends BaseViewModel {


    public ChartercarOrderDetailsViewModel(@NonNull Application application) {
        super(application);
        isLoad.set(false);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCall = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucNavigate = new SingleLiveEvent<>();
        public SingleLiveEvent<CharterCancalOrder> ucCancelOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucUpdateOrderStatus = new SingleLiveEvent<>();
        public SingleLiveEvent<CharterOrderDetailsEntity> ucRecoverOrderDetails = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucQrcode = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucPayDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucPaySuccess = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCharterWait = new SingleLiveEvent<>();

        public SingleLiveEvent<GetCharterSystemTime > ucCharterSystemTime= new SingleLiveEvent<>();


    }


    public ObservableField<Boolean> isLoad = new ObservableField<>(true);
    public ObservableField<String> phoneNum = new ObservableField<>();
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
    public ObservableField<String> callCustomerPhone = new ObservableField<>();
    public ObservableField<String> appointmentTime = new ObservableField<>();
    public ObservableArrayList<LatLng> smoothRunningLatLng = new ObservableArrayList<>();

    public ObservableField<String> nextStartTime = new ObservableField<>();
    public ObservableField<Integer> nextDay = new ObservableField<>();


    public BindingCommand load = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            recoverCharteredOrderDetails(orderId.get());

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
            phoneNum.set(callCustomerPhone.get());
            uc.ucCall.call();
        }
    });
    public BindingCommand callCustomerService = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            UserEntity userEntity = LoginHelper.getUserEntity();
            phoneNum.set(userEntity.getServicePhone());
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

    public BindingCommand qWait = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucCharterWait.call();
        }
    });


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

                        orderStatus.set(updateOrderStatusEntity.getDriverState());
                        orderId.set(updateOrderStatusEntity.getOrderNo());
                        isLastDay.set(updateOrderStatusEntity.getIsLastDay());
                        titleText.set(updateOrderStatusEntity.getTitleText());


                        if (updateOrderStatusEntity.getDriverState() == TravelViewModel.DRIVER_STATE_TO_PASSENGERS) {
                            YmxCache.setOrderId(updateOrderStatusEntity.getOrderNo());

                        } else if (updateOrderStatusEntity.getDriverState() == TravelViewModel.DRIVER_STATE_ROADING) {

                            YmxCache.setOrderId(updateOrderStatusEntity.getOrderNo());
                        } else if (updateOrderStatusEntity.getDriverState() == TravelViewModel.DRIVER_ORDER_FINISH) {
                            YmxCache.setOrderId("");
                        }
                        uc.ucUpdateOrderStatus.call();
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public void recoverCharteredOrderDetails(String orderNo) {
        RetrofitFactory.sApiService.recoverCharteredOrderDetails(orderNo)
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
                        callCustomerPhone.set(orderDetailsEntity.getPhone());
                        appointmentTime.set(orderDetailsEntity.getAppointmentTime());
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
                        nextStartTime.set(orderDetailsEntity.getNextStartTime());
                        nextDay.set(orderDetailsEntity.getNextDay());
                        uc.ucRecoverOrderDetails.setValue(orderDetailsEntity);
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
            case MessageEvent.MSG_QUERY_SHOW_PAY_DIALOG:

                String orderPayId = (String) event.src;
                uc.ucPayDialog.setValue(orderPayId);
                break;
            case MessageEvent.MSG_QUERY_LONG_DRIVIER_PAY_SUCCESS:

                uc.ucPaySuccess.call();

                break;

            case MessageEvent.MSG_GOTO_CHARTTER_SELECT_TIME_CODE:

                String selectTime = (String) event.src;
                CharterOrderStartLocation(orderId.get(), 4, selectTime);
                break;

            case MessageEvent.MSG_GOTO_CHARTTER_CANCAL_ORDER_CODE:

                CharterCancalOrder charterCancalOrder = (CharterCancalOrder) event.src;
                uc.ucCancelOrder.setValue(charterCancalOrder);

                break;
            case MessageEvent.MSG_UPDATE_ORDER_INFO:
                recoverCharteredOrderDetails(orderId.get());
                break;
        }
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
                    updateOrderDetailsStatus(orderNo, String.valueOf(serviceLocation.getLongitude()), String.valueOf(serviceLocation.getLatitude()), actionType, serviceLocation.getAddress(), appointmentTime);
                }

                ServerLocationManager.getInstance(YmxApp.getInstance()).onDestroy();

            }

            @Override
            public void fails() {
                getUC().getDismissDialogEvent().call();
                AMapLocation serviceLocation = LocationManager.getInstance(YmxApp.getInstance()).getAMapLocation();
                updateOrderDetailsStatus(orderNo, String.valueOf(serviceLocation.getLongitude()), String.valueOf(serviceLocation.getLatitude()), actionType, serviceLocation.getAddress(), appointmentTime);
                ServerLocationManager.getInstance(YmxApp.getInstance()).onDestroy();
            }
        });
    }

    public void getCharterSystemTime(String orderNo){

        RetrofitFactory.sApiService.getCharterSystemTime(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<GetCharterSystemTime>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(GetCharterSystemTime getCharterSystemTime) {
                        uc.ucCharterSystemTime.setValue(getCharterSystemTime);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


}
