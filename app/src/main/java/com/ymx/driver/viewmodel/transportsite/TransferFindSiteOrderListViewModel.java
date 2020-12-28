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
import com.ymx.driver.entity.app.SiteOrderList;
import com.ymx.driver.entity.app.TransferFindSiteOrderListEntity;
import com.ymx.driver.entity.app.TransferStationGrabOrder;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.UIUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class TransferFindSiteOrderListViewModel extends BaseTranSportSiteViewModel {

    public ObservableField<String> siteName = new ObservableField<>();
    public ObservableField<String> orderStateName = new ObservableField<>();
    public ObservableField<String> siteTitleText = new ObservableField<>();


    public ObservableField<Integer> selectTypeNum = new ObservableField<>(0);
    public ObservableField<String> selectTypeHiht = new ObservableField<>("全部");
    public ObservableField<Integer> pagerIndex = new ObservableField<>(1);

    public ObservableList<FindSiteOrderListItemView> itemList = new ObservableArrayList<>();
    public ItemBinding<FindSiteOrderListItemView> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.transfer_find_side_order_list_item_lll);
    public UIChangeObservable ucStationTrip = new UIChangeObservable();




    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucSecectType = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> onGrabFails = new SingleLiveEvent<>();
        public SingleLiveEvent<TransferStationGrabOrder> ucGrapOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> onUcTripList = new SingleLiveEvent<>();
        public SingleLiveEvent<String> onGrabOrder = new SingleLiveEvent<>();
    }

    public BindingCommand selectTripOrder = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ucStationTrip.ucSecectType.call();
        }
    });

    public BindingCommand startTrip = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ucStationTrip.onUcTripList.call();
        }
    });



    public TransferFindSiteOrderListViewModel(@NonNull Application application) {
        super(application);
    }

    public void findSiteOrderListSuccess(List<SiteOrderList> list) {

        if (list == null || list.size() <= 0) {
            return;
        }
        for (SiteOrderList siteOrderListItem : list) {
            itemList.add(new FindSiteOrderListItemView(this, siteOrderListItem));
        }

    }


    public void findTransferFindSiteOrderList(int pageSize, int currentPage, int siteId, int orderType) {
        RetrofitFactory.sApiService.findSiteOrderList(pageSize, currentPage, siteId, orderType)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<TransferFindSiteOrderListEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(TransferFindSiteOrderListEntity transferFindSiteOrderListEntity) {
                        siteName.set(transferFindSiteOrderListEntity.getSiteName());
                        orderStateName.set(transferFindSiteOrderListEntity.getOrderStateName());
                        siteTitleText.set(transferFindSiteOrderListEntity.getTitleText());
                        findSiteOrderListSuccess(transferFindSiteOrderListEntity.getSiteOrderList());
                        uc.ucRefreshOrder.setValue(true);
                    }

                    @Override
                    protected void onFailure(String message) {
                        uc.ucRefreshOrder.setValue(true);
                        UIUtils.showToast(message);
                    }
                });
    }


    public void ransferStationGrabOrder(String orderNo) {
        RetrofitFactory.sApiService.transferStationGrabOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<TransferStationGrabOrder>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(TransferStationGrabOrder transferStationGrabOrder) {
                        ucStationTrip.ucGrapOrder.setValue(transferStationGrabOrder);
                    }

                    @Override
                    protected void onFailure(String message) {
                        uc.ucRefreshOrder.setValue(true);
                        UIUtils.showToast(message);
                        ucStationTrip.onGrabFails.call();
                    }
                });
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            //
            case MessageEvent.MSG_GO_TO_TRANSFER_GRAP_ORDER_CODE:
                String orderNo = (String) event.src;


                ucStationTrip . onGrabOrder.setValue(orderNo);

                break;
        }
    }
}
