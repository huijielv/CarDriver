package com.ymx.driver.viewmodel.travel;

import android.app.Application;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.BaseEntity;
import com.ymx.driver.entity.app.CancelOrderEntity;
import com.ymx.driver.entity.app.TransferOrderResultEntity;
import com.ymx.driver.entity.app.UpdateOrderStatusEntity;
import com.ymx.driver.entity.app.UpdateStartAddressEntity;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.http.ResultException;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TAddObserver;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.map.ServerLocation;
import com.ymx.driver.map.ServerLocationManager;
import com.ymx.driver.tts.BaiduSpeech;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.util.VoicePlayMannager;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xuweihua
 * 2020/5/14
 */
public class TravelViewModel extends BaseViewModel {

    public TravelViewModel(@NonNull Application application) {
        super(application);
        isLoad.set(false);
    }


    public static final int MODE_SELECT_POINT = 0;

    /**
     * 1-行程待开始
     */
    public static final int DRIVER_STATE_TO_START = 1;
    /**
     * 2-去接乘客
     */
    public static final int DRIVER_STATE_TO_PASSENGERS = 2;
    /**
     * 3-准备出发
     */
    public static final int DRIVER_STATE_READY_TO_GO = 3;
    /**
     * 4-行程中
     */
    public static final int DRIVER_STATE_ROADING = 4;
    /**
     * 5-确认费用
     */
    public static final int DRIVER_STATE_CONFIRM_COST = 5;
    /**
     * 6-待付款
     */
    public static final int DRIVER_STATE_TO_PAY = 6;

    /**
     * 7 已完成
     */
    public static final int DRIVER_ORDER_FINISH = 7;

    /**
     * 8 已关闭
     */
    public static final int DRIVER_ORDER_CLOSED = 8;


