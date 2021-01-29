package com.ymx.driver.viewmodel.orderdetails;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.CarPoolCancalOrderEntity;
import com.ymx.driver.entity.app.LongDrivingPaySuccessEntigy;
import com.ymx.driver.entity.app.RecoverOrderEntity;
import com.ymx.driver.entity.app.UpdateTransferStationActionEntity;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.http.ResultException;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TAddObserver;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.map.ServerLocation;
import com.ymx.driver.map.ServerLocationManager;
import com.ymx.driver.util.UIUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CarPoolDetailsViewModel extends BaseViewModel {
    public ObservableField<Boolean> isLoad = new ObservableField<>();
    public ObservableField<String> phoneNum = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> passengerInfo = new ObservableField<>();
    public ObservableField<String> orderId = new ObservableField<>();
    public ObservableField<Double> lat = new ObservableField<>();
    public ObservableField<Double> lng = new ObservableField<>();
    public ObservableField<String> endName = new ObservableField<>();
    public ObservableField<String> startName = new ObservableField<>();
    public ObservableField<Integer> orderStatus = new ObservableField<>();
    public ObservableField<String> buttonText = new ObservableField<>();
    public ObservableField<String> orderTimeDesc = new ObservableField<>();

    public CarPoolDetailsViewModel(@NonNull Application application) {
        super(application);
        isLoad.set(false);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<PassengerInfoEntity> ucRecoverOrderDetails = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucChangeMap = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucPowPopShow = new SingleLiveEvent<>();
        public SingleLiveEvent<CarPoolCancalOrderEntity> ucCancalOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<CarPoolCancalOrderEntity> ucSystemCancalOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucErrorMsg = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucPayDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<LongDrivingPaySuccessEntigy> ucPaySuccess = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGoTtipList = new SingleLiveEvent<>();
    }

    public ObservableArrayList<LatLng> smoothRunningLatLng = new ObservableArrayList<>();

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    public BindingCommand load = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            recoverOrderDetails(orderId.get());
        }
    });


    public void recoverOrderDetails(String orderNo) {
        RetrofitFactory.sApiService.recoverOrderDetails(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TAddObserver<PassengerInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(PassengerInfoEntity orderDetailsEntity) {
                        isLoad.set(true);
                        initPassengerInfo(orderDetailsEntity);
                    }

                    @Override
                    protected void onFailure(String message) {
                        isLoad.set(false);
                        UIUtils.showToast(message);
                    }

                    @Override
                    protected void onFailure(ResultException e) {
                        isLoad.set(false);
                        UIUtils.showToast(e.getErrMsg());
                        if (e.getErrCode().equals("-100")) {
                            uc.ucGoTtipList.call();
                        }
                    }
                });
    }

    public void carpoolSwitchPassenger(String orderNo) {
        RetrofitFactory.sApiService.carpoolSwitchPassenger(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TAddObserver<PassengerInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(PassengerInfoEntity orderDetailsEntity) {
                        if (orderDetailsEntity.getDriverState() == 2 || orderDetailsEntity.getDriverState() == 3 || orderDetailsEntity.getDriverState() == 4) {
                            YmxCache.setOrderId(orderDetailsEntity.getOrderNo());
                        }
                        title.set(orderDetailsEntity.getTitleText());
                        orderStatus.set(orderDetailsEntity.getDriverState());
                        phoneNum.set(orderDetailsEntity.getPhone());
                        endName.set(orderDetailsEntity.getDesName());
                        startName.set(orderDetailsEntity.getSrcName());
                        lat.set(orderDetailsEntity.getLat());
                        lng.set(orderDetailsEntity.getLng());
                        orderId.set(orderDetailsEntity.getOrderNo());
                        buttonText.set(orderDetailsEntity.getButtonText());
                        if (!TextUtils.isEmpty(orderDetailsEntity.getPhone())) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("尾号");
                            sb.append(orderDetailsEntity.getPhone().substring(7));
                            passengerInfo.set(sb.toString());
                        }
                        uc.ucRecoverOrderDetails.setValue(orderDetailsEntity);
                        uc.ucPowPopShow.call();
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }

                    @Override
                    protected void onFailure(ResultException e) {
                        UIUtils.showToast(e.getErrMsg());
                    }
                });
    }


    public void carpoolUpdateStatus(String orderNo, Double lng, Double lat, int actionType, String currentAddress) {
        RetrofitFactory.sApiService.carpoolAction(new UpdateTransferStationActionEntity(orderNo, lng, lat, actionType, currentAddress))
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TAddObserver<PassengerInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(PassengerInfoEntity orderDetailsEntity) {
                        if (orderDetailsEntity.getDriverState() == 2 || orderDetailsEntity.getDriverState() == 3 || orderDetailsEntity.getDriverState() == 4) {
                            YmxCache.setOrderId(orderDetailsEntity.getOrderNo());
                        }
                        initPassengerInfo(orderDetailsEntity);

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }

                    @Override
                    protected void onFailure(ResultException e) {
                        UIUtils.showToast(e.getErrMsg());
                        uc.ucGoTtipList.call();
                    }
                });
    }

    public void carpoolCanCalOrder(String orderNo) {
        RetrofitFactory.sApiService.carpoolCancelOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TAddObserver<CarPoolCancalOrderEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(CarPoolCancalOrderEntity carPoolCancalOrderEntity) {
                        uc.ucCancalOrder.setValue(carPoolCancalOrderEntity);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }

                    @Override
                    protected void onFailure(ResultException e) {
                        UIUtils.showToast(e.getErrMsg());
                        uc.ucGoTtipList.call();
                    }
                });
    }


    public void driverCarpoolEndTrip(String orderNo, Double lng, Double lat) {
        RetrofitFactory.sApiService.driverCarpoolEndTrip(orderNo, lng, lat)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TAddObserver<PassengerInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(PassengerInfoEntity orderDetailsEntity) {
                        YmxCache.setOrderId("");
                        uc.ucBack.call();
                    }

                    @Override
                    protected void onFailure(String message) {
//                        UIUtils.showToast(message);


                        UIUtils.showToast(message);


                    }

                    @Override
                    protected void onFailure(ResultException e) {
                        if (e.getErrMsg().contains("乘客")) {
                            uc.ucErrorMsg.setValue(e.getErrMsg());
                        } else {
                            UIUtils.showToast(e.getErrMsg());
                        }


                    }
                });
    }


    public void initPassengerInfo(PassengerInfoEntity orderDetailsEntity) {
        title.set(orderDetailsEntity.getTitleText());
        orderStatus.set(orderDetailsEntity.getDriverState());
        phoneNum.set(orderDetailsEntity.getPhone());
        endName.set(orderDetailsEntity.getDesName());
        startName.set(orderDetailsEntity.getSrcName());
        lat.set(orderDetailsEntity.getLat());
        lng.set(orderDetailsEntity.getLng());
        orderId.set(orderDetailsEntity.getOrderNo());
        buttonText.set(orderDetailsEntity.getButtonText());
        if (!TextUtils.isEmpty(orderDetailsEntity.getPhone())) {
            StringBuffer sb = new StringBuffer();
            sb.append("尾号");
            sb.append(orderDetailsEntity.getPhone().substring(7));
            passengerInfo.set(sb.toString());
        }
        uc.ucRecoverOrderDetails.setValue(orderDetailsEntity);
    }

    public void orderStartLocation(String orderNo, int actionType) {
        getUC().getShowDialogEvent().call();
        ServerLocationManager.getInstance(YmxApp.getInstance()).startLocation(new ServerLocation() {
            @Override
            public void success(AMapLocation amapLocation) {
                getUC().getDismissDialogEvent().call();
                if (amapLocation != null) {
                    carpoolUpdateStatus(orderNo, amapLocation.getLongitude(), amapLocation.getLatitude(), actionType, "");

                } else {
                    AMapLocation serviceLocation = LocationManager.getInstance(YmxApp.getInstance()).getAMapLocation();

                    if (serviceLocation != null) {
                        carpoolUpdateStatus(orderNo, serviceLocation.getLongitude(), serviceLocation.getLatitude(), actionType, "");
                    } else {
                        UIUtils.showToast("GPS信号弱，请检查当前信号");
                    }


                }

                ServerLocationManager.getInstance(YmxApp.getInstance()).onDestroy();

            }

            @Override
            public void fails() {
                getUC().getDismissDialogEvent().call();
                AMapLocation serviceLocation = LocationManager.getInstance(YmxApp.getInstance()).getAMapLocation();


                if (serviceLocation != null) {
                    carpoolUpdateStatus(orderNo, serviceLocation.getLongitude(), serviceLocation.getLatitude(), actionType, "");
                } else {
                    UIUtils.showToast("GPS信号弱，请检查当前信号");
                }

                ServerLocationManager.getInstance(YmxApp.getInstance()).onDestroy();
            }
        });
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_CAR_POOL_CANCAL_ORDER_CODE:


                uc.ucSystemCancalOrder.setValue((CarPoolCancalOrderEntity) event.src);

                break;

            // 司机支付弹窗
            case MessageEvent.MSG_QUERY_LONG_DRIVIER_PAY_SUCCESS:
                LongDrivingPaySuccessEntigy paySuccessEntigy = (LongDrivingPaySuccessEntigy) event.src;
                uc.ucPaySuccess.setValue(paySuccessEntigy);

                break;
            case MessageEvent.MSG_QUERY_SHOW_PAY_DIALOG:

                String orderPayId = (String) event.src;
                uc.ucPayDialog.setValue(orderPayId);
                break;

            case MessageEvent.MSG_CAR_POOL_RECOVER_ORDER_CODE:
                RecoverOrderEntity recoverOrderEntity = (RecoverOrderEntity) event.src;
                if (recoverOrderEntity != null) {
                    recoverOrderDetails(recoverOrderEntity.getDriverOrderNo());
                }

                break;


        }
    }


}
