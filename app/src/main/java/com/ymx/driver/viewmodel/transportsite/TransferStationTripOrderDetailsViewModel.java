package com.ymx.driver.viewmodel.transportsite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.amap.api.location.AMapLocation;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.BaseEntity;
import com.ymx.driver.entity.TransferCancalOrderInfo;
import com.ymx.driver.entity.app.LongDrivingPaySuccessEntigy;
import com.ymx.driver.entity.app.TransferCancalOrderEntity;
import com.ymx.driver.entity.app.TransferOrderCancalEntity;
import com.ymx.driver.entity.app.TransferOrderUpdateTime;
import com.ymx.driver.entity.app.TransferStationRecoverEntity;
import com.ymx.driver.entity.app.TransferStationRecoverLiteItemEntity;
import com.ymx.driver.entity.app.UpdateTransferStationActionEntity;
import com.ymx.driver.http.ResultException;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TAddObserver;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.map.LocationManager;
import com.ymx.driver.map.ServerLocation;
import com.ymx.driver.map.ServerLocationManager;
import com.ymx.driver.util.DateUtils;
import com.ymx.driver.util.UIUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class TransferStationTripOrderDetailsViewModel extends BaseViewModel {
    public ObservableField<Boolean> isLoad = new ObservableField<>(true);
    public ObservableField<String> orderNo = new ObservableField<>();
    public ObservableField<String> passengerOrderNo = new ObservableField<>();
    public ObservableField<String> titleText = new ObservableField<>();
    public ObservableField<String> buttonText = new ObservableField<>();
    public ObservableField<Integer> orderType = new ObservableField<>();
    public ObservableField<Integer> grabOrderShow = new ObservableField<>(0);
    public ObservableField<Integer> driverState = new ObservableField<>();
    public ObservableField<String> passengerPhone = new ObservableField<>();
    public ObservableField<Integer> sideId = new ObservableField<>();
    public ObservableList<TransferStationTripOrderDetailsListItem> itemList = new ObservableArrayList<>();
    public ItemBinding<TransferStationTripOrderDetailsListItem> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.transfer_station_trip_order_details_list_item_ll);

    public UIChangeObservable uc = new UIChangeObservable();


    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucRefreshOrderDetails = new SingleLiveEvent<>();
        /// 更新司机详情
        public SingleLiveEvent<Void> ucOrderDetailsSuccess = new SingleLiveEvent<>();
        // 乘客电话
        public SingleLiveEvent<Void> ucPassengerPhone = new SingleLiveEvent<>();
        // 司機取消訂單

        public SingleLiveEvent<TransferCancalOrderEntity> ucTransferCancalOrderSuccess = new SingleLiveEvent<>();
        public SingleLiveEvent<TransferCancalOrderInfo> ucTransferCancalOrder = new SingleLiveEvent<>();
        // 乘客取消訂單
        public SingleLiveEvent<TransferOrderCancalEntity> ucShowCancalOrderDialog = new SingleLiveEvent<>();
        // 刷新订单
        public SingleLiveEvent<Boolean> ucRefreshOrder = new SingleLiveEvent<>();
        // 继续抢单
        public SingleLiveEvent<Void> ucGrabOrder = new SingleLiveEvent<>();
        // 时间选择
        public SingleLiveEvent<Void> ucShowSelectTime = new SingleLiveEvent<>();
        //乘客同意司机修改接驾时间，通知司机弹窗
        public SingleLiveEvent<TransferOrderUpdateTime> ucShowUpdateTime = new SingleLiveEvent<>();
        //点击导航处理
        public SingleLiveEvent<TransferStationRecoverLiteItemEntity> ucNavi = new SingleLiveEvent<>();
        //更改乘客状态
        public SingleLiveEvent<TransferStationRecoverLiteItemEntity> ucUpdatePassengerType = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucErrorMsg = new SingleLiveEvent<>();
        public SingleLiveEvent<TransferStationRecoverLiteItemEntity> ucShowQrCode = new SingleLiveEvent<>();
        // 支付成功的处理
        public SingleLiveEvent<LongDrivingPaySuccessEntigy> ucPaySuccess = new SingleLiveEvent<>();
        // 展示微信支付和支付宝支付
        public SingleLiveEvent<String> ucPayDialog = new SingleLiveEvent<>();


        public SingleLiveEvent<TransferCancalOrderEntity> ucDriverCancanOrder = new SingleLiveEvent<>();
        //订单取消MQTT 没接收到
        public SingleLiveEvent<Void> ucCancanOrderFails = new SingleLiveEvent<>();
    }


    public TransferStationTripOrderDetailsViewModel(@NonNull Application application) {
        super(application);
    }


    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });
    public BindingCommand load = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    public BindingCommand grab = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucGrabOrder.call();
        }
    });


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_GOTO_TRANSFER_ORDER_DETAILS_PASSENGER_PHONE_CODE:
                passengerPhone.set((String) event.src);
                uc.ucPassengerPhone.call();
                break;
            case MessageEvent.MSG_GOTO_TRANSFER_ORDER_DETAILS_CANCAL_ORDER_CODE:
                TransferCancalOrderInfo transferCancalOrderInfo = (TransferCancalOrderInfo) event.src;
                uc.ucTransferCancalOrder.setValue(transferCancalOrderInfo);

                break;
            case MessageEvent.MSG_TRANSFER_ORDER_CANCAL_CODE:


                TransferOrderCancalEntity transferOrderCancalEntity = (TransferOrderCancalEntity) event.src;
                uc.ucShowCancalOrderDialog.setValue(transferOrderCancalEntity);
