package com.ymx.driver.viewmodel.longrangdriving;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.MsgEntity;
import com.ymx.driver.entity.app.OperationLineListItem;
import com.ymx.driver.entity.app.RemoteInfoEntity;
import com.ymx.driver.entity.app.UpdateFerryRemoteInfoEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.StatusView;
import com.ymx.driver.viewmodel.main.MsgItemViewModel;


import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class LongRangeFerryDrvingViewModel extends BaseViewModel {
    public LongRangeFerryDrvingViewModel(@NonNull Application application) {
        super(application);
        isLoad.set(false);
    }

    public ObservableField<Integer> remoteState = new ObservableField<>();
    public ObservableField<Integer> lockDrvierState = new ObservableField<>();
    public ObservableField<Boolean> isLoad = new ObservableField<>();
    public ObservableField<String> lockTips = new ObservableField<>();

    public UIChangeObservable uc = new UIChangeObservable();


    public ObservableList<OperationLineListItemViewModel> list = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<OperationLineListItemViewModel> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.operation_line_list_item_view);

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucStopWork = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucLockDrvier = new SingleLiveEvent<>();
        public SingleLiveEvent<RemoteInfoEntity> ucInitData = new SingleLiveEvent<>();
        public SingleLiveEvent<UpdateFerryRemoteInfoEntity> ucInit = new SingleLiveEvent<>();
    }


    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });


    public BindingCommand stopWork = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucStopWork.call();
        }
    });
    public BindingCommand lockDrvier = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucLockDrvier.call();
        }
    });



        private void loadListSuccess(List<OperationLineListItem> datalist) {
            list.clear();
            if (datalist == null ||datalist.size() <= 0) {

                return;
            }
            for (OperationLineListItem entity : datalist) {
                list.add(new OperationLineListItemViewModel(this, entity));
            }
        }


    public BindingCommand load = new BindingCommand(new BindingAction() {
        @Override
        public void call() {


            getRemoteInfo();

        }
    });


    public void getRemoteInfo() {
        RetrofitFactory.sApiService.getRemoteInfo()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<RemoteInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(RemoteInfoEntity remoteInfo) {
                        lockTips.set(remoteInfo.getLockTips());
                        remoteState.set(remoteInfo.getRemoteState());
                        lockDrvierState.set(remoteInfo.getLockState());
                        uc.ucInitData.setValue(remoteInfo);
                        loadListSuccess(remoteInfo.getOperationLineList());
                        isLoad.set(true);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });


    }

    public void updateFerryRemoteInfo(int state, int lockState) {
        RetrofitFactory.sApiService.updateFerryRemoteInfo(state, lockState)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<UpdateFerryRemoteInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(UpdateFerryRemoteInfoEntity updateFerryRemoteInfoEntity) {
                        lockTips.set(updateFerryRemoteInfoEntity.getLockTips());
                        remoteState.set(updateFerryRemoteInfoEntity.getRemoteState());
                        lockDrvierState.set(updateFerryRemoteInfoEntity.getLockState());
                        uc.ucInit.setValue(updateFerryRemoteInfoEntity);

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
            case MessageEvent.MSG_DRIVER_LOCK_CODE:
                getRemoteInfo();
                break;

        }
    }

}
