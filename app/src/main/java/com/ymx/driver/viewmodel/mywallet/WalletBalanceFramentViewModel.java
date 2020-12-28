package com.ymx.driver.viewmodel.mywallet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.IncomeDetailEntity;
import com.ymx.driver.entity.app.TripOrderList;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.triporderlist.NotFinishTripViewModel;
import com.ymx.driver.viewmodel.triporderlist.TripOrderListItemViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class WalletBalanceFramentViewModel extends BaseViewModel {
    public SingleLiveEvent<Boolean> ucCanLoadmore = new SingleLiveEvent<>();
    public ObservableField<Integer> pagerIndex = new ObservableField<>(1);
    public ObservableList<WalletBalanceListItemViewModel> itemList = new ObservableArrayList<>();
    public ItemBinding<WalletBalanceListItemViewModel> itembinding = ItemBinding.of(BR.viewModel, R.layout.wallet_balance_list_item_view);

    public WalletBalanceFramentViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Boolean> ucCanLoadmore = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> ucRefreshOrder = new SingleLiveEvent<>();

    }

    public void getListSuccess(List<IncomeDetailEntity> list) {
        if (list == null || list.size() <= 0) {
            return;
        }

        if (list.size() < 10) {
            uc.ucCanLoadmore.setValue(true);
        } else {
            uc.ucCanLoadmore.setValue(true);
        }


        for (IncomeDetailEntity tripOrderList : list) {
            itemList.add(new WalletBalanceListItemViewModel(this, tripOrderList));
        }

        for (int i = 0; i < itemList.size(); i++) {

            if (itemList.size() > 1 && i > 0) {
                WalletBalanceListItemViewModel previous = itemList.get(i - 1);
                WalletBalanceListItemViewModel walletBalanceListItemViewModel = itemList.get(i);
                try {
                    if (previous.entity.get().getDayTime().equals(walletBalanceListItemViewModel.entity.get().getDayTime())) {
                        walletBalanceListItemViewModel.setIsShow(true);
                    }
                } catch (Exception e) {

                }

            }

        }

    }


    public void getIncomeDetailList(String tradeType, int page, int currentPage) {

        RetrofitFactory.sApiService.getIncomeDetailList(tradeType, page, currentPage)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<List<IncomeDetailEntity>>() {
                    @Override
                    protected void onRequestStart() {

                    }

                    @Override
                    protected void onRequestEnd() {

                    }

                    @Override
                    protected void onSuccees(List<IncomeDetailEntity> list) {
                        getListSuccess(list);
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

            // 系统自动派单消息
            case MessageEvent.MSG_WITHDRAWAL_DATA_CODE:
                itemList.clear();
                pagerIndex.set(1);
                getIncomeDetailList(String.valueOf(2), 10, pagerIndex.get());

                break;

        }
    }


}
