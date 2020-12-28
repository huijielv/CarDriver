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
import com.ymx.driver.entity.app.FindCarpoolingSiteResultEntity;
import com.ymx.driver.entity.app.SiteOrderListItem;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class TranSportSiteViewModelUpdateStateViewModel extends BaseTranSportSiteViewModel {

    public ObservableField<Boolean> isLoad = new ObservableField<>(true);
    public ObservableField<Integer> carState = new ObservableField<>();

    public ObservableList<SiteListItemViewModel> itemList = new ObservableArrayList<>();
    public ItemBinding<SiteListItemViewModel> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.site_list_item_ll);

    public ObservableList<SiteOrderListItemViewModel> siteOrderList = new ObservableArrayList<>();
    public ItemBinding<SiteOrderListItemViewModel> siteOrderListItembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.site_order_list_item_ll);


    public TranSportSiteViewModelUpdateStateViewModel(@NonNull Application application) {
        super(application);
    }


    public BindingCommand stopWork = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (carState != null && carState.get() != null && carState.get() == 1) {
                updateCarState(0);
            } else if (carState != null && carState.get() != null && carState.get() == 2) {
                UIUtils.showToast("行程中");
            }

        }
    });

    public BindingCommand clickTripList = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            transport.ucGoToTripList.call();

        }
    });


    public UIChangeObservable transport = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Boolean> ucRefreshOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<FindCarpoolingSiteResultEntity> ucViewShow = new SingleLiveEvent<>();
        public SingleLiveEvent<FindCarpoolingSiteResultEntity> ucInitSideIdList = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGoToTripList = new SingleLiveEvent<>();
        public SingleLiveEvent<Integer> ucSideId = new SingleLiveEvent<>();
    }


    public BindingCommand load = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            findCarpoolingSite();

        }
    });

    public void siteOrderListSuccess(List<SiteOrderListItem> list) {
        siteOrderList.clear();
        if (list == null || list.size() <= 0) {
            return;
        }
        for (SiteOrderListItem siteOrderListItem : list) {
            siteOrderList.add(new SiteOrderListItemViewModel(this, siteOrderListItem));
        }

    }

    public void siteListItemSuccess(List<String> list) {
        siteOrderList.clear();
        if (list == null || list.size() <= 0) {
            return;
        }
        for (String info : list) {
            itemList.add(new SiteListItemViewModel(this, info));
        }

    }

  // 接送站信息
    public void findCarpoolingSite() {
        RetrofitFactory.sApiService.findCarpoolingSite()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<FindCarpoolingSiteResultEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(FindCarpoolingSiteResultEntity findCarpoolingSiteResultEntity) {
                        transport.ucViewShow.setValue(findCarpoolingSiteResultEntity);
                        carState.set(findCarpoolingSiteResultEntity.getCarState());
                        siteListItemSuccess(findCarpoolingSiteResultEntity.getSiteList());
                        siteOrderListSuccess(findCarpoolingSiteResultEntity.getSiteOrderList());
                        transport.ucRefreshOrder.call();
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                        transport.ucRefreshOrder.call();

                    }
                });
    }


    public void updateCarState(int state) {
        RetrofitFactory.sApiService.updateCarState(state)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<FindCarpoolingSiteResultEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(FindCarpoolingSiteResultEntity findCarpoolingSiteResultEntity) {
                        transport.  ucInitSideIdList.setValue(findCarpoolingSiteResultEntity);
                        transport.ucViewShow.setValue(findCarpoolingSiteResultEntity);
                        carState.set(findCarpoolingSiteResultEntity.getCarState());
                        itemList.clear();
                        siteOrderList.clear();
                        siteListItemSuccess(findCarpoolingSiteResultEntity.getSiteList());
                        siteOrderListSuccess(findCarpoolingSiteResultEntity.getSiteOrderList());
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
            //
            case MessageEvent.MSG_GO_TO_TRANSFER_SIDE_ORDER_CODE :
                int sideId = (int) event.src;
                transport.ucSideId.setValue(sideId);
                break;
        }
    }

}
