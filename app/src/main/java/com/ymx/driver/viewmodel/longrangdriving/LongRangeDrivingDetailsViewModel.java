package com.ymx.driver.viewmodel.longrangdriving;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.amap.api.maps.model.LatLng;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.RangDrivingCancelOrder;
import com.ymx.driver.entity.app.LongDrivingPaySuccessEntigy;
import com.ymx.driver.entity.app.LongRangCustomerPremiumStatus;
import com.ymx.driver.entity.app.OrderCirculationEntity;
import com.ymx.driver.entity.app.OrderCirculationInfo;
import com.ymx.driver.entity.app.PassengerItemInfo;
import com.ymx.driver.entity.app.UpdateOrderEntity;

import com.ymx.driver.entity.app.mqtt.LongDriverSysTemCancalOrder;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.http.UpdateLongDrivingOrderActionBodyEntity;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.travel.TravelViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LongRangeDrivingDetailsViewModel extends BaseViewModel {

    public ObservableField<String> phoneNum = new ObservableField<>();
    public ObservableField<String> passengerInfo = new ObservableField<>();
    public ObservableField<String> updateOrderContent = new ObservableField<>();
    public ObservableField<String> orderId = new ObservableField<>();
    public ObservableField<String> endName = new ObservableField<>();
    public ObservableField<String> startName = new ObservableField<>();
    public ObservableField<Integer> orderStatus = new ObservableField<>();
    public ObservableField<String> orderTimeDesc = new ObservableField<>();
    public ObservableField<Double> lat = new ObservableField<>();
    public ObservableField<Double> lng = new ObservableField<>();
    public ObservableField<Integer> channel = new ObservableField<>();
    public ObservableField<Boolean> isLoad = new ObservableField<>();
    public ObservableField<Boolean> loadMapInfo = new ObservableField<>();
    public ObservableInt mode = new ObservableInt(TravelViewModel.DRIVER_STATE_TO_PASSENGERS);
    public ObservableField<String> driverId = new ObservableField<>();

    public ObservableArrayList<LatLng> smoothRunningLatLng = new ObservableArrayList<>();
    public ObservableField<Integer> initWatiDialog = new ObservableField<>(0);

    public LongRangeDrivingDetailsViewModel(@NonNull Application application) {
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
     * 7 已完成
     */
    public static final int DRIVER_ORDER_FINISH = 7;

    /**
     * 8 已关闭
     */
    public static final int DRIVER_ORDER_CLOSED = 8;


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


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucCall = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucNavigate = new SingleLiveEvent<>();
        public SingleLiveEvent<PassengerInfoEntity> ucUpdateOrderStatus = new SingleLiveEvent<>();
        public SingleLiveEvent<PassengerInfoEntity> ucRecoverOrderDetails = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucChangeMap = new SingleLiveEvent<>();
        public SingleLiveEvent<List<PassengerItemInfo>> uclist = new SingleLiveEvent<>();
        public SingleLiveEvent<RangDrivingCancelOrder> ucCancelOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucPayDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucUpdateOrderInfo = new SingleLiveEvent<>();
        public SingleLiveEvent<LongDrivingPaySuccessEntigy> ucPaySuccess = new SingleLiveEvent<>();
        public SingleLiveEvent<LongDriverSysTemCancalOrder> ucSysTemCancalOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<OrderCirculationEntity> ucOrderCirculation = new SingleLiveEvent<>();
        public SingleLiveEvent<OrderCirculationInfo> ucOrderCirculationNotice = new SingleLiveEvent<>();
        public SingleLiveEvent<List<PassengerItemInfo>> updatePassengerList = new SingleLiveEvent<>();
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


    public void updateOrderDetailsStatus(String orderNo, double loadLng, double loadLat, int type, String address) {
        RetrofitFactory.sApiService.rangeDrivingRemoteOrderAction(new UpdateLongDrivingOrderActionBodyEntity(orderNo, String.valueOf(loadLng), String.valueOf(loadLat), String.valueOf(type), address))
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
                        loadMapInfo.set(true);
                        updateOrderContent.set(orderDetailsEntity.getButtonText());
                        if (orderDetailsEntity.getCirculationState() == 0) {
                            endName.set(orderDetailsEntity.getDesName());
                            if (orderDetailsEntity.getDriverState() == DRIVER_STATE_ROADING || orderDetailsEntity.getDriverState() == DRIVER_STATE_CONFIRM_COST) {
                                startName.set(orderDetailsEntity.getDesName());
                            } else {
                                startName.set(orderDetailsEntity.getSrcName());
                            }
                        }

                        uc.ucUpdateOrderStatus.setValue(orderDetailsEntity);


                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public void cancelOrder(String orderNo) {
        RetrofitFactory.sApiService.rangeDrivingCancalOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<RangDrivingCancelOrder>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(RangDrivingCancelOrder s) {

                        if (s.getDriverState() == DRIVER_ORDER_CLOSED) {
                            YmxCache.setOrderId("");
                            uc.ucBack.call();
                        } else {
                            uc.ucCancelOrder.setValue(s);
                        }


                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
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
                        isLoad.set(true);
                        int waitNumber = initWatiDialog.get();
                        initWatiDialog.set(++waitNumber);
                        mode.set(orderDetailsEntity.getDriverState());
                        orderStatus.set(orderDetailsEntity.getDriverState());
                        endName.set(orderDetailsEntity.getDesName());
                        if (orderDetailsEntity.getDriverState() == DRIVER_STATE_ROADING || orderDetailsEntity.getDriverState() == DRIVER_STATE_CONFIRM_COST) {
                            startName.set(orderDetailsEntity.getDesName());
                        } else {
                            startName.set(orderDetailsEntity.getSrcName());
                        }
                        channel.set(orderDetailsEntity.getChannel());
                        updateOrderContent.set(orderDetailsEntity.getButtonText());
                        lat.set(orderDetailsEntity.getLat());
                        lng.set(orderDetailsEntity.getLng());
                        orderId.set(orderDetailsEntity.getOrderNo());
                        uc.ucRecoverOrderDetails.setValue(orderDetailsEntity);
                        uc.uclist.setValue(orderDetailsEntity.getPassengerList());

                    }

                    @Override
                    protected void onFailure(String message) {
                        if (!message.equals("订单已被取消")){
                            UIUtils.showToast(message);
                        }

                    }
                });
    }


    public void orderCirculation(String orderNo, String driverId) {
        RetrofitFactory.sApiService.orderCirculation(orderNo, driverId)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<OrderCirculationEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(OrderCirculationEntity orderCirculationEntity) {
                        uc.ucOrderCirculation.setValue(orderCirculationEntity);

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    public void updatePassengerListInfo(String orderNo) {
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
                        isLoad.set(true);
                        int waitNumber = initWatiDialog.get();
                        initWatiDialog.set(++waitNumber);
                        uc.updatePassengerList.setValue(orderDetailsEntity.getPassengerList());


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

                LongDrivingPaySuccessEntigy paySuccessEntigy = (LongDrivingPaySuccessEntigy) event.src;
                uc.ucPaySuccess.setValue(paySuccessEntigy);

                break;

            case MessageEvent.MSG_QUERY_LONG_DRIVIER_SYSTEM_CALCAL_ORDER:
                LongDriverSysTemCancalOrder longDriverSysTemCancalOrder = (LongDriverSysTemCancalOrder) event.src;

                if (longDriverSysTemCancalOrder.getDriverState() == DRIVER_ORDER_CLOSED) {
                    YmxCache.setOrderId("");
                }
                uc.ucSysTemCancalOrder.setValue(longDriverSysTemCancalOrder);
                break;

            case MessageEvent.MSG_UPDATE_ORDER_INFO:
                UpdateOrderEntity updateOrderEntity = (UpdateOrderEntity) event.src;

                uc.ucUpdateOrderInfo.setValue(updateOrderEntity.getOrderNo());


                break;

            case MessageEvent.MSG_DRIVER_ID_CODE:

                String info = (String) event.src;
                driverId.set(info);


                break;
            case MessageEvent.MSG_ORDER_CIRCULATION_FAILE_NOTICE:

                uc.ucOrderCirculationNotice.setValue((OrderCirculationInfo) event.src);


                break;

            case MessageEvent.MSG_ORDER_CIRCULATION_SUCCESS_NOTICE:
                OrderCirculationInfo circulationInfo = (OrderCirculationInfo) event.src;
                UIUtils.showToast(circulationInfo.getTips());
                uc.ucBack.call();
                break;
            case MessageEvent.MSG_ORDER_CIRCULATION_NOTICE_QCODE_TO_DETAILS_CODE:

                uc.ucOrderCirculation.setValue((OrderCirculationEntity) event.src);
                break;
            case MessageEvent.MSG_ORDER_LONG_RANGE_COMFIRM_PRICE_CODE:


                loadMapInfo.set(false);
                PassengerInfoEntity orderDetailsEntity = (PassengerInfoEntity) event.src;
                int waitNumber = initWatiDialog.get();
                initWatiDialog.set(++waitNumber);
                updateOrderContent.set(orderDetailsEntity.getButtonText());
                uc.ucUpdateOrderStatus.setValue(orderDetailsEntity);

                break;

            case MessageEvent.MSG_ORDER_LONG_RANGE_CANCAL_PRICE_CODE:
                UIUtils.showToast("撤回成功");
                updatePassengerListInfo(orderId.get());
                break;

            case MessageEvent.MSG_ORDER_LONG_RANGE_CUSTOMER_TRIPS_CODE:

                LongRangCustomerPremiumStatus longRangCustomerPremiumStatus = (LongRangCustomerPremiumStatus) event.src;
                if (longRangCustomerPremiumStatus.getState() == 0) {
                    UIUtils.showToast("加价成功");
                }

                updatePassengerListInfo(orderId.get());

                break;
            case MessageEvent.MSG_ORDER_LONG_RANGE_RECOVER_ORDER_DETAILSCODE:

                recoverOrderDetails(orderId.get());
                break;
        }
    }

    public BindingCommand load = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            if (orderId != null && !TextUtils.isEmpty(orderId.get())) {

                recoverOrderDetails(orderId.get());

            }


        }
    });


}
