package com.ymx.driver.viewmodel.triporderlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.TripOrderList;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.StatusView;
import com.ymx.driver.viewmodel.main.MineActionItemViewModel;
import com.ymx.driver.viewmodel.orderdetails.OrderItemViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class NotFinishTripViewModel extends BaseViewModel {

    public ObservableInt status = new ObservableInt(StatusView.STATUS_NORMAL);
    public ObservableList<TripOrderListItemViewModel> itemList = new ObservableArrayList<>();
    public ObservableField<Integer> pagerIndex = new ObservableField<>(1);
    public SingleLiveEvent<Boolean> ucCanLoadmore = new SingleLiveEvent<>();
    public ItemBinding<TripOrderListItemViewModel> itembinding = ItemBinding.of(BR.viewModel, R.layout.trip_order_list_item_ll);

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Boolean> ucCanLoadmore = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> ucRefreshOrder = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucTripRefreshData = new SingleLiveEvent<>();

    }


    public NotFinishTripViewModel(@NonNull Application application) {
        super(application);
    }

    public void getTripOrderListSuccess(List<TripOrderList> list) {
        if (list == null || list.size() <= 0) {
            if (pagerIndex.get() == 1) {
                if (itemList.size() == 0) {
                    status.set(StatusView.STATUS_EMPTY);
                }
            }

            return;
        }

        if (list.size() < 10) {
            uc.ucCanLoadmore.setValue(true);
        } else {
            uc.ucCanLoadmore.setValue(true);
        }

        for (TripOrderList tripOrderList : list) {
            itemList.add(new TripOrderListItemViewModel(this, tripOrderList));
        }

        for (int i = 0; i < itemList.size(); i++) {

            if (itemList.size() > 1 && i > 0) {
                TripOrderListItemViewModel previous = itemList.get(i - 1);
                TripOrderListItemViewModel tripOrderList = itemList.get(i);
                try {
                    if (previous != null && tripOrderList != null && previous.entity.get().getWeek().equals(tripOrderList.entity.get().getWeek()) && previous.entity.get().getDayTime().equals(tripOrderList.entity.get().getDayTime())) {
                        tripOrderList.setIsShow(true);
                    }
                } catch (Exception e) {

                }

            }

        }

    }

    public void refreshOrderList(List<TripOrderList> list) {
        if (list == null || list.size() <= 0) {
            if (pagerIndex.get() == 1) {
                if (itemList.size() == 0) {
                    status.set(StatusView.STATUS_EMPTY);
                }
            }

            return;
        }


        for (TripOrderList tripOrderList : list) {

            itemList.add(new TripOrderListItemViewModel(this, tripOrderList));
        }

        for (int i = 0; i < itemList.size(); i++) {

            if (itemList.size() > 1 && i > 0) {
                TripOrderListItemViewModel previous = itemList.get(i - 1);
                TripOrderListItemViewModel tripOrderList = itemList.get(i);
                try {
                    if (previous != null && tripOrderList != null && previous.entity.get().getWeek().equals(tripOrderList.entity.get().getWeek()) && previous.entity.get().getDayTime().equals(tripOrderList.entity.get().getDayTime())) {
                        tripOrderList.setIsShow(true);

                    }
                } catch (Exception e) {

                }


            }

        }


    }


    public void getTripOrderList(int orderState, int pagesize, int currentPage) {
        RetrofitFactory.sApiService.getTripOrderList(orderState + "", pagesize, currentPage)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<List<TripOrderList>>() {
                    @Override
                    protected void onRequestStart() {

                    }

                    @Override
                    protected void onRequestEnd() {

                    }

                    @Override
                    protected void onSuccees(List<TripOrderList> list) {
                        getTripOrderListSuccess(list);
                    }

                    @Override
                    protected void onFailure(String message) {
                        uc.ucRefreshOrder.setValue(true);
                        UIUtils.showToast(message);
                    }
                });
    }


    public void refreshOrder(int orderState, int pagesize, int currentPage) {
        RetrofitFactory.sApiService.getTripOrderList(orderState + "", pagesize, currentPage)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<List<TripOrderList>>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(List<TripOrderList> list) {
                        uc.ucRefreshOrder.setValue(true);
                        refreshOrderList(list);
                    }

                    @Override
                    protected void onFailure(String message) {
                        uc.ucRefreshOrder.setValue(true);
                        status.set(StatusView.STATUS_ERROR);
                        UIUtils.showToast(message);
                    }
                });
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_TRIP_REFRESH_DATA_CODE:
                uc.ucTripRefreshData.call();
                break;
            case MessageEvent.MSG_CHARTTER_ORDER_STATUS_UPDATE_CODE:
//                uc.ucTripRefreshData.call();
                break;

        }
    }


}
