package com.ymx.driver.viewmodel.main;

import android.app.Application;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.NetChangeEntity;
import com.ymx.driver.entity.app.CancelOrderEntity;
import com.ymx.driver.entity.app.CarStateEntity;
import com.ymx.driver.entity.app.ConfirmOrderEntity;
import com.ymx.driver.entity.app.DriverLockEntity;
import com.ymx.driver.entity.app.HeadDetailInfoEntity;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

import static com.ymx.driver.BR.viewModel;

/**
 * Created by xuweihua
 * 2020/5/4
 */
public class HomeViewModel extends BaseViewModel {

    public static final int autoOrderType = 1;
    public static final int manualOrderType = 2;

    public ObservableField<Integer> driverStatus = new ObservableField<>();  //司机状态：//离线0 在线 1 行程中 2
    public ObservableList<GrapOrderItemViewModel> orderList = new ObservableArrayList<>();
    public ObservableField<String> onlineTime = new ObservableField<>();
    public ObservableField<String> todayOrderNumber = new ObservableField<>();
    public ObservableField<String> todayIncome = new ObservableField<>();
    public ObservableField<Boolean> pendingOrder = new ObservableField<>(false);
    public ObservableField<Integer> lockDrvierState = new ObservableField<>();
    public ObservableField<Integer> drvierType = new ObservableField<>();
    public ItemBinding<GrapOrderItemViewModel> itembinding = ItemBinding.of(viewModel, R.layout.home_frament_grab_order_rv_item);

