package com.ymx.driver.viewmodel.transportsite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.R;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.BaseEntity;
import com.ymx.driver.entity.app.TransferOrderCancalEntity;
import com.ymx.driver.entity.app.TransferStationRecoverEntity;
import com.ymx.driver.entity.app.TransferStationTripOrderListItem;
import com.ymx.driver.http.ResultException;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TAddObserver;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class TransferStationTripOrderListViewModel extends BaseTranSportSiteViewModel {

    public ObservableField<Integer> selectTypeNum = new ObservableField<>(0);
    public ObservableField<String> selectTypeHiht = new ObservableField<>("全部");
    public ObservableField<String> orderNo = new ObservableField<>();
    public ObservableField<String> tripOrderListTitle = new ObservableField<>();
    public ObservableField<Boolean> more = new ObservableField<>(false);
    public ObservableField<Integer> giveUpOrderResult = new ObservableField<>(0);

    public ObservableField<Boolean> isShow = new ObservableField<>(false);

    public UIChangeObservable ucStationTrip = new UIChangeObservable();
    public ObservableList<TransferStationTripOrderListItemViewModel> itemList = new ObservableArrayList<>();
    public ItemBinding<TransferStationTripOrderListItemViewModel> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.transfer_station_trip_order_list_ll);

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucSecectType = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGiveUpOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucShowTimeNavalidDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucGoToDetails = new SingleLiveEvent<>();
        public SingleLiveEvent<TransferOrderCancalEntity> ucShowCancalOrderDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<String> ucJoin = new SingleLiveEvent<>();

        public SingleLiveEvent<Void> ucLoadDataSuccess = new SingleLiveEvent<>();
    }


    public TransferStationTripOrderListViewModel(@NonNull Application application) {
        super(application);
        isShow.set(false);
    }

    public BindingCommand selectTripOrder = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ucStationTrip.ucSecectType.call();
        }
    });


    public void transferStationTripOrderListSuccess(List<TransferStationTripOrderListItem> list) {
        if (list == null || list.size() <= 0) {
            if (itemList.size() <= 0) {
                more.set(true);
            }
            return;
        }

//        if (itemList.size()){
        more.set(false);


        for (TransferStationTripOrderListItem siteOrderListItem : list) {

            itemList.add(new TransferStationTripOrderListItemViewModel(this, siteOrderListItem, isShow.get()));
        }

    }

    public void findTripOrderList(int type) {
        RetrofitFactory.sApiService.findTripOrderList(type)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<List<TransferStationTripOrderListItem>>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(List<TransferStationTripOrderListItem> listItems) {
                        itemList.clear();
                        transferStationTripOrderListSuccess(listItems);
                        ucStationTrip.ucLoadDataSuccess.call();
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                        more.set(true);
                    }
                });
    }

    public void findTripOrderList(String orderNo) {
        RetrofitFactory.sApiService.findOptionalTripOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<List<TransferStationTripOrderListItem>>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(List<TransferStationTripOrderListItem> listItems) {
                        itemList.clear();
                        transferStationTripOrderListSuccess(listItems);
                        ucStationTrip.ucLoadDataSuccess.call();
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                        more.set(true);
                    }
                });
    }

    public void joinOrder(String orderNo, String joinOrderNo) {
        RetrofitFactory.sApiService.joinTripOrder(orderNo, joinOrderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TAddObserver<TransferStationRecoverEntity>() {
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
                        ucStationTrip.ucJoin.setValue(recoverEntity.getOrderNo());
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }

                    @Override
                    protected void onFailure(ResultException result) {
                        UIUtils.showToast(result.getErrMsg());
                        giveUpOrderResult.set(1);
                    }
                });
    }


    public void giveUpOrder(String orderNo) {
        RetrofitFactory.sApiService.giveUpOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TAddObserver<BaseEntity>() {
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
                        ucStationTrip.ucGiveUpOrder.call();
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }

                    @Override
                    protected void onFailure(ResultException result) {
                        UIUtils.showToast(result.getErrMsg());
                        giveUpOrderResult.set(1);
                    }
                });
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            //
            case MessageEvent.MSG_GO_TO_TRANSFER_JOIN_ORDER_CODE:
                int joinOrder = (int) event.src;
                joinOrder(orderNo.get(), String.valueOf(joinOrder));
                break;
            case MessageEvent.MSG_GO_TO_TRANSFER_TIME_INVALID_CODE:
                if (!isShow.get()) {
                    ucStationTrip.ucShowTimeNavalidDialog.setValue((String) event.src);
                }
                break;

            case MessageEvent.MSG_TRANSFER_ORDER_CANCAL_CODE:

                if (!isShow.get()) {
                    TransferOrderCancalEntity transferOrderCancalEntity = (TransferOrderCancalEntity) event.src;
                    ucStationTrip.ucShowCancalOrderDialog.setValue(transferOrderCancalEntity);
//                    ucShowCancalOrderDialog
                }
                break;
            case MessageEvent.MSG_GOTO_TRANSFER_ORDER_DETAILS_CODE:
                if (isShow.get()) {
                    ucStationTrip.ucGoToDetails.setValue((String) event.src);
                }
                break;

        }
    }
}