//                    ucShowCancalOrderDialog
                break;
            case MessageEvent.MSG_MINE_TRANSFER_ORDER_SELECT_TIME_CODE:
                passengerOrderNo.set((String) event.src);

                uc.ucShowSelectTime.call();

//                    ucShowCancalOrderDialog
                break;

            case MessageEvent.MSG_GOTO_CHARTTER_SELECT_TIME_CODE:
                String time = (String) event.src;
                chooseAfterDrivingTime(passengerOrderNo.get(), DateUtils.date2TimeStamp(time, DateUtils.normal_formats));

                break;
            // 司机更改时间
            case MessageEvent.MSG_MINE_TRANSFER_ORDER_UPDATE_TIME_CODE:
                TransferOrderUpdateTime transferOrderUpdateTime = (TransferOrderUpdateTime) event.src;
                if (transferOrderUpdateTime != null) {
                    uc.ucShowUpdateTime.setValue(transferOrderUpdateTime);
                }

                break;
            // 导航
            case MessageEvent.MSG_MINE_TRANSFER_ORDER_NAVI_CODE:
                TransferStationRecoverLiteItemEntity transferStationRecoverLiteItemEntity = (TransferStationRecoverLiteItemEntity) event.src;
                if (transferStationRecoverLiteItemEntity != null) {
                    uc.ucNavi.setValue(transferStationRecoverLiteItemEntity);
                }

                break;
            // 更改乘客状态
            case MessageEvent.MSG_MINE_TRANSFER_ORDER_UPDATE_PASSENGER_TYPE_CODE:
                TransferStationRecoverLiteItemEntity updatePassengerType = (TransferStationRecoverLiteItemEntity) event.src;
                uc.ucUpdatePassengerType.setValue(updatePassengerType);

                break;
            case MessageEvent.MSG_MINE_TRANSFER_ORDER_SHOW_PAY_TYPE_CODE:
                TransferStationRecoverLiteItemEntity payEntity = (TransferStationRecoverLiteItemEntity) event.src;
                uc.ucShowQrCode.setValue(payEntity);
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

        }

    }

    public void transferListSuccess(List<TransferStationRecoverLiteItemEntity> list) {
        itemList.clear();
        if (list == null || list.size() <= 0) {
            return;
        }
        for (TransferStationRecoverLiteItemEntity transferStationRecoverLiteItemEntity : list) {

            itemList.add(new TransferStationTripOrderDetailsListItem(this, transferStationRecoverLiteItemEntity, driverState.get()));

        }

    }

    public void updateTransferStationRecoverEntity(TransferStationRecoverEntity recoverEntity) {
        grabOrderShow.set(recoverEntity.getGrabOrderShow());
        sideId.set(recoverEntity.getSiteId());
        buttonText.set(recoverEntity.getButtonText());
        orderType.set(recoverEntity.getOrderType());
        titleText.set(recoverEntity.getTitleText());
        driverState.set(recoverEntity.getDriverState());
        transferListSuccess(recoverEntity.getPassengerList());
        uc.ucOrderDetailsSuccess.call();
    }

    // 恢复订单
    public void tripOrderRecoverDetails(String orderNo) {
        RetrofitFactory.sApiService.recoverTransferStationOrderDetails(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<TransferStationRecoverEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(TransferStationRecoverEntity recoverEntity) {
                        isLoad.set(true);


                        updateTransferStationRecoverEntity(recoverEntity);
                        uc.ucRefreshOrder.setValue(true);

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                        uc.ucRefreshOrder.setValue(true);
                    }
                });
    }

    // 司机取消订单
    public void transferCancalOrder(String orderNo) {

        RetrofitFactory.sApiService.transferCancalOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TAddObserver<TransferCancalOrderEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(TransferCancalOrderEntity transferCancalOrderEntity) {

                        uc.ucDriverCancanOrder.setValue(transferCancalOrderEntity);

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }

                    @Override
                    protected void onFailure(ResultException result) {
                        if (result.getErrCode().equals("-100")) {
                          uc.ucCancanOrderFails.call();
                        }

                        UIUtils.showToast(result.getErrMsg());
                    }
                });
    }

    // 司机更改时间
    public void chooseAfterDrivingTime(String orderNo, long afterDrivingTime) {
        RetrofitFactory.sApiService.chooseAfterDrivingTime(orderNo, afterDrivingTime)
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

                        uc.ucRefreshOrderDetails.call();

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    // 滑动去接乘客/行程开始/行程结束接口
    public void updateTransferStationAction(String orderNo, Double lng, Double lat, int actionType, String currentAddress) {

        RetrofitFactory.sApiService.updateTransferStationAction(new UpdateTransferStationActionEntity(orderNo, lng, lat, actionType, currentAddress))
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<TransferStationRecoverEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(TransferStationRecoverEntity transferStationRecoverEntity) {

                        if (transferStationRecoverEntity.getDriverState() == 2) {
                            YmxCache.setOrderId(transferStationRecoverEntity.getOrderNo());
                        }

                        updateTransferStationRecoverEntity(transferStationRecoverEntity);

                    }

                    @Override
                    protected void onFailure(String message) {

                        if (message.contains("所有乘客")) {
                            uc.ucErrorMsg.setValue(message);
                        } else {
                            UIUtils.showToast(message);
                        }


                    }
                });
    }

    public void transferArrivePlace(String orderNo, Double lng, Double lat, int actionType, String currentAddress) {


        RetrofitFactory.sApiService.transferArrivePlace(new UpdateTransferStationActionEntity(orderNo, lng, lat, actionType, currentAddress))
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<TransferStationRecoverEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(TransferStationRecoverEntity transferStationRecoverEntity) {


                        updateTransferStationRecoverEntity(transferStationRecoverEntity);

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public void orderStartLocation(String orderNo, int actionType, int orderType) {
        getUC().getShowDialogEvent().call();
        ServerLocationManager.getInstance(YmxApp.getInstance()).startLocation(new ServerLocation() {
            @Override
            public void success(AMapLocation amapLocation) {
                getUC().getDismissDialogEvent().call();
                if (amapLocation != null) {

                    if (orderType == 1) {
                        updateTransferStationAction(orderNo, amapLocation.getLongitude(), amapLocation.getLatitude(), actionType, "");
                    } else if (orderType == 2) {
                        transferArrivePlace(orderNo, amapLocation.getLongitude(), amapLocation.getLatitude(), actionType, "");
                    }


                } else {
                    AMapLocation serviceLocation = LocationManager.getInstance(YmxApp.getInstance()).getAMapLocation();

                    if (serviceLocation != null) {
                        if (orderType == 1) {
                            updateTransferStationAction(orderNo, serviceLocation.getLongitude(), serviceLocation.getLatitude(), actionType, "");
                        } else if (orderType == 2) {
                            transferArrivePlace(orderNo, serviceLocation.getLongitude(), serviceLocation.getLatitude(), actionType, "");
                        }
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
                    if (orderType == 1) {
                        updateTransferStationAction(orderNo, serviceLocation.getLongitude(), serviceLocation.getLatitude(), actionType, "");
                    } else if (orderType == 2) {
                        transferArrivePlace(orderNo, serviceLocation.getLongitude(), serviceLocation.getLatitude(), actionType, "");
                    }
                } else {
                    UIUtils.showToast("GPS信号弱，请检查当前信号");
                }

                ServerLocationManager.getInstance(YmxApp.getInstance()).onDestroy();
            }
        });
    }


}