    public static final String CANCAL_ORDER_STATUS = "1111";
    public ObservableField<Boolean> isLoad = new ObservableField<>();
    public ObservableField<String> phoneNum = new ObservableField<>();
    public ObservableField<Integer> businessType = new ObservableField<>();
    public ObservableField<UpdateOrderStatusEntity> updateOrderStatusEntityObservableField = new ObservableField<>();
    public ObservableField<String> passengerInfo = new ObservableField<>();
    public ObservableField<String> updateOrderContent = new ObservableField<>();
    public ObservableField<String> orderId = new ObservableField<>();
    public ObservableField<String> endName = new ObservableField<>();
    public ObservableField<String> startName = new ObservableField<>();
    public ObservableField<Integer> orderStatus = new ObservableField<>();
    public ObservableField<String> orderTimeDesc = new ObservableField<>();
    public ObservableField<String> timeDescribe = new ObservableField<>();
    public ObservableField<Double> lat = new ObservableField<>();
    public ObservableField<Double> lng = new ObservableField<>();
    public ObservableField<Integer> phoneOrder = new ObservableField<>();
    public ObservableField<Integer> channel = new ObservableField<>();
    public ObservableField<Integer> driverType = new ObservableField<>();
    public ObservableField<String> rightTitle = new ObservableField<>();
    public ObservableField<Boolean> rightTitleShow = new ObservableField<>();
    public ObservableField<Integer> transferOrderState = new ObservableField<>(0);
    public ObservableField<String> transferOrderTips = new ObservableField<>();


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCall = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucNavigate = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCancelOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucUpdateOrderStatus = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucRecoverOrderDetails = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucTransferOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucTransferOrderFails = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucTransferOrderSuccess = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCancelTransferOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<CancelOrderEntity> ucCancelOrderSuccess = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucChangeMap = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucFailsMsg = new SingleLiveEvent<>();
        public SingleLiveEvent<UpdateStartAddressEntity> ucUpdateAddress= new SingleLiveEvent<>();
    }


    public ObservableInt mode = new ObservableInt(TravelViewModel.DRIVER_STATE_TO_PASSENGERS);

    public ObservableArrayList<LatLng> smoothRunningLatLng = new ObservableArrayList<>();


    public String getModeTitle(int mode) {
        if (mode == TravelViewModel.DRIVER_STATE_TO_START) {
            return "行程待开始";
        } else if (mode == TravelViewModel.DRIVER_STATE_TO_PASSENGERS) {
            return UIUtils.getString(R.string.driver_start_go);
        } else if (mode == TravelViewModel.DRIVER_STATE_READY_TO_GO) {
            return UIUtils.getString(R.string.driver_arrive_place);
        } else if (mode == TravelViewModel.DRIVER_STATE_ROADING) {
            return UIUtils.getString(R.string.driver_start_roading);
        } else if (mode == TravelViewModel.DRIVER_STATE_CONFIRM_COST) {
            return UIUtils.getString(R.string.driver_start_confirm_cost);
        } else {
            return "";
        }
    }

    public boolean getCanStatus(int mode) {
        if (mode == TravelViewModel.DRIVER_STATE_TO_START) {
            return true;
        } else if (mode == TravelViewModel.DRIVER_STATE_TO_PASSENGERS) {
            return true;
        } else if (mode == TravelViewModel.DRIVER_STATE_READY_TO_GO) {
            return true;
        } else if (mode == TravelViewModel.DRIVER_STATE_ROADING) {
            return false;
        } else if (mode == TravelViewModel.DRIVER_STATE_CONFIRM_COST) {
            return false;
        } else {
            return false;
        }
    }

    public boolean getBackStatus(int mode) {
        if (mode == TravelViewModel.DRIVER_STATE_TO_START) {
            return false;
        } else if (mode == TravelViewModel.DRIVER_STATE_TO_PASSENGERS) {
            return false;
        } else if (mode == TravelViewModel.DRIVER_STATE_READY_TO_GO) {
            return false;
        } else if (mode == TravelViewModel.DRIVER_STATE_ROADING) {
            return false;
        } else if (mode == TravelViewModel.DRIVER_STATE_CONFIRM_COST) {
            return false;
        } else {
            return false;
        }
    }

    public void initRightTitleShow(int mode) {
        if (mode == TravelViewModel.DRIVER_STATE_TO_START) {
            rightTitleShow.set(true);
        } else if (mode == TravelViewModel.DRIVER_STATE_TO_PASSENGERS) {
            rightTitleShow.set(true);
        } else if (mode == TravelViewModel.DRIVER_STATE_READY_TO_GO) {
            rightTitleShow.set(true);
        } else if (mode == TravelViewModel.DRIVER_STATE_ROADING) {
            rightTitleShow.set(false);
        } else if (mode == TravelViewModel.DRIVER_STATE_CONFIRM_COST) {
            rightTitleShow.set(false);
        } else {
            rightTitleShow.set(true);
        }
    }


    public void initRightTitleStatus(int code, int businessType, int transferOrderState) {
        if (businessType == 1 && code == 2) {
            rightTitle.set((transferOrderState == 0 ? "转单" : "取消转单"));
        } else {
            rightTitle.set("取消");
        }
    }


    private String locationInfo(AMapLocation aMapLocation) {
        StringBuffer sb = new StringBuffer();
        sb.append(aMapLocation.getLongitude()
        ).append(",").append(aMapLocation.getLatitude());
        return sb.toString();
    }


    public void updateButton(UpdateOrderStatusEntity updateOrderStatusEntity, boolean isVoice) {

        updateOrderStatusEntityObservableField.set(updateOrderStatusEntity);
        updateOrderContent.set(updateOrderStatusEntity.getButtonText());
        mode.set(updateOrderStatusEntity.getDriverState());
        orderStatus.set(updateOrderStatusEntity.getDriverState());
        orderId.set(updateOrderStatusEntity.getOrderNo());
        phoneOrder.set(updateOrderStatusEntity.getChannel());
        if (updateOrderStatusEntity.getDriverState() == TravelViewModel.DRIVER_STATE_TO_PASSENGERS) {
            YmxCache.setOrderId(updateOrderStatusEntity.getOrderNo());
            initRightTitleStatus(orderStatus.get(), businessType.get(), transferOrderState.get());
            if (businessType.get() == 1 && updateOrderStatusEntity.getCancalNumber() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(getApplication().getString(R.string.transfer_order_hiht_number));
                stringBuffer.append(String.valueOf(updateOrderStatusEntity.getCancalNumber()));
                stringBuffer.append("次");
                BaiduSpeech.getInstance(YmxApp.getInstance()).playText(stringBuffer.toString(),null);
            }
        } else if (updateOrderStatusEntity.getDriverState() == TravelViewModel.DRIVER_STATE_READY_TO_GO) {
            if (isVoice) {
                VoicePlayMannager.getInstance(getApplication()).play(R.raw.arrive_passenger);
            }
            initRightTitleStatus(orderStatus.get(), businessType.get(), transferOrderState.get());
            YmxCache.setOrderId(updateOrderStatusEntity.getOrderNo());


        } else if (updateOrderStatusEntity.getDriverState() == TravelViewModel.DRIVER_STATE_ROADING) {
            startName.set(updateOrderStatusEntity.getDesName());
            lat.set(updateOrderStatusEntity.getDesLat());
            lng.set(updateOrderStatusEntity.getDesLng());
            if (businessType.get() == 10 || (businessType.get() == 5 && driverType.get() == 6)) {
                orderTimeDesc.set("");
            }

            if (updateOrderStatusEntity.getChannel() == 6) {
                uc.ucChangeMap.call();
            }
            YmxCache.setOrderId(updateOrderStatusEntity.getOrderNo());
        } else if (updateOrderStatusEntity.getDriverState() == TravelViewModel.DRIVER_STATE_CONFIRM_COST) {
            YmxCache.setOrderId("");


        }
        uc.ucUpdateOrderStatus.call();
    }

    public void orderStartLocation(String orderNo, int actionType, String appointmentTime) {
        getUC().getShowDialogEvent().call();
        ServerLocationManager.getInstance(YmxApp.getInstance()).startLocation(new ServerLocation() {
            @Override
            public void success(AMapLocation amapLocation) {
                getUC().getDismissDialogEvent().call();
                if (amapLocation != null) {


                    updateOrderDetailsStatus(orderNo, locationInfo(amapLocation), String.valueOf(actionType), "");
                } else {
                    AMapLocation serviceLocation = LocationManager.getInstance(YmxApp.getInstance()).getAMapLocation();

                    if (serviceLocation != null) {
                        updateOrderDetailsStatus(orderNo, locationInfo(serviceLocation), String.valueOf(actionType), "");
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
                    updateOrderDetailsStatus(orderNo, locationInfo(serviceLocation), String.valueOf(actionType), "");
                } else {
                    UIUtils.showToast("GPS信号弱，请检查当前信号");
                }

                ServerLocationManager.getInstance(YmxApp.getInstance()).onDestroy();
            }
        });
    }


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

    public BindingCommand cancelOrder = new BindingCommand(new BindingAction() {
        @Override
        public void call() {


            if (orderStatus != null && orderStatus.get() != null && businessType != null && businessType.get() != null) {
                if (orderStatus.get() == 2 && businessType.get() == 1) {
                    switch (transferOrderState.get()) {
                        case 1:
                            cancelTransferOrder(orderId.get());
                            break;
                        case 0:
                            uc.ucTransferOrder.call();

                            break;
                    }


                } else {
                    uc.ucCancelOrder.call();
                }

            } else {
                UIUtils.showToast("网络不好，请稍后再试");
            }

        }
    });


    public void updateOrderDetailsStatus(String orderNo, String currentCoord, String actionType, String otherCharge) {
        RetrofitFactory.sApiService.updateOrderDetailsStatus(orderNo, currentCoord, actionType, otherCharge)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TAddObserver<UpdateOrderStatusEntity>() {
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
                        initRightTitleShow(updateOrderStatusEntity.getDriverState());
                        updateButton(updateOrderStatusEntity, true);

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }

                    @Override
                    protected void onFailure(ResultException e) {
                        uc.ucFailsMsg.setValue(e.getErrMsg());
                    }
                });
    }


    public void cancelOrder(String orderNo) {
        RetrofitFactory.sApiService.cancelOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TAddObserver<CancelOrderEntity>() {
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
                        YmxCache.setOrderId("");

                        uc.ucCancelOrderSuccess.setValue(s);


                    }

                    @Override
                    protected void onFailure(String message) {


                        // 1111 状态告知司机订单已经被取消
                        if (CANCAL_ORDER_STATUS.equals(message)) {
                            YmxCache.setOrderId("");
                            UIUtils.showToast("订单已被乘客取消");
                            uc.ucBack.call();

                        } else {
                            UIUtils.showToast(message);
                        }


                    }

                    @Override
                    protected void onFailure(ResultException e) {
                        uc.ucFailsMsg.setValue(e.getErrMsg());
                    }
                });
    }


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

                        mode.set(orderDetailsEntity.getDriverState());
                        orderStatus.set(orderDetailsEntity.getDriverState());
                        businessType.set(orderDetailsEntity.getBusinessType());
                        driverType.set(orderDetailsEntity.getDriverType());
                        endName.set(orderDetailsEntity.getDesName());
                        transferOrderState.set(orderDetailsEntity.getTransferOrderState());
                        transferOrderTips.set(orderDetailsEntity.getTransferOrderTips());
                        initRightTitleShow(orderDetailsEntity.getDriverState());
                        initRightTitleStatus(orderDetailsEntity.getDriverState(), orderDetailsEntity.getBusinessType(), orderDetailsEntity.getTransferOrderState());
                        if (orderDetailsEntity.getDriverState() == DRIVER_STATE_ROADING || orderDetailsEntity.getDriverState() == DRIVER_STATE_CONFIRM_COST) {
                            startName.set(orderDetailsEntity.getDesName());
                        } else {
                            startName.set(orderDetailsEntity.getSrcName());
                        }

                        phoneNum.set(orderDetailsEntity.getPhone());
                        channel.set(orderDetailsEntity.getChannel());
                        updateOrderContent.set(orderDetailsEntity.getButtonText());
                        timeDescribe.set(orderDetailsEntity.getTimeDescribe());
                        lat.set(orderDetailsEntity.getLat());
                        lng.set(orderDetailsEntity.getLng());
                        orderId.set(orderDetailsEntity.getOrderNo());
                        if (!TextUtils.isEmpty(orderDetailsEntity.getPhone())) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("尾号");
                            sb.append(orderDetailsEntity.getPhone().substring(7));
                            passengerInfo.set(sb.toString());
                        }
                        isLoad.set(true);
                        uc.ucRecoverOrderDetails.call();
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    public void transferOrder(String orderNo) {
        RetrofitFactory.sApiService.transferOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<TransferOrderResultEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(TransferOrderResultEntity transferOrderResultEntity) {

                        transferOrderState.set(transferOrderResultEntity.getTransferOrderState());
                        transferOrderTips.set(transferOrderResultEntity.getTransferOrderTips());
                        initRightTitleStatus(orderStatus.get(), businessType.get(), transferOrderResultEntity.getTransferOrderState());
                        uc.ucTransferOrderSuccess.call();

                    }

                    @Override
                    protected void onFailure(String message) {

                        uc.ucTransferOrderFails.setValue(message);
                    }
                });
    }

    public void cancelTransferOrder(String orderNo) {
        RetrofitFactory.sApiService.cancelTransferOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<TransferOrderResultEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(TransferOrderResultEntity transferOrderResultEntity) {

                        transferOrderState.set(transferOrderResultEntity.getTransferOrderState());
                        initRightTitleStatus(orderStatus.get(), businessType.get(), transferOrderResultEntity.getTransferOrderState());
                        uc.ucTransferOrderSuccess.call();
                    }

                    @Override
                    protected void onFailure(String message) {

                        uc.ucTransferOrderFails.setValue(message);
                    }
                });
    }

    public void updateStartAddress(String orderNo , int state) {
        RetrofitFactory.sApiService.updateStartAddress(orderNo, state)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<BaseEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(BaseEntity baseEntity) {
                        recoverOrderDetails(orderId.get());
                    }

                    @Override
                    protected void onFailure(String message) {

                        uc.ucTransferOrderFails.setValue(message);
                    }
                });
    }





    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {

            // 导航变更数据
            case MessageEvent.MSG_NAVI_UPDATE_BUTTON_CODE:
                UpdateOrderStatusEntity updateOrderStatusEntity = (UpdateOrderStatusEntity) event.src;


                if (updateOrderStatusEntity.getDriverState() == DRIVER_STATE_CONFIRM_COST) {
                    uc.ucBack.call();

                    return;
                }

                if (updateOrderStatusEntity != null) {
                    updateButton(updateOrderStatusEntity, false);
                }

//
                break;
            case MessageEvent.MSG_TRANSFER_ORDER_SUCCESS_CODE:
                uc.ucBack.call();

                break;
            case MessageEvent.MSG_MINE_ORDER_UPDATE_ADDRESSS_TYPE_CODE :
                UpdateStartAddressEntity updateStartAddressEntity = (UpdateStartAddressEntity) event.src;
                if (updateStartAddressEntity!=null){
                    uc. ucUpdateAddress.setValue(updateStartAddressEntity);
                }

                break;




        }
    }

}