    public HomeViewModel(@NonNull Application application) {
        super(application);
        loadData();
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {

            // 系统自动派单消息
            case MessageEvent.MSG_PASSENGER_ORDER:
//                if (driverStatus.get() == 0) {
                PassengerInfoEntity passeInfo = (PassengerInfoEntity) event.src;
                uc.ucPassengerInfo.setValue(passeInfo);
                uc.ucShowAutoReceiveDialog.call();
//                }
                orderList.clear();
                getHeadDetailInfo();
                break;
            case MessageEvent.MSG_PASSENGER_CANCEL_ORDER:
                uc.ucSystemCancelOrder.call();
                break;
            //接受到系统过来的推送订单
            case MessageEvent.MSG_PASSENGER_GOOD_FRIEND_ORDER:
                PassengerInfoEntity passengerInfo = (PassengerInfoEntity) event.src;
                setOrder(passengerInfo);
                break;
            //抢好友订单的消息
            case MessageEvent.MSG_GRAB_GOOD_FIREND_ORDER:
                PassengerInfoEntity grabPassenger = (PassengerInfoEntity) event.src;
                uc.ucGrapPassengerInfo.setValue(grabPassenger);
                break;
            case MessageEvent.MSG_GRAB_GOOD_FIREND_ORDER_SUCCESS:
                String orderNo = (String) event.src;
                uc.ucGrapPassengerSucess.setValue(orderNo);
                break;

            case MessageEvent.MSG_ORDER_SERVICE_COMPLETED:
                uc.ucServiceCompleted.call();
                break;

            case MessageEvent.MSG_NET_CHANGE_CODE:

                NetChangeEntity netChangeEntity = (NetChangeEntity) event.src;
                uc.ucNetChange.setValue(netChangeEntity);
                break;
            case MessageEvent.MSG_HOME_FRAGMENT_DATA_CODE:
                orderList.clear();
                getHeadDetailInfo();
                break;
            case MessageEvent.MSG_RANGGE_DRIVING_NEW_ORDER:

                PassengerInfoEntity longDrivingOrder = (PassengerInfoEntity) event.src;
                uc.ucPassengerInfo.setValue(longDrivingOrder);
                uc.ucShowAutoReceiveDialog.call();
                orderList.clear();
                getHeadDetailInfo();

                break;
            case MessageEvent.MSG_GOTO_CHARTTER_NEW_ORDER_CODE:


                PassengerInfoEntity chatterOrder = (PassengerInfoEntity) event.src;
                uc.ucPassengerInfo.setValue(chatterOrder);
                uc.ucShowAutoReceiveDialog.call();
                orderList.clear();
                getHeadDetailInfo();

                break;

            case MessageEvent.MSG_DRIVER_LOCK_CODE:

                if (YmxCache.getDriverType() != 4 || YmxCache.getDriverType() != 2) {
                    getHeadDetailInfo();
                }
                DriverLockEntity lockEntity = (DriverLockEntity) event.src;
                uc.ucLockDrvierHiHt.setValue(lockEntity);

                break;


        }
    }


    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucStartWorking = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucEndWorking = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucServiceCompleted = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucShowAutoReceiveDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGoToTripOrderActicity = new SingleLiveEvent<>();
        public SingleLiveEvent<PassengerInfoEntity> ucPassengerInfo = new SingleLiveEvent<>();
        public SingleLiveEvent<PassengerInfoEntity> ucGrapPassengerInfo = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucGrapPassengerSucess = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucMyWallet = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> refreshSucess = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucCancelOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucSystemCancelOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<NetChangeEntity> ucNetChange = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucTodayOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucTodayIncome = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucLockDrvier = new SingleLiveEvent<>();
        public SingleLiveEvent<DriverLockEntity> ucLockDrvierHiHt = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucSafetyTipsShow = new SingleLiveEvent<>();
    }

    public void loadData() {

        driverStatus.set(Integer.valueOf(0));
    }

    public BindingCommand startWorking = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucStartWorking.call();
        }
    });

    public BindingCommand endWorking = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucEndWorking.call();
        }
    });
    // 去我的行程界面
    public BindingCommand tripOrder = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucGoToTripOrderActicity.call();
        }
    });
    // 我的钱包
    public BindingCommand myWallet = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucMyWallet.call();
        }
    });

    // 今日订单
    public BindingCommand todayOrder = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucTodayOrder.call();
        }
    });

    public BindingCommand goTodayIncome = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucTodayIncome.call();
        }
    });


    public BindingCommand lockDrvier = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucLockDrvier.call();
        }
    });


    public void setOrder(PassengerInfoEntity passengerInfoEntity) {
        if (passengerInfoEntity == null) {
            return;
        }
        orderList.add(new GrapOrderItemViewModel(this, passengerInfoEntity));
    }

    public void setOrderList(List<PassengerInfoEntity> list) {
        orderList.clear();
        if (list.isEmpty() || list.size() <= 0) {
            return;
        }
        for (PassengerInfoEntity entity : list) {
            orderList.add(new GrapOrderItemViewModel(this, entity));
        }

    }

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
                        uc.ucCancelOrder.setValue(s.getMessage());
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    // 抢订单
    public void grabOrder(String orderNo) {
        RetrofitFactory.sApiService.confirmOrder(orderNo, 1)
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

                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GRAB_GOOD_FIREND_ORDER_SUCCESS, s.getOrderNo()));
                        getHeadDetailInfo();
                    }

                    @Override
                    protected void onFailure(String message) {
                        getHeadDetailInfo();
                        UIUtils.showToast(message);
                    }
                });
    }


    // 获取最新信息
    public void getHeadDetailInfo() {
        RetrofitFactory.sApiService.getHeadDetailInfo()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<HeadDetailInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(HeadDetailInfoEntity s) {
                        todayIncome.set(s.getTodayIncome());
                        todayOrderNumber.set(String.valueOf(s.getTodayOrderNumber()));
                        onlineTime.set(s.getOnlineTime());
                        pendingOrder.set(s.isPendingOrder());
                        lockDrvierState.set(s.getLockState());
                        drvierType.set(s.getDriverType());
                        YmxCache.setDriverIntegral(s.getIntegral());
                        YmxCache.setCarState(s.getCarState());
                        YmxCache.setDriverType(s.getDriverType());
                        YmxCache.setLockDriverLock(s.getLockState());
                        setOrderList(s.getOrderInfoList());
                        uc.refreshSucess.call();

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    // 收车
    public void stopWork() {
        RetrofitFactory.sApiService.stopWork()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<CarStateEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(CarStateEntity s) {
                        driverStatus.set(s.getCarState());
                        YmxCache.setCarState(s.getCarState());
                        lockDrvierState.set(s.getLockState());
                        YmxCache.setLockDriverLock(s.getLockState());
                        orderList.clear();
                        getHeadDetailInfo();

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }
    // 发车

    public void startWork(int businessType) {
        RetrofitFactory.sApiService.startWork(businessType)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<CarStateEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(CarStateEntity s) {
                        driverStatus.set(s.getCarState());

                        if (businessType == 1) {
                            uc.ucSafetyTipsShow.call();
                        }
                        YmxCache.setCarState(s.getCarState());
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public void driverLock(int state) {
        RetrofitFactory.sApiService.lockOrUnlock(state)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<DriverLockEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(DriverLockEntity driverLockEntity) {

                        lockDrvierState.set(driverLockEntity.getLockState());
                        YmxCache.setLockDriverLock(driverLockEntity.getLockState());
                        if (driverLockEntity.getLockState() == 1) {
                            UIUtils.showToast(driverLockEntity.getLockTips());
                        }

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


}
