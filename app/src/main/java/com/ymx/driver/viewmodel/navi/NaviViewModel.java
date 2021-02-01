package com.ymx.driver.viewmodel.navi;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.amap.api.location.AMapLocation;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.OrderPriceEntity;
import com.ymx.driver.entity.app.UpdateOrderStatusEntity;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.map.ServerLocation;
import com.ymx.driver.map.ServerLocationManager;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.util.VoicePlayMannager;
import com.ymx.driver.viewmodel.travel.TravelViewModel;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NaviViewModel extends BaseViewModel {
    public NaviViewModel(@NonNull Application application) {
        super(application);
    }

    public static final int DRIVER_STATE_ROADING = 4;
    public static final int DRIVER_STATE_CONFIRM_COST = 5;

    public ObservableField<String> updateOrderContent = new ObservableField<>();
    public ObservableField<String> orderId = new ObservableField<>();
    public ObservableField<Integer> orderStatus = new ObservableField<>();
    public ObservableField<Double> lat = new ObservableField<>();
    public ObservableField<Double> lng = new ObservableField<>();
    public ObservableField<Integer> channel = new ObservableField<>();
    public ObservableField<Integer> transferOrderState = new ObservableField<>(0);
    public ObservableField<String> transferOrderTips = new ObservableField<>();
    public ObservableField<Integer> categoryType = new ObservableField<>();

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucUpdateOrderStatus = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucRecoverOrderDetails = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucChangeMap = new SingleLiveEvent<>();
        public SingleLiveEvent<OrderPriceEntity> ucOrderPrice = new SingleLiveEvent<>();
    }


    public void recoverOrderDetails(String orderNo) {
        RetrofitFactory.sApiService.recoverOrderDetails(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<PassengerInfoEntity>() {
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
                        transferOrderState.set(orderDetailsEntity.getTransferOrderState());
                        orderStatus.set(orderDetailsEntity.getDriverState());
                        updateOrderContent.set(orderDetailsEntity.getButtonText());
                        channel.set(orderDetailsEntity.getChannel());
                        orderId.set(orderDetailsEntity.getOrderNo());

                        uc.ucRecoverOrderDetails.call();
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

                        updateOrderContent.set(updateOrderStatusEntity.getButtonText());
                        orderStatus.set(updateOrderStatusEntity.getDriverState());
                        orderId.set(updateOrderStatusEntity.getOrderNo());

                        if (updateOrderStatusEntity.getDriverState() == TravelViewModel.DRIVER_STATE_TO_PASSENGERS) {
                            YmxCache.setOrderId(updateOrderStatusEntity.getOrderNo());

                        } else if (updateOrderStatusEntity.getDriverState() == TravelViewModel.DRIVER_STATE_READY_TO_GO) {
//                            VoicePlayMannager.getInstance(getApplication()).play(R.raw.arrive_passenger);
                            YmxCache.setOrderId(updateOrderStatusEntity.getOrderNo());


                        } else if (updateOrderStatusEntity.getDriverState() == TravelViewModel.DRIVER_STATE_ROADING) {

                            lat.set(updateOrderStatusEntity.getDesLat());
                            lng.set(updateOrderStatusEntity.getDesLng());
                            if (updateOrderStatusEntity.getChannel() == 6) {
                                uc.ucChangeMap.call();
                            }
                            YmxCache.setOrderId(updateOrderStatusEntity.getOrderNo());
                        } else if (updateOrderStatusEntity.getDriverState() == TravelViewModel.DRIVER_STATE_CONFIRM_COST) {
                            YmxCache.setOrderId("");


                        }
                        uc.ucUpdateOrderStatus.call();
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_NAVI_UPDATE_BUTTON_CODE, updateOrderStatusEntity));


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
            case MessageEvent.MSG_CODE_UPDATE_ORDER_PRICE_CODE:
                OrderPriceEntity orderPriceEntity = (OrderPriceEntity) event.src;
                uc.ucOrderPrice.setValue(orderPriceEntity);

                break;
        }
    }

    public void orderStartLocation(String orderNo, int actionType, String appointmentTime) {
        getUC().getShowDialogEvent().call();
        ServerLocationManager.getInstance(YmxApp.getInstance()).startLocation(new ServerLocation() {
            @Override
            public void success(AMapLocation amapLocation) {
                getUC().getDismissDialogEvent().call();
                if (amapLocation != null) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(amapLocation.getLongitude()
                    ).append(",").append(amapLocation.getLatitude());

                    updateOrderDetailsStatus(orderNo, sb.toString(), String.valueOf(actionType), "");
                } else {

                    AMapLocation serviceLocation = LocationManager.getInstance(YmxApp.getInstance()).getAMapLocation();
                    if (serviceLocation !=null ){
                        StringBuffer sb = new StringBuffer();
                        sb.append(serviceLocation.getLongitude()
                        ).append(",").append(serviceLocation.getLatitude());

                        updateOrderDetailsStatus(orderNo, sb.toString(), String.valueOf(actionType), "");
                    }else {
                        UIUtils.showToast("GPS信号弱，请检查当前信号");
                    }

                }

                ServerLocationManager.getInstance(YmxApp.getInstance()).onDestroy();

            }

            @Override
            public void fails() {
                getUC().getDismissDialogEvent().call();

                AMapLocation serviceLocation = LocationManager.getInstance(YmxApp.getInstance()).getAMapLocation();
                if (serviceLocation!=null){
                    StringBuffer sb = new StringBuffer();
                    sb.append(serviceLocation.getLongitude()
                    ).append(",").append(serviceLocation.getLatitude());

                    updateOrderDetailsStatus(orderNo, sb.toString(), String.valueOf(actionType), "");
                }else {
                    UIUtils.showToast("GPS信号弱，请检查当前信号");
                }

                ServerLocationManager.getInstance(YmxApp.getInstance()).onDestroy();
            }
        });
    }
}
