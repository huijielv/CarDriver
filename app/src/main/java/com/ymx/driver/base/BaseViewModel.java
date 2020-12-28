package com.ymx.driver.base;

import android.app.Application;

import com.amap.api.location.AMapLocation;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.TransferNewOrderEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public class BaseViewModel extends AndroidViewModel implements IBaseViewModel, io.reactivex.Observer<Disposable> {
    private UIChangeLiveData uc;
    //弱引用持有
    private WeakReference<LifecycleProvider> lifecycle;
    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private CompositeDisposable compositeDisposable;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
    }

    protected void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    public void injectLifecycleProvider(LifecycleProvider lifecycle) {
        this.lifecycle = new WeakReference<>(lifecycle);
    }

    public LifecycleProvider getLifecycleProvider() {
        return lifecycle.get();
    }

    public UIChangeLiveData getUC() {
        if (uc == null) {
            uc = new UIChangeLiveData();
        }
        return uc;
    }

    public void showLoading() {
        uc.showDialogEvent.call();
    }

    public void dismissLoading() {
        uc.dismissDialogEvent.call();
    }

    public void hideSoftKeyBoard() {
        uc.hideSoftKeyBoard.call();
    }

    /**
     * 定位成功
     */
    public void locateSuccess(AMapLocation aMapLocation) {
        uc.locateSuccess.postValue(aMapLocation);
    }

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {

    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void removeEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.type) {
            case MessageEvent.MSG_LOCATION_SUCCESS:
                locateSuccess((AMapLocation) event.src);
                break;
            case MessageEvent.MSG_TO_MAIN:

                getUC().getToMainEvent().call();
                break;

            case MessageEvent.MSG_TRANSFER_NEW_ORDER_CODE:
                getUC(). getTransferNewOrderEvent().setValue((TransferNewOrderEntity) event.src);


                break;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //ViewModel销毁时会执行，同时取消所有异步任务
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        addSubscribe(d);
    }

    @Override
    public void onNext(Disposable disposable) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    protected void aboutOrder(MessageEvent messageEvent) {

    }

    public final class UIChangeLiveData extends SingleLiveEvent {
        private SingleLiveEvent<String> showDialogEvent;
        private SingleLiveEvent<Void> dismissDialogEvent;
        private SingleLiveEvent<AMapLocation> locateSuccess;
        private SingleLiveEvent<Void> hideSoftKeyBoard;
        private SingleLiveEvent<Void> toMain;
        public SingleLiveEvent<TransferNewOrderEntity> transferNewOrder ;
        public SingleLiveEvent<String> getShowDialogEvent() {
            return showDialogEvent = createLiveData(showDialogEvent);
        }

        public SingleLiveEvent<Void> getDismissDialogEvent() {
            return dismissDialogEvent = createLiveData(dismissDialogEvent);
        }

        public SingleLiveEvent<AMapLocation> getLocateSuccessEvent() {
            return locateSuccess = createLiveData(locateSuccess);
        }

        public SingleLiveEvent<Void> getHideSoftKeyBoardEvent() {
            return hideSoftKeyBoard = createLiveData(hideSoftKeyBoard);
        }

        public SingleLiveEvent<Void> getToMainEvent() {
            return toMain = createLiveData(toMain);
        }

        public SingleLiveEvent<TransferNewOrderEntity> getTransferNewOrderEvent() {
            return transferNewOrder = createLiveData(transferNewOrder);
        }

        private <T> SingleLiveEvent<T> createLiveData(SingleLiveEvent<T> liveData) {
            if (liveData == null) {
                liveData = new SingleLiveEvent<>();
            }
            return liveData;
        }

        @Override
        public void observe(LifecycleOwner owner, Observer observer) {
            super.observe(owner, observer);
        }
    }
}
